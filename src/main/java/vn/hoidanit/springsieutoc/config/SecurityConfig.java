package vn.hoidanit.springsieutoc.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import vn.hoidanit.springsieutoc.helper.exception.CustomAccessDeniedHandler;
import vn.hoidanit.springsieutoc.helper.exception.CustomAuthenticationEntryPoint;
import vn.hoidanit.springsieutoc.service.UserService;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration // giup ghi de Bean
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${hoidanit.jwt.base64-secret}")
    private String jwtkey;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(UserService userServiCe) {
        return new CustomUserDetailService(userServiCe);
    }

    @Bean
    AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                    PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider(userDetailsService);
        daoProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(daoProvider);
    }

    @Bean
    JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(getSecretKey())
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();
        scopeConverter.setAuthoritiesClaimName("scope");
        scopeConverter.setAuthorityPrefix(""); // giữ nguyên, không thêm "SCOPE_"

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(scopeConverter);
        return converter;
    }


    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtkey).decode();

        return new SecretKeySpec(keyBytes, 0, keyBytes.length,
                JwtService.JWT_ALGORITHM.getName());
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            CustomAccessDeniedHandler customAccessDeniedHandler,
                                            CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception{

        http.authorizeHttpRequests((requests) ->
                requests.requestMatchers("/auth/login", "/auth/refresh").permitAll()
                .requestMatchers("/users/**").hasRole("ADMIN")
                .anyRequest().authenticated());

        http.csrf((c -> c.disable()));

        http.formLogin(form -> form.disable());

        http.oauth2ResourceServer(oauth2 -> oauth2
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

}
