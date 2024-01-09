package swt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import swt.dto.*;
import swt.exception.WeakPasswordException;
import swt.exception.WrongPasswordException;
import swt.model.User;
import swt.repository.UserRepository;
import swt.util.PasswordValidator;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserProfileDTO getUserData(Principal authUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();

        return UserProfileDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .description(user.getDescription())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .displayName(user.getDisplayName())
                .build();
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

        return "Password changed successfully";
    }

    public String changeDisplayName(DisplayNameDTO displayName, Principal authUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();

        user.setDisplayName(displayName.getDisplayName());
        repository.save(user);

        return "Display name updated successfully";
    }

    public String changeDescription(DescriptionDTO descriptionDTO, Principal authUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();

        user.setDescription(descriptionDTO.getDescription());
        repository.save(user);

        return "Description updated successfully";
    }
}
