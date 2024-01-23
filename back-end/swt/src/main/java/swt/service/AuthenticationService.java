package swt.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import swt.config.JwtService;
import swt.dto.LoginDTO;
import swt.dto.RegisterDTO;
import swt.exception.EmailAlreadyExistsException;
import swt.exception.InvalidCredentialsException;
import swt.exception.UsernameAlreadyExistsException;
import swt.enums.Role;
import swt.exception.WeakPasswordException;
import swt.model.User;
import swt.repository.UserRepository;
import swt.util.PasswordValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private static final Logger LOGGER = LogManager.getLogger(AuthenticationService.class);

    public String register(RegisterDTO registerDTO) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {

        if (repository.findByUsername(registerDTO.getUsername()).isPresent()) {
           throw new UsernameAlreadyExistsException(registerDTO.getUsername());
        }
        if (repository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(registerDTO.getEmail());
        }
        if (!PasswordValidator.isPasswordValid(registerDTO.getPassword())) {
            throw new WeakPasswordException();
        }

        var user = User.builder()
                .firstName(registerDTO.getFirstName())
                .lastName(registerDTO.getLastName())
                .lastLogin(LocalDateTime.now())
                .email(registerDTO.getEmail())
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .role(Role.USER)
                .adminGroups(new ArrayList<>())
                .groups(new ArrayList<>())
                .receivedFriendRequests(new ArrayList<>())
                .sentFriendRequests(new ArrayList<>())
                .build();
        repository.save(user);

        LOGGER.info("{} registered", user.getUsername());
        return jwtService.generateToken(user);
    }

    public String login(LoginDTO loginDTO) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername(), loginDTO.getPassword()
            ));
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException();
        }

        var user = repository.findByUsername(loginDTO.getUsername())
                .orElseThrow();

        user.setLastLogin(LocalDateTime.now());
        repository.save(user);

        LOGGER.info("{} logged in", user.getUsername());
        return jwtService.generateToken(user);
    }
}
