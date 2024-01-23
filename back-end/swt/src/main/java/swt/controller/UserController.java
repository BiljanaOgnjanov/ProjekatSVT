package swt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swt.dto.*;
import swt.service.GroupService;
import swt.service.UserService;
import swt.util.ApiDataResponse;
import swt.util.ApiResponse;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService service;

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

    @GetMapping("/search")
    public ResponseEntity<ApiDataResponse> searchUsers(
            @RequestParam Optional<String> firstName,
            @RequestParam Optional<String> lastName
            ) {

        return new ResponseEntity<>(
                new ApiDataResponse(
                        true,
                        "Successfully retrieved data",
                        LocalDateTime.now(),
                        service.search(firstName, lastName)
                ),
                OK
        );
    }

    @PostMapping("friend-requests/send")
    public ResponseEntity<ApiResponse> sendFriendRequest(
            @RequestBody FriendRequestDTO friendRequestDTO,
            Principal authUser
    ) {

        return new ResponseEntity<>(
                new ApiResponse(
                        true,
                        service.sendFriendRequest(friendRequestDTO.getUserId(), authUser),
                        LocalDateTime.now()
                ),
                OK
        );
    }

    @PutMapping("friend-requests/{requestId}")
    public ResponseEntity<ApiResponse> processFriendRequest(
            @PathVariable Long requestId,
            @RequestBody RequestProcessDTO dto,
            Principal authUser
    ) {

        return new ResponseEntity<>(
                new ApiResponse(
                        true,
                        service.processFriendRequest(requestId, dto.getApproved(), authUser),
                        LocalDateTime.now()
                ),
                OK
        );
    }
}
