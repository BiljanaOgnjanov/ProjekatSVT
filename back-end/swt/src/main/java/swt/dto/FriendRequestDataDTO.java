package swt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestDataDTO {
    private Long id;
    private LocalDateTime createdAt;
    private String username;
    private String displayName;
    private String firstName;
    private String lastName;
}
