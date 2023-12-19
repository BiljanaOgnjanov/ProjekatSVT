package swt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swt.dto.ChangePasswordDTO;
import swt.service.UserService;
import swt.util.ApiResponse;

import java.security.Principal;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    @PatchMapping("/password-change")
    public ResponseEntity<ApiResponse> changePassword(
            @RequestBody ChangePasswordDTO changePasswordDTO,
            Principal authUser) {
        return new ResponseEntity<>(
                new ApiResponse(
                        true,
                        service.changePassword(changePasswordDTO, authUser),
                        LocalDateTime.now()
                ),
                OK
        ) ;
    }
}
