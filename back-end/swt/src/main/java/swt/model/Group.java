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
@Table(name = "_group")
public class Group {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private LocalDateTime creationDate;
    private Boolean isSuspended;
    private String suspendedReason;
    @OneToOne
    private User admin;
}
