package swt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserDataDTO {
    private Long id;
    private String username;
    private String displayName;
    private String firstName;
    private String lastName;
    private Boolean isFriend;
}
