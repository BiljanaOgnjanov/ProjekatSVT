package swt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import swt.enums.ReactionType;
import swt.exception.ItemNotFoundException;
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

        repository.save(comment);

        return "Successfully commented";
    }

    public String addReactionToComment(Long commentId, ReactionType type, Principal authUser) {

        var comment = repository.findById(commentId).orElseThrow(() -> new ItemNotFoundException("Comment"));
        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();

        comment.getReactions().stream()
                .filter(reaction -> reaction.getUser().getUsername().equals(user.getUsername()))
                .findFirst()
                .ifPresentOrElse(
                        reaction -> {
                            reaction.setType(type);
                            reaction.setTimestamp(LocalDate.now());
                            reactionRepository.save(reaction);
                        },
                        () -> {
                            var newReaction = Reaction.builder()
                                    .type(type)
                                    .timestamp(LocalDate.now())
                                    .comment(comment)
                                    .user(user)
                                    .build();
                            reactionRepository.save(newReaction);
                        }
                );

        return "Successfully reacted to comment";
    }
}
