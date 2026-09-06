package enterprise.auth_service.dto;

import enterprise.auth_service.enums.Role;
import enterprise.auth_service.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateUser {
        private String userId;
        private Role role;
        private Status status;
}
