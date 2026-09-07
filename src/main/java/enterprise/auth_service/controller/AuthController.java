package enterprise.auth_service.controller;

import enterprise.auth_service.dto.UserRegistrationRequest;
import enterprise.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> userRegister(@RequestBody UserRegistrationRequest request) {

        return authService.userRegister(request);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> userLogin(@RequestParam String userId, @RequestParam String password) {
        return authService.userLogin(userId, password);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshAccessToken(@RequestParam String refreshToken) {
        return authService.refreshAccessToken(refreshToken);
    }
}
