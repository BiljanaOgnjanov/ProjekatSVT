package swt.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupDataDTO {
    private Long id;
    private String name;
    private String description;
}
