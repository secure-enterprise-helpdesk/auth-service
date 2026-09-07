package enterprise.auth_service.service;

import enterprise.auth_service.entity.RefreshToken;
import enterprise.auth_service.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public String generateRefreshToken(String userId, String oldRefreshToken) {

        if (oldRefreshToken == null || oldRefreshToken.isBlank()) {
            String refreshToken = generateRefreshToken();
            String hashedToken = hashToken(refreshToken);

            RefreshToken refreshTokenEntity = RefreshToken.builder()
                    .userId(userId)
                    .tokenHash(hashedToken)
                    .expiresAt(LocalDateTime.now().plusDays(7))
                    .createdAt(LocalDateTime.now())
                    .revoked(false)
                    .build();

            refreshTokenRepository.save(refreshTokenEntity);

            return refreshToken;
        } else {
            String hashedOldToken = hashToken(oldRefreshToken);
            RefreshToken existingToken = refreshTokenRepository.findByTokenHash(hashedOldToken)
                    .orElseThrow(() -> new RuntimeException("Invalid or expired refresh token"));

            if (!existingToken.getRevoked() && existingToken.getExpiresAt().isAfter(LocalDateTime.now())) {
                existingToken.setRevoked(true);
                existingToken.setRevokedAt(LocalDateTime.now());
                refreshTokenRepository.save(existingToken);

                String newRefreshToken = generateRefreshToken();
                String hashedNewToken = hashToken(newRefreshToken);

                RefreshToken newRefreshTokenEntity = RefreshToken.builder()
                        .userId(userId)
                        .tokenHash(hashedNewToken)
                        .expiresAt(LocalDateTime.now().plusDays(7))
                        .createdAt(LocalDateTime.now())
                        .revoked(false)
                        .build();

                refreshTokenRepository.save(newRefreshTokenEntity);

                return newRefreshToken;
            } else {
                throw new RuntimeException("Invalid or expired refresh token");
            }
        }
    }

    public RefreshToken getValidRefreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new RuntimeException("Refresh token is required");
        }

        String hashedToken = hashToken(refreshToken);
        RefreshToken tokenEntity = refreshTokenRepository.findByTokenHash(hashedToken)
                .orElseThrow(() -> new RuntimeException("Invalid or expired refresh token"));

        if (tokenEntity.getRevoked() || !tokenEntity.getExpiresAt().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        return tokenEntity;
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            getValidRefreshToken(refreshToken);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    private String hashToken(String token){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateRefreshToken() {
        byte[] bytes = new byte[64];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
