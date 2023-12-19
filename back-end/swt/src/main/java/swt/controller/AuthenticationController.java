package swt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swt.dto.LoginDTO;
import swt.dto.RegisterDTO;
import swt.service.AuthenticationService;
import swt.util.ApiResponse;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterDTO registerDTO) {

        return new ResponseEntity<>(
                new ApiResponse(
                        true,
                        service.register(registerDTO),
                        LocalDateTime.now()
                ),
                CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginDTO loginDTO) {

        return new ResponseEntity<>(
                new ApiResponse(
                        true,
                        service.login(loginDTO),
                        LocalDateTime.now()
                ),
                OK
        );
    }

}
