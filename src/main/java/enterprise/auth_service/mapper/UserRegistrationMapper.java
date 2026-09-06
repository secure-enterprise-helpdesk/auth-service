package enterprise.auth_service.mapper;

import enterprise.auth_service.dto.CreateUserProfileRequest;
import enterprise.auth_service.dto.UserRegistrationRequest;
import enterprise.auth_service.entity.UserCredential;

public interface UserRegistrationMapper {
    CreateUserProfileRequest toCreateUserProfileRequest(UserRegistrationRequest request);
    UserCredential toUserCredential(UserRegistrationRequest request);
}

