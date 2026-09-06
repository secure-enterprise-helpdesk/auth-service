package enterprise.auth_service.wrapper;

import enterprise.auth_service.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    private String userId;
    private String firstName;
    private String lastName;
    private Integer departmentId;
    private String status;
}
