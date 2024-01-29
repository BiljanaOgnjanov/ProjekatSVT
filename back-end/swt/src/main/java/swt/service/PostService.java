package swt.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import swt.dto.PostDataDTO;
import swt.enums.ReactionType;
import swt.exception.ItemNotFoundException;
import swt.exception.FieldBlankException;
import swt.exception.UnauthorizedEditException;
import swt.model.Post;
import swt.model.Reaction;
import swt.model.User;
import swt.repository.PostRepository;
import swt.repository.ReactionRepository;
import swt.util.DataMapper;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository repository;
    private final ReactionRepository reactionRepository;
    private final PostRepository postRepository;

    private static final Logger LOGGER = LogManager.getLogger(PostService.class);

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

        var postId = postRepository.save(post).getId();

        LOGGER.info("User {} created post with id {}", user.getUsername(), postId);
        return "Post created successfully";
    }

    private Post getPost(Long postId, UsernamePasswordAuthenticationToken authUser) {

        var user = (User) authUser.getPrincipal();

        var post = repository.findById(postId).orElseThrow(() -> new ItemNotFoundException("Post"));
        if (!post.getUser().getUsername().equals(user.getUsername())) {
            throw new UnauthorizedEditException("post");
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

        LOGGER.info("User {} edited post with id {}", post.getUser().getUsername(), postId);
        return "Post edited successfully";
    }

    public String deletePost(Long postId, Principal authUser) {

        var post = getPost(postId, (UsernamePasswordAuthenticationToken) authUser);
        repository.delete(post);

        LOGGER.info("User {} deleted post with id {}", post.getUser().getUsername(), postId);
        return "Post deleted successfully";
    }

    public String addReactionToPost(Long postId, ReactionType type, Principal authUser) {

        var post = repository.findById(postId).orElseThrow(() -> new ItemNotFoundException("Post"));
        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();

        Long[] reactionId = { null };

        post.getReactions().stream()
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
                                    .post(post)
                                    .user(user)
                                    .build();
                            reactionId[0] = reactionRepository.save(newReaction).getId();
                        }
                );

        LOGGER.info("User {} added reaction with id {} to post with id {}", user.getUsername(), reactionId[0], postId);
        return "Successfully reacted to post";
    }

    public List<PostDataDTO> getUserPosts(Principal authUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();

        LOGGER.info("User {} requested his posts", user.getUsername());
        return DataMapper.getPostDTOs(repository.findAllByUser(user));
    }

    public Object getPosts(Principal authUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) authUser).getPrincipal();

        LOGGER.info("User {} requested all posts", user.getUsername());
        return DataMapper.getPostDTOs(repository.findPosts());
    }
}
