package swt.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String displayName;
    private String description;
}
