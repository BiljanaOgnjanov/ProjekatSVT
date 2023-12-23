package swt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swt.dto.ChangePasswordDTO;
import swt.dto.DescriptionDTO;
import swt.dto.DisplayNameDTO;
import swt.dto.GroupDTO;
import swt.service.GroupService;
import swt.service.UserService;
import swt.util.ApiDataResponse;
import swt.util.ApiResponse;

import java.security.Principal;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService service;
    private final GroupService groupService;

    @GetMapping
    public ResponseEntity<ApiDataResponse> getUserData(Principal authUser) {

        return new ResponseEntity<>(
                new ApiDataResponse(
                        true,
                        "Successfully retrieved data",
                        LocalDateTime.now(),
                        service.getUserData(authUser)
                ),
                OK
        );
    }

    @PatchMapping("/password-change")
    public ResponseEntity<ApiResponse> changePassword(
            @RequestBody ChangePasswordDTO changePasswordDTO,
            Principal authUser
    ) {
        return new ResponseEntity<>(
                new ApiResponse(
                        true,
                        service.changePassword(changePasswordDTO, authUser),
                        LocalDateTime.now()
                ),
                OK
        );
    }

    @PatchMapping("/display-name")
    public ResponseEntity<ApiResponse> changeDisplayName(
            @RequestBody DisplayNameDTO displayNameDTO,
            Principal authUser
    ) {

        return new ResponseEntity<>(
                new ApiResponse(
                        true,
                        service.changeDisplayName(displayNameDTO, authUser),
                        LocalDateTime.now()
                ),
                OK
        );
    }

    @PatchMapping("/description")
    public ResponseEntity<ApiResponse> changeDescription(
            @RequestBody DescriptionDTO descriptionDTO,
            Principal authUser
    ) {

        return new ResponseEntity<>(
                new ApiResponse(
                        true,
                        service.changeDescription(descriptionDTO, authUser),
                        LocalDateTime.now()
                ),
                OK
        );
    }

    @PostMapping("/group/create")
    public ResponseEntity<ApiResponse> createGroup(
            @RequestBody GroupDTO groupDTO,
            Principal authUser
    ){

        return new ResponseEntity<>(
                new ApiResponse(
                        true,
                        groupService.createGroup(groupDTO, authUser),
                        LocalDateTime.now()
                ),
                OK
        ) ;
    }
}
