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
import swt.util.ApiDataResponse;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<ApiDataResponse> register(@RequestBody RegisterDTO registerDTO) {

        return new ResponseEntity<>(
                new ApiDataResponse(
                        true,
                        "Successfully registered",
                        LocalDateTime.now(),
                        service.register(registerDTO)
                ),
                CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiDataResponse> login(@RequestBody LoginDTO loginDTO) {

        return new ResponseEntity<>(
                new ApiDataResponse(
                        true,
                        "Successfully logged in",
                        LocalDateTime.now(),
                        service.login(loginDTO)
                ),
                OK
        );
    }

}
