package enterprise.auth_service.service;

import enterprise.auth_service.dto.CreateUserProfileRequest;
import enterprise.auth_service.dto.ValidateUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "user-service",
        url = "${services.user-service.url}"
)
public interface UserServiceClient {

    @PostMapping("/user-profile/register")
    ResponseEntity<String> createUser(@RequestBody CreateUserProfileRequest request);

    @PostMapping("/user-profile/login")
    public ResponseEntity<ValidateUser> login(@RequestParam String userId);
}
