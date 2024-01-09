package swt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import swt.dto.CommentDataDTO;
import swt.dto.PostDataDTO;
import swt.dto.ReactionDataDTO;
import swt.dto.UserDataDTO;
import swt.enums.ReactionType;
import swt.exception.ItemNotFoundException;
import swt.exception.FieldBlankException;
import swt.exception.UnauthorizedPostEditException;
import swt.model.Post;
import swt.model.Reaction;
import swt.model.User;
import swt.repository.PostRepository;
import swt.repository.ReactionRepository;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository repository;
    private final ReactionRepository reactionRepository;
    private final PostRepository postRepository;

    public String createPost(String postContent, Principal authUser) {

        if (postContent.isBlank()) {
            throw new FieldBlankException("Post content");
        }

        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();

        var post = Post.builder()
                .content(postContent)
                .creationTime(LocalDateTime.now())
                .user(user)
                .build();

        postRepository.save(post);

        return "Post created successfully";
    }

    private Post getPost(Long postId, UsernamePasswordAuthenticationToken authUser) {

        var user = (User) authUser.getPrincipal();

        var post = repository.findById(postId).orElseThrow(() -> new ItemNotFoundException("Post"));
        if (!post.getUser().getUsername().equals(user.getUsername())) {
            throw new UnauthorizedPostEditException();
        }
        return post;
    }

    public String editPost(Long postId, String postContent, Principal authUser) {

        if (postContent.isBlank()) {
            throw new FieldBlankException("Post content");
        }

        var post = getPost(postId, (UsernamePasswordAuthenticationToken) authUser);
        post.setContent(postContent);
        repository.save(post);

        return "Post edited successfully";
    }

    public String deletePost(Long postId, Principal authUser) {

        var post = getPost(postId, (UsernamePasswordAuthenticationToken) authUser);
        repository.delete(post);

        return "Post deleted successfully";
    }

    public String addReactionToPost(Long postId, ReactionType type, Principal authUser) {

        var post = repository.findById(postId).orElseThrow(() -> new ItemNotFoundException("Post"));
        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();

        post.getReactions().stream()
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
                                    .post(post)
                                    .user(user)
                                    .build();
                            reactionRepository.save(newReaction);
                        }
                );

        return "Successfully reacted to post";
    }

    private List<PostDataDTO> getPostDTOs(List<Post> posts) {
        return posts.stream()
                .sorted(Comparator.comparing(Post::getCreationTime).reversed())
                .map(post -> new PostDataDTO(
                        post.getId(),
                        post.getContent(),
                        post.getCreationTime(),
                        new UserDataDTO(post.getUser().getUsername(), post.getUser().getDisplayName()),
                        post.getComments().stream()
                                .map(comment -> new CommentDataDTO(
                                        comment.getId(),
                                        new UserDataDTO(
                                                comment.getUser().getUsername(),
                                                comment.getUser().getDisplayName()
                                        ),
                                        comment.getText(),
                                        comment.getTimestamp(),
                                        comment.getReactions().stream()
                                                .map(reaction -> new ReactionDataDTO(
                                                        reaction.getId(),
                                                        new UserDataDTO(
                                                                reaction.getUser().getUsername(),
                                                                reaction.getUser().getDisplayName()
                                                        ),
                                                        reaction.getType(),
                                                        reaction.getTimestamp()
                                                )).toList()
                                )).toList(),
                        post.getReactions().stream()
                                .map(reaction -> new ReactionDataDTO(
                                        reaction.getId(),
                                        new UserDataDTO(
                                                reaction.getUser().getUsername(),
                                                reaction.getUser().getDisplayName()
                                        ),
                                        reaction.getType(),
                                        reaction.getTimestamp()
                                )).toList()
                )).toList();
    }

    public List<PostDataDTO> getUserPosts(Principal authUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();

        return getPostDTOs(repository.findAllByUser(user));
    }

    public Object getPosts() {

        return getPostDTOs(repository.findAll());
    }
}
