package enterprise.auth_service.controller;

import enterprise.auth_service.dto.ServiceLoginRequest;
import enterprise.auth_service.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/internal/auth")
@RequiredArgsConstructor
public class InternalAuthController {

    @Value("${jwt.username}")
    private String username;

    @Value("${jwt.password}")
    private String password;

    private final JwtTokenService jwtTokenService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody ServiceLoginRequest request) {

        if (!username.equals(request.getUsername()) ||
                !password.equals(request.getPassword())) {
            return ResponseEntity.status(401).build();
        }

        String accessToken = jwtTokenService.generateServiceToken("ticket-service");

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", "");
        response.put("tokenType", "Bearer");
        response.put("expiresIn", 900);

        return ResponseEntity.ok(response);
    }
}