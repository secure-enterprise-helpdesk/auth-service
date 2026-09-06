package enterprise.auth_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
@Entity
@Data
public class UserCredential {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String userId;
    private String email;
    private String phoneNumber;
    private String passwordHash;
    private Boolean enabled;
    private Boolean accountLocked;
    private LocalDateTime lockedUntil;
    private int attemptCount;
}
