package swt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swt.dto.CommentDTO;
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

    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponse> editComment(
            Principal authUser,
            @PathVariable Long commentId,
            @RequestBody CommentDTO commentDTO
            ) {

        return new ResponseEntity<>(
                new ApiResponse(
                        true,
                        service.editComment(commentId, commentDTO.getText(), authUser),
                        LocalDateTime.now()
                ),
                OK
        );
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(
            Principal authUser,
            @PathVariable Long commentId
    ) {

        return new ResponseEntity<>(
                new ApiResponse(
                        true,
                        service.deleteComment(commentId, authUser),
                        LocalDateTime.now()
                ),
                OK
        );
    }
}