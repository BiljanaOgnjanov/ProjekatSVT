package swt.util;

import swt.dto.*;
import swt.model.GroupRequest;
import swt.model.Post;

import java.util.Comparator;
import java.util.List;

public class DataMapper {

    public static List<PostDataDTO> getPostDTOs(List<Post> posts) {
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

    public static List<GroupRequestDataDTO> getGroupRequestDTOs(List<GroupRequest> requests) {
        return requests.stream()
                .sorted(Comparator.comparing(GroupRequest::getCreatedAt).reversed())
                .map(groupRequest -> new GroupRequestDataDTO(
                        groupRequest.getId(),
                        groupRequest.getCreatedAt(),
                        new UserDataDTO(
                                groupRequest.getUser().getUsername(),
                                groupRequest.getUser().getDisplayName()
                        )
                )).toList();
    }
}
