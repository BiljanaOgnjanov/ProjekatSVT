package swt.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupRequestDataDTO {
    private Long id;
    private LocalDateTime createdAt;
    private UserDataDTO user;
}
