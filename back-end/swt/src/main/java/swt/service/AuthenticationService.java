package swt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import swt.config.JwtService;
import swt.dto.LoginDTO;
import swt.dto.RegisterDTO;
import swt.exception.EmailAlreadyExistsException;
import swt.exception.UsernameAlreadyExistsException;
import swt.enums.Role;
import swt.model.User;
import swt.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterDTO registerDTO) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {

        if (repository.findByUsername(registerDTO.getUsername()).isPresent()) {
           throw new UsernameAlreadyExistsException(registerDTO.getUsername());
        }
        if (repository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(registerDTO.getEmail());
        }

        var user = User.builder()
                .firstName(registerDTO.getFirstName())
                .lastName(registerDTO.getLastName())
                .lastLogin(LocalDateTime.now())
                .email(registerDTO.getEmail())
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);

        return jwtService.generateToken(user);
    }

    public String login(LoginDTO loginDTO) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword()
        ));

        var user = repository.findByUsername(loginDTO.getUsername())
                .orElseThrow();

        user.setLastLogin(LocalDateTime.now());
        repository.save(user);

        return jwtService.generateToken(user);
    }
}
