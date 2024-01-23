package swt.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FriendRequest {
    @Id
    @GeneratedValue
    private Long id;
    private Boolean approved;
    private LocalDateTime at;
    private LocalDateTime createdAt;
    @ManyToOne
    private User sender;
    @ManyToOne
    private User receiver;
}
