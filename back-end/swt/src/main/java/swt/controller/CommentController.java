package swt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swt.dto.ReactionDTO;
import swt.service.CommentService;
import swt.util.ApiResponse;

import java.security.Principal;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService service;

    @PutMapping("/{commentId}/react")
    public ResponseEntity<ApiResponse> addReactionToComment(
            Principal authUser,
            @PathVariable Long commentId,
            @RequestBody ReactionDTO reactionDTO
    ) {

        return new ResponseEntity<>(
                new ApiResponse(
                        true,
                        service.addReactionToComment(commentId, reactionDTO.getReaction(), authUser),
                        LocalDateTime.now()
                ),
                OK
        );
    }
}