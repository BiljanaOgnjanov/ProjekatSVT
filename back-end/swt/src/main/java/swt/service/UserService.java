package swt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import swt.config.JwtService;
import swt.dto.ChangePasswordDTO;
import swt.exception.WrongPasswordException;
import swt.model.User;
import swt.repository.UserRepository;
import swt.util.ApiResponse;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public String changePassword(ChangePasswordDTO changePasswordDTO, Principal authUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();

        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new WrongPasswordException();
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));

        repository.save(user);

        return "Password changed successfully";
    }
}
