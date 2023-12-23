package swt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swt.dto.CommentDTO;
import swt.enums.ReactionType;
import swt.exception.ItemNotFoundException;
import swt.model.Comment;
import swt.model.Reaction;
import swt.repository.CommentRepository;
import swt.repository.PostRepository;
import swt.repository.ReactionRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository repository;
    private final PostRepository postRepository;
    private final ReactionRepository reactionRepository;

    public String addCommentToPost(Long postId, String commentText) {

        var post = postRepository.findById(postId).orElseThrow(() -> new ItemNotFoundException("Post"));

        var comment = Comment.builder()
                .text(commentText)
                .timestamp(LocalDate.now())
                .isDeleted(false)
                .post(post)
                .build();

        repository.save(comment);

        return "Successfully commented";
    }

    public String addReactionToComment(Long commentId, ReactionType type) {
        Comment comment = repository.findById(commentId).orElseThrow(() -> new ItemNotFoundException("Comment"));

        var reaction = Reaction.builder()
                .type(type)
                .timestamp(LocalDate.now())
                .comment(comment)
                .build();

        comment.getReactions().add(reaction);
        repository.save(comment);

        return "Successfully reacted to comment";
    }

    public List<Reaction> getReactionsForComment(Long commentId) {
        Comment comment = repository.findById(commentId).orElseThrow(() -> new ItemNotFoundException("Comment"));

        return comment.getReactions();
    }
}
