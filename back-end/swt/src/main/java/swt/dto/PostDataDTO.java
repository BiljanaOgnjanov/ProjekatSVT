package swt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDataDTO {
    private Long id;
    private String content;
    private LocalDateTime creationTime;
    private UserDataDTO user;
    private List<CommentDataDTO> comments;
    private List<ReactionDataDTO> reactions;
}
