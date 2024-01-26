package swt.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupsDataDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean isMember;
}
