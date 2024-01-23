package swt.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import swt.dto.*;
import swt.exception.InvalidFriendRequest;
import swt.exception.ItemNotFoundException;
import swt.exception.WeakPasswordException;
import swt.exception.WrongPasswordException;
import swt.model.FriendRequest;
import swt.model.User;
import swt.repository.UserRepository;
import swt.util.PasswordValidator;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    public UserProfileDTO getUserData(Principal authUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();
        var fullUser = repository.findById(user.getId()).orElseThrow(() -> new ItemNotFoundException("User"));
//        fullUser fetches group data (lazy initialization)

        return UserProfileDTO.builder()
                .username(fullUser.getUsername())
                .email(fullUser.getEmail())
                .description(fullUser.getDescription())
                .firstName(fullUser.getFirstName())
                .lastName(fullUser.getLastName())
                .displayName(fullUser.getDisplayName())
                .groups(fullUser.getGroups()
                        .stream()
                        .filter(groupRequest -> Objects.nonNull(groupRequest)
                                && Objects.nonNull(groupRequest.getApproved())
                                && groupRequest.getApproved()
                                && !groupRequest.getGroup().getIsSuspended())
                        .map(groupRequest -> new GroupDataDTO(
                                groupRequest.getGroup().getId(),
                                groupRequest.getGroup().getName(),
                                groupRequest.getGroup().getDescription()
                        )).toList()
                ).build();
    }

    public String changePassword(ChangePasswordDTO changePasswordDTO, Principal authUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();

        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new WrongPasswordException();
        }
        if (!PasswordValidator.isPasswordValid(changePasswordDTO.getNewPassword())) {
            throw new WeakPasswordException();
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        repository.save(user);

        LOGGER.info("User {} changed password", user.getUsername());
        return "Password changed successfully";
    }

    public String changeDisplayName(DisplayNameDTO displayName, Principal authUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();

        user.setDisplayName(displayName.getDisplayName());
        repository.save(user);

        LOGGER.info("User {} updated display name", user.getUsername());
        return "Display name updated successfully";
    }

    public String changeDescription(DescriptionDTO descriptionDTO, Principal authUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();

        user.setDescription(descriptionDTO.getDescription());
        repository.save(user);

        LOGGER.info("User {} updated description", user.getUsername());
        return "Description updated successfully";
    }

    public List<UserDataDTO> search(Optional<String> firstName, Optional<String> lastName) {

        return repository.findAll().stream()
                .filter(user -> (firstName.isEmpty() || user.getFirstName().toLowerCase().contains(firstName.get().toLowerCase()))
                                && (lastName.isEmpty() || user.getLastName().toLowerCase().contains(lastName.get().toLowerCase()))
                                && (firstName.isPresent() || lastName.isPresent()))
                .map(user -> new UserDataDTO(
                        user.getUsername(),
                        user.getDisplayName()
                ))
                .toList();
    }

    public String sendFriendRequest(Long userId, Principal authUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();
        var fullUser = repository.findById(user.getId()).orElseThrow(() -> new ItemNotFoundException("User"));

        var receiveUser = repository.findById(userId).orElseThrow(() -> new ItemNotFoundException("User"));
        if (fullUser.getId().equals(receiveUser.getId())) {
            throw new InvalidFriendRequest();
        }
        var request = FriendRequest.builder()
                .createdAt(LocalDateTime.now())
                .sender(fullUser)
                .receiver(receiveUser)
                .build();
        receiveUser.getReceivedFriendRequests().add(request);
        fullUser.getSentFriendRequests().add(request);

        repository.save(receiveUser);
        repository.save(fullUser);

        LOGGER.info("User {} sent friend request to user with id {}", user.getUsername(), receiveUser.getId());
        return "Friend request sent";
    }

    public String processFriendRequest(Long requestId, Boolean approved, Principal authUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();
        var fullUser = repository.findById(user.getId()).orElseThrow(() -> new ItemNotFoundException("User"));

        var friendRequest = fullUser.getReceivedFriendRequests()
                .stream()
                .filter(friendReq -> friendReq.getId().equals(requestId))
                .findFirst().orElseThrow(() -> new ItemNotFoundException("Friend request"));
        if (!friendRequest.getReceiver().getUsername().equals(fullUser.getUsername())) {
            throw new InvalidFriendRequest();
        }

        friendRequest.setApproved(approved);
        friendRequest.setAt(LocalDateTime.now());
        repository.save(fullUser);

        if (approved) {
            LOGGER.info("User {} approved friend request with id {}", user.getUsername(), requestId);
            return "Request approved";
        }
        LOGGER.info("User {} rejected friend request with id {}", user.getUsername(), requestId);
        return "Request rejected";
    }
}
