package swt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import swt.dto.GroupDTO;
import swt.exception.FieldBlankException;
import swt.model.Group;
import swt.model.User;
import swt.repository.GroupRepository;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository repository;

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
                .admin(user)
                .build();

        repository.save(group);

        return "Group created successfully";
    }
}
