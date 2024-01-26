package swt.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import swt.dto.*;
import swt.exception.*;
import swt.model.*;
import swt.repository.GroupRepository;
import swt.util.DataMapper;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository repository;

    private static final Logger LOGGER = LogManager.getLogger(CommentService.class);

    public String createGroup(GroupDTO groupDTO, Principal authUser) {

        if (groupDTO.getName().isBlank()) {
            throw new FieldBlankException("Group name");
        }

        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();

        var group = Group.builder()
                .name(groupDTO.getName())
                .description(groupDTO.getDescription())
                .creationDate(LocalDateTime.now())
                .isSuspended(false)
                .groupAdmins(new ArrayList<>())
                .users(new ArrayList<>())
                .posts(new ArrayList<>())
                .build();

        var groupAdmin = new GroupAdmin(user, group);
        group.getGroupAdmins().add(groupAdmin);
        group.getUsers().add(GroupRequest.builder()
                .createdAt(LocalDateTime.now())
                .approved(true)
                .at(LocalDateTime.now())
                .user(user)
                .group(group)
                .build()
        );
        try {
            var groupId = repository.save(group).getId();
            LOGGER.info("User {} created group with id {}", user.getUsername(), groupId);
            return "Group created successfully";
        } catch (Exception e) {
            LOGGER.error("Error creating group", e);
            throw new GroupCreationException();
        }
    }

    public GroupInfoDataDTO getGroupInfo(Long groupId, Principal authUser) {

        var group = repository.findByIdAndIsSuspendedIs(groupId, false).orElseThrow(() -> new ItemNotFoundException("Group"));
        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();

        LOGGER.info("User {} requested info for group with id {}", user.getUsername(), groupId);
        var groupDTO = DataMapper.mapGroupToGroupInfoDTO(group);
        groupDTO.setIsUserInGroup(isUserInGroup(group, user));
        return groupDTO;
    }

    public String suspendGroup(Long groupId, String suspendedReason, Principal authUser) {

        if (suspendedReason == null || suspendedReason.isBlank()) {
            throw new SuspendedReasonNeededException();
        }

        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();
        var group = repository.findById(groupId).orElseThrow(() -> new ItemNotFoundException("Group"));

        if (group.getIsSuspended()) {
            throw new GroupAlreadySuspendedException();
        }

        group.setIsSuspended(true);
        group.setSuspendedReason(suspendedReason);
        group.getGroupAdmins().clear();
        repository.save(group);


        LOGGER.info("System admin {} suspended group with id {}", user.getUsername(), groupId);
        return "Group suspended successfully";
    }

    public String createPost(Long groupId, String postContent, Principal authUser) {

        var group = repository.findByIdAndIsSuspendedIs(groupId, false).orElseThrow(() -> new ItemNotFoundException("Group"));

        if (postContent.isBlank()) {
            throw new FieldBlankException("Post content");
        }

        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();

        if (!isUserInGroup(group, user)) {
            LOGGER.warn("User {} is not a member of the group with id {}", user.getUsername(), groupId);
            throw new UserNotInGroupException();
        }

        var post = Post.builder()
                .content(postContent)
                .creationTime(LocalDateTime.now())
                .user(user)
                .build();

        group.getPosts().add(post);

        repository.save(group);

        LOGGER.info("User {} created post in group with id {}", user.getUsername(), group.getId());
        return "Post created successfully";
    }

    private static boolean isUserInGroup(Group group, User user) {
        return group.getUsers()
                .stream()
                .anyMatch(groupUser -> groupUser.getUser().getUsername().equals(user.getUsername())
                        && Boolean.TRUE.equals(groupUser.getApproved()));
    }


    public String createRequest(Long groupId, Principal authUser) {

        var group = repository.findByIdAndIsSuspendedIs(groupId, false).orElseThrow(() -> new ItemNotFoundException("Group"));
        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();

        var existingGroupRequest = group.getUsers()
                .stream()
                .filter(groupReq -> groupReq.getUser().getUsername().equals(user.getUsername()))
                .findFirst()
                .orElse(null);

        if (existingGroupRequest != null) {
            if (existingGroupRequest.getApproved() == null)
                throw new RequestAlreadyExistsException("pending");
            if (existingGroupRequest.getApproved())
                throw new RequestAlreadyExistsException("accepted");
            else
                throw new RequestAlreadyExistsException("rejected");
        }

        var groupRequest = GroupRequest.builder()
                .group(group)
                .createdAt(LocalDateTime.now())
                .user(user)
                .group(group)
                .build();

        group.getUsers().add(groupRequest);

        repository.save(group);

        LOGGER.info("User {} created request to join group with id {}", user.getUsername(), group.getId());
        return "Request sent";
    }

    public List<PostDataDTO> getPosts(Long groupId, Principal authUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();
        var group = repository.findByIdAndIsSuspendedIs(groupId, false).orElseThrow(() -> new ItemNotFoundException("Group"));

        if (!isUserInGroup(group, user)) {
            LOGGER.warn("User {} is not a member of the group with id {}", user.getUsername(), groupId);
            throw new UserNotInGroupException();
        }

        LOGGER.info("User {} requested posts from group with id {}", user.getUsername(), groupId);
        return DataMapper.getPostDTOs(group.getPosts());
    }


    public List<GroupRequestDataDTO> getGroupRequests(Long groupId, Principal authUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();
        var group = repository.findByIdAndIsSuspendedIs(groupId, false).orElseThrow(() -> new ItemNotFoundException("Group"));

        if (!isUserAdministrator(user, group)) {
            LOGGER.warn("User {} is not an administrator of the group with id {}", user.getUsername(), groupId);
            throw new UserNotGroupAdministratorException();
        }

        var requests = group.getUsers()
                .stream()
                .filter(groupRequest -> groupRequest.getApproved() == null)
                .toList();

        LOGGER.info("Administrator {} requested group requests for group with id {}", user.getUsername(), groupId);
        return DataMapper.getGroupRequestDTOs(requests);
    }

    public String processGroupRequest(Long groupId, Long requestId, Boolean approved, Principal authUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();
        var group = repository.findByIdAndIsSuspendedIs(groupId, false).orElseThrow(() -> new ItemNotFoundException("Group"));

        if (!isUserAdministrator(user, group)) {
            LOGGER.warn("User {} is not an administrator of the group with id {}", user.getUsername(), groupId);
            throw new UserNotGroupAdministratorException();
        }

        var request = group.getUsers()
                .stream()
                .filter(groupRequest -> Objects.equals(groupRequest.getId(), requestId))
                .findFirst().orElseThrow(() -> new ItemNotFoundException("Group request"));

        if (request.getApproved() != null) {
            throw new GroupRequestAlreadyProcessedException();
        }

        request.setApproved(approved);
        repository.save(group);

        if (approved) {
            LOGGER.info("Administrator {} approved request with id {} for group with id {}", user.getUsername(), requestId, group.getId());
            return "Request approved";
        }
        LOGGER.info("Administrator {} rejected request with id {} for group with id {}", user.getUsername(), requestId, group.getId());
        return "Request rejected";
    }

    private static boolean isUserAdministrator(User user, Group group) {
        return group.getGroupAdmins()
                .stream()
                .anyMatch(groupAdmin -> groupAdmin.getUser().getUsername().equals(user.getUsername()));
    }

    public List<GroupsDataDTO> getAllGroups(Principal authUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();

        return repository.findAll()
                .stream()
                .filter(group -> !group.getIsSuspended())
                .map(group -> new GroupsDataDTO(
                        group.getId(),
                        group.getName(),
                        group.getDescription(),
                        checkIfUserIsMember(group, user)
                )
        ).toList();
    }

    private boolean checkIfUserIsMember(Group group, User user) {
        return group.getUsers().stream().anyMatch(groupUser -> groupUser.getUser().getUsername().equals(user.getUsername()));
    }
}
