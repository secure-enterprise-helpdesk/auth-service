package enterprise.auth_service.service;

import enterprise.auth_service.dto.CreateUserProfileRequest;
import enterprise.auth_service.dto.UserRegistrationRequest;
import enterprise.auth_service.dto.ValidateUser;
import enterprise.auth_service.entity.UserCredential;
import enterprise.auth_service.enums.Status;
import enterprise.auth_service.repository.UserCredentialRepository;
import enterprise.auth_service.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    SecurityConfig securityConfig;

    @Autowired
    UserServiceClient userServiceClient;
    @Autowired
    UserCredentialRepository userCredentialRepository;

    @Autowired
    enterprise.auth_service.mapper.UserRegistrationMapper userRegistrationMapper;

    public ResponseEntity<String> userRegister(UserRegistrationRequest request) {
        if (request == null) {
            return ResponseEntity.badRequest().body("Request cannot be null");
        }
        if (findByUserId(request.getUserId())) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        // map registration request -> user credential and create profile request using MapStruct
        UserCredential userCredential = userRegistrationMapper.toUserCredential(request);
        // encode password (MapStruct ignores passwordHash)
        userCredential.setPasswordHash(securityConfig.passwordEncoder().encode(request.getPassword()));

        CreateUserProfileRequest createUserProfileRequest = userRegistrationMapper.toCreateUserProfileRequest(request);

        userServiceClient.createUser(createUserProfileRequest);
        userCredentialRepository.save(userCredential);
        return ResponseEntity.ok("User registered successfully");
    }
    public boolean findByUserId(String userId) {
        UserCredential userCredential = userCredentialRepository.findByUserId(userId);
        return userCredential != null;
    }

    public ResponseEntity<String> userLogin(String userId, String password) {
        UserCredential userCredential = userCredentialRepository.findByUserId(userId);
        ValidateUser validateUser = userServiceClient.login(userId).getBody();
        if (userCredential == null || validateUser == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        if (userCredential.getAccountLocked()) {
            if (LocalDateTime.now().isAfter(userCredential.getLockedUntil())) {
                userCredential.setAccountLocked(false);
                userCredential.setLockedUntil(null);
                userCredential.setAttemptCount(0);
                userCredentialRepository.save(userCredential);
            } else {
                return ResponseEntity.badRequest().body("Account is locked");
            }
        }
        if (!securityConfig.passwordEncoder().matches(password, userCredential.getPasswordHash())) {
            userCredential.setAttemptCount(userCredential.getAttemptCount() + 1);
            if (userCredential.getAttemptCount() >= 5) {
                userCredential.setAccountLocked(true);
                userCredential.setLockedUntil(LocalDateTime.now().plusHours(1));
            }
            userCredentialRepository.save(userCredential);
            return ResponseEntity.badRequest().body("Invalid password");
        }
        userCredential.setAttemptCount(0);
        userCredentialRepository.save(userCredential);
        if (!validateUser.getStatus().equals(Status.Active)) {
            return ResponseEntity.badRequest().body("User is not active");
        }
        return ResponseEntity.ok("Login successful");
    }
}
