package enterprise.auth_service.repository;

import enterprise.auth_service.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {

    UserCredential findByUserId(String username);
}
