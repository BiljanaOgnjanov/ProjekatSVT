package swt.model;

import jakarta.persistence.*;
import lombok.*;
import swt.enums.ReactionType;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reaction {
    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private ReactionType type;
    private LocalDate timestamp;
    @ManyToOne
    private Post post;
    @ManyToOne
    private Comment comment;
    @ManyToOne
    private User user;
}
