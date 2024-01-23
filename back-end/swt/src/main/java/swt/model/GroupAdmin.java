package swt.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class GroupAdmin {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Group group;

    public GroupAdmin(User user, Group group) {
        this.user = user;
        this.group = group;
    }
}
