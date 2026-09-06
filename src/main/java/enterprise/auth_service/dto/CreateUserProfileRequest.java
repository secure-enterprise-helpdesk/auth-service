package enterprise.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserProfileRequest {
    private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    private Integer departmentId;
}
