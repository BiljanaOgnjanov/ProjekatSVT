package swt.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class GroupRequest {
    @Id
    @GeneratedValue
    private Long id;
    private Boolean approved;
    private LocalDateTime at;
    private LocalDateTime createdAt;

    @ManyToOne
    private User user;

    @ManyToOne
    private Group group;
}
