package swt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swt.model.User;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDataDTO {
    private Long id;
    private UserDataDTO user;
    private String text;
    private LocalDate timestamp;
    private List<ReactionDataDTO> reactions;
}
