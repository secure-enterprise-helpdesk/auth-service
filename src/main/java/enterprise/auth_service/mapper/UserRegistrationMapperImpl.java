package enterprise.auth_service.mapper;

import org.springframework.stereotype.Component;
import enterprise.auth_service.dto.CreateUserProfileRequest;
import enterprise.auth_service.dto.UserRegistrationRequest;
import enterprise.auth_service.entity.UserCredential;

@Component
public class UserRegistrationMapperImpl implements UserRegistrationMapper {
    @Override
    public CreateUserProfileRequest toCreateUserProfileRequest(UserRegistrationRequest request) {
        if (request == null) return null;
        CreateUserProfileRequest r = new CreateUserProfileRequest();
        r.setId(null);
        r.setUserId(request.getUserId());
        r.setFirstName(request.getFirstName());
        r.setLastName(request.getLastName());
        r.setDepartmentId(request.getDepartmentId());
        return r;
    }

    @Override
    public UserCredential toUserCredential(UserRegistrationRequest request) {
        if (request == null) return null;
        UserCredential c = new UserCredential();
        c.setUserId(request.getUserId());
        c.setEmail(request.getEmail());
        // passwordHash intentionally left null here; caller should set hashed password
        c.setPasswordHash(null);
        c.setEnabled(Boolean.TRUE);
        c.setAccountLocked(Boolean.FALSE);
        return c;
    }
}
