package swt.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import swt.enums.ReactionType;
import swt.exception.FieldBlankException;
import swt.exception.ItemNotFoundException;
import swt.exception.UnauthorizedEditException;
import swt.model.Comment;
import swt.model.Reaction;
import swt.model.User;
import swt.repository.CommentRepository;
import swt.repository.PostRepository;
import swt.repository.ReactionRepository;

import java.security.Principal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository repository;
    private final PostRepository postRepository;
    private final ReactionRepository reactionRepository;

    private static final Logger LOGGER = LogManager.getLogger(CommentService.class);

    public String addCommentToPost(Long postId, String commentText, Principal authUser) {

        var post = postRepository.findById(postId).orElseThrow(() -> new ItemNotFoundException("Post"));
        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();

        var comment = Comment.builder()
                .text(commentText)
                .timestamp(LocalDate.now())
                .isDeleted(false)
                .post(post)
                .user(user)
                .build();

        var commentID = repository.save(comment).getId();

        LOGGER.info("User {} added comment with id {} to post with id {}", user.getUsername(), commentID, postId);
        return "Successfully commented";
    }

    public String addReactionToComment(Long commentId, ReactionType type, Principal authUser) {

        var comment = repository.findById(commentId).orElseThrow(() -> new ItemNotFoundException("Comment"));
        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();

        Long[] reactionId = { null };

        comment.getReactions().stream()
                .filter(reaction -> reaction.getUser().getUsername().equals(user.getUsername()))
                .findFirst()
                .ifPresentOrElse(
                        reaction -> {
                            reaction.setType(type);
                            reaction.setTimestamp(LocalDate.now());
                            reactionRepository.save(reaction);
                            reactionId[0] = reaction.getId();
                        },
                        () -> {
                            var newReaction = Reaction.builder()
                                    .type(type)
                                    .timestamp(LocalDate.now())
                                    .comment(comment)
                                    .user(user)
                                    .build();
                            reactionId[0] = reactionRepository.save(newReaction).getId();

                        }
                );

        LOGGER.info("User {} added reaction with id {} to comment with id {}", user.getUsername(), reactionId[0], commentId);
        return "Successfully reacted to comment";
    }

    public String editComment(Long commentId, String commentText, Principal authUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();

        if (commentText.isBlank()) {
            throw new FieldBlankException("Comment text");
        }
        var comment = repository.findById(commentId).orElseThrow(() -> new ItemNotFoundException("Comment"));
        if (!comment.getUser().getUsername().equals(user.getUsername())) {
            throw new UnauthorizedEditException("comment");
        }
        comment.setText(commentText);
        repository.save(comment);

        LOGGER.info("User {} edited comment with id {}", user.getUsername(), commentId);
        return "Comment edited successfully";
    }

    public String deleteComment(Long commentId, Principal authUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();

        var comment = repository.findById(commentId).orElseThrow(() -> new ItemNotFoundException("Comment"));
        if (!comment.getUser().getUsername().equals(user.getUsername())) {
            throw new UnauthorizedEditException("comment");
        }
        repository.delete(comment);

        LOGGER.info("User {} deleted comment with id {}", user, commentId);
        return "Comment deleted successfully";
    }
}
