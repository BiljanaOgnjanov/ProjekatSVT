package swt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import swt.dto.GroupDTO;
import swt.dto.RequestProcessDTO;
import swt.dto.PostDTO;
import swt.dto.SuspendGroupDTO;
import swt.service.GroupService;
import swt.util.ApiDataResponse;
import swt.util.ApiResponse;

import java.security.Principal;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupController {

    private final GroupService service;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createGroup(
            @RequestBody GroupDTO groupDTO,
            Principal authUser
    ){

        return new ResponseEntity<>(
                new ApiResponse(
                        true,
                        service.createGroup(groupDTO, authUser),
                        LocalDateTime.now()
                ),
                OK
        );
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<ApiDataResponse> getGroupInfo(
            @PathVariable Long groupId,
            Principal authUser
    ) {

        return new ResponseEntity<>(
                new ApiDataResponse(
                    true,
                    "Successfully retrieved data",
                    LocalDateTime.now(),
                    service.getGroupInfo(groupId, authUser)
                ),
                OK
        );
    }

    @PatchMapping("/{groupId}/suspend")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> suspendGroup(
            @PathVariable Long groupId,
            Principal authUser,
            @RequestBody SuspendGroupDTO suspendGroupDTO
    ) {

        return new ResponseEntity<>(
                new ApiResponse(
                        true,
                        service.suspendGroup(groupId, suspendGroupDTO.getSuspendedReason(), authUser),
                        LocalDateTime.now()
                ),
                OK
        );
    }

    @PostMapping("/{groupId}/create-post")
    public ResponseEntity<ApiResponse> createPost(
            @PathVariable Long groupId,
            @RequestBody PostDTO postDTO,
            Principal authUser
    ) {

        return new ResponseEntity<>(
                new ApiResponse(
                        true,
                        service.createPost(groupId, postDTO.getContent(), authUser),
                        LocalDateTime.now()
                ),
                OK
        );
    }

    @PostMapping("/{groupId}/create-request")
    public ResponseEntity<ApiResponse> createRequest(
            @PathVariable Long groupId,
            Principal authUser
    ) {

        return new ResponseEntity<>(
                new ApiResponse(
                        true,
                        service.createRequest(groupId, authUser),
                        LocalDateTime.now()
                ),
                OK
        );
    }

    @GetMapping("/{groupId}/posts")
    public ResponseEntity<ApiDataResponse> getPosts(
            @PathVariable Long groupId,
            Principal authUser
    ) {

        return new ResponseEntity<>(
                new ApiDataResponse(
                        true,
                        "Successfully retrieved data",
                        LocalDateTime.now(),
                        service.getPosts(groupId, authUser)
                ),
                OK
        );
    }

    @GetMapping("/{groupId}/requests")
    public ResponseEntity<ApiDataResponse> getGroupRequests(
            @PathVariable Long groupId,
            Principal authUser
    ) {

        return new ResponseEntity<>(
                new ApiDataResponse(
                        true,
                        "Successfully retrieved data",
                        LocalDateTime.now(),
                        service.getGroupRequests(groupId, authUser)
                ),
                OK
        );
    }

    @PatchMapping("/{groupId}/requests/{requestId}")
    public ResponseEntity<ApiResponse> processGroupRequest(
            @PathVariable Long groupId,
            @PathVariable Long requestId,
            @RequestBody RequestProcessDTO dto,
            Principal authUser
    ) {

        return new ResponseEntity<>(
                new ApiResponse(
                        true,
                        service.processGroupRequest(groupId, requestId, dto.getApproved(), authUser),
                        LocalDateTime.now()
                ),
                OK
        );
    }

    @GetMapping()
    public ResponseEntity<ApiDataResponse> getAllGroups(
            Principal authUser
    ) {

        return new ResponseEntity<>(
                new ApiDataResponse(
                        true,
                        "Successfully retrieved data",
                        LocalDateTime.now(),
                        service.getAllGroups(authUser)
                )
                ,
                OK
        );
    }
}
