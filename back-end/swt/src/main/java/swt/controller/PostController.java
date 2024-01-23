package swt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swt.dto.CommentDTO;
import swt.dto.PostDTO;
import swt.dto.ReactionDTO;
import swt.service.CommentService;
import swt.service.PostService;
import swt.util.ApiDataResponse;
import swt.util.ApiResponse;

import java.security.Principal;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService service;
    private final CommentService commentService;

    @GetMapping("/user")
    public ResponseEntity<ApiDataResponse> getUserPosts(Principal authUser) {

        return new ResponseEntity<>(
                new ApiDataResponse(
                        true,
                        "Successfully retrieved data",
                        LocalDateTime.now(),
                        service.getUserPosts(authUser)
                ),
                OK
        );
    }

    @GetMapping
    public ResponseEntity<ApiDataResponse> getPosts(Principal authUser) {

        return new ResponseEntity<>(
                new ApiDataResponse(
                        true,
                        "Successfully retrieved data",
                        LocalDateTime.now(),
                        service.getPosts(authUser)
                ),
                OK
        );
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createPost(
            @RequestBody PostDTO postDTO,
            Principal authUser
    ) {

       return new ResponseEntity<>(
               new ApiResponse(
                       true,
                       service.createPost(postDTO.getContent(), authUser),
                       LocalDateTime.now()
               ),
               OK
       );
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse> editPost(
            @RequestBody PostDTO postDTO,
            Principal authUser,
            @PathVariable Long postId) {

        return new ResponseEntity<>(
                new ApiResponse(
                        true,
                        service.editPost(postId, postDTO.getContent(), authUser),
                        LocalDateTime.now()
                ),
                OK
        );
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse> deletePost(
            Principal authUser,
            @PathVariable Long postId) {

        return new ResponseEntity<>(
                new ApiResponse(
                        true,
                        service.deletePost(postId, authUser),
                        LocalDateTime.now()
                ),
                OK
        );
    }

    @PutMapping("/{postId}/react")
    public ResponseEntity<ApiResponse> addReactionToPost(
            Principal authUser,
            @PathVariable Long postId,
            @RequestBody ReactionDTO reactionDTO
    ) {

        return new ResponseEntity<>(
                new ApiResponse(
                        true,
                        service.addReactionToPost(postId, reactionDTO.getReaction(), authUser),
                        LocalDateTime.now()
                ),
                OK
        );
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<ApiResponse> addCommentToPost(
            Principal authUser,
            @PathVariable Long postId,
            @RequestBody CommentDTO commentDTO
    ) {

        return new ResponseEntity<>(
                new ApiResponse(
                        true,
                        commentService.addCommentToPost(postId, commentDTO.getText(), authUser),
                        LocalDateTime.now()
                ),
                OK
        );
    }
}
