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
public class GroupInfoDataDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime creationDate;
    private Integer userCount;
    private Integer postCount;
    private Boolean isUserInGroup;
}
