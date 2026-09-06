package enterprise.auth_service.mapper;


import enterprise.auth_service.dto.UserRegistrationRequest;
import enterprise.auth_service.entity.UserCredential;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserCredential toEntity(UserRegistrationRequest dto);
    UserRegistrationRequest toDto(UserCredential entity);
}
