package swt.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue
    private Long id;
    private String text;
    private LocalDate timestamp;
    private Boolean isDeleted;
    @ManyToOne
    private Post post;
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<Reaction> reactions;
    @ManyToOne
    private User user;
}
