package swt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swt.enums.ReactionType;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReactionDataDTO {
    private Long id;
    private UserDataDTO user;
    private ReactionType type;
    private LocalDate timestamp;
}
