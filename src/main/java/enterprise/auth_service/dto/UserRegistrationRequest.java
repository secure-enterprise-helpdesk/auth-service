package enterprise.auth_service.dto;

import enterprise.auth_service.enums.Role;
import enterprise.auth_service.enums.Status;
import lombok.Data;

@Data
public class UserRegistrationRequest {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Integer departmentId;
    private Status status;
    private String password;
}
