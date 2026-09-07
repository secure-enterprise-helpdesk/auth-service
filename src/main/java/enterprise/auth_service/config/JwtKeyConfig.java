package enterprise.auth_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwtKeyConfig {

    @Value("classpath:keys/private_key.pem")
    private Resource privateKeyResource;

    @Value("classpath:keys/public_key.pem")
    private Resource publicKeyResource;


    @Bean
    public PrivateKey privateKey() throws Exception {
        String key = readPemKey(privateKeyResource, "-----BEGIN PRIVATE KEY-----", "-----END PRIVATE KEY-----");

        byte[] decoded = Base64.getDecoder().decode(key);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePrivate(keySpec);
    }

    @Bean
    public PublicKey publicKey() throws Exception {
        String key = readPemKey(publicKeyResource, "-----BEGIN PUBLIC KEY-----", "-----END PUBLIC KEY-----");

        byte[] decoded = Base64.getDecoder().decode(key);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePublic(keySpec);
    }

    private String readPemKey(Resource resource, String beginMarker, String endMarker) throws Exception {
        try (var inputStream = resource.getInputStream()) {
            String key = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return key
                    .replace(beginMarker, "")
                    .replace(endMarker, "")
                    .replaceAll("\\s+", "");
        }
    }
}
