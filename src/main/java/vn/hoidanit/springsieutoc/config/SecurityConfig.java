package vn.hoidanit.springsieutoc.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration // giup ghi de Bean
public class SecurityConfig {

    @Value("${hoidanit.jwt.base64-secret}")
    private String jwtkey;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }


    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtkey).decode();

        return new SecretKeySpec(keyBytes, 0, keyBytes.length,
                JwtConfig.JWT_ALGORITHM.getName());
    }

}
