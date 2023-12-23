package swt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swt.enums.ReactionType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReactionDTO {
    private ReactionType reaction;
}
