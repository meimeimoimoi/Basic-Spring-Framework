package vn.hoidanit.springsieutoc.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import vn.hoidanit.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.hoidanit.springsieutoc.model.DTO.ExchangeTokenResponse;
import vn.hoidanit.springsieutoc.model.DTO.LoginResponseDTO;
import vn.hoidanit.springsieutoc.model.RefreshToken;
import vn.hoidanit.springsieutoc.model.User;
import vn.hoidanit.springsieutoc.service.RefreshTokenService;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS256;

    private final JwtEncoder jwtEncoder;
    private final RefreshTokenService refreshTokenService;

    @Value("${hoidanit.jwt.access-token-validity-in-seconds}")
    private String accessTokenExpiration;

    @Value("${hoidanit.jwt.refesh-token-validity-in-seconds}")
    private String refreshTokenExpiration;

    public String getScope(Authentication authentication){
        if(authentication != null){
            String scope = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));
            return scope;
        }
        return "UNKNOWN";
    }

    public String generateSecureToken() {
        byte[] randomBytes = new byte[64]; // 512 bits
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    public String createRefreshToken(User user){
        Instant now = Instant.now();
        Instant validity = now.plus(Long.valueOf(refreshTokenExpiration),
                ChronoUnit.SECONDS);
        String token = generateSecureToken();

        RefreshToken rf = new RefreshToken();
        rf.setCreatedAt(now);
        rf.setExpiredAt(validity);
        rf.setToken(token);
        rf.setUser(user);

        this.refreshTokenService.createRefreshToken(rf);

        return token;
    }

    public String createAccessToken(Authentication authentication, int id){
        Instant now = Instant.now();
        Instant validity = now.plus(Long.valueOf(accessTokenExpiration),
                ChronoUnit.SECONDS);

        String scope = this.getScope(authentication);
        String userName = authentication.getName();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(userName)
                .claim("id", id)
                .claim("scope", scope)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims))
                .getTokenValue();
    }

    public ExchangeTokenResponse hanldeExchangeToken(String inputToken){
        RefreshToken currentRefreshToken = this.refreshTokenService.findByToken(inputToken);

        Instant now = Instant.now();

        if(now.isAfter(currentRefreshToken.getExpiredAt())){
            throw new ResourceNotFoundException("Token is expired");
        }

        User currentUser = currentRefreshToken.getUser();

        String newRefreshToken = this.createRefreshToken(currentUser);

        Instant validity = now.plus(Long.valueOf(accessTokenExpiration),
                ChronoUnit.SECONDS);

        String scope = "ROLE_" + currentUser.getRole().getName();
        // @formatter: off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(currentUser.getEmail())
                .claim("id", currentUser.getId())
                .claim("scope", scope)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();

        String accessToken = this.jwtEncoder.encode( // create accesstoken
            JwtEncoderParameters.from(jwsHeader, claims)
        ).getTokenValue();

        // @formatter: on
        ExchangeTokenResponse exToken = new ExchangeTokenResponse();
        exToken.setAccessToken(accessToken);
        exToken.setRefreshToken(newRefreshToken);
        exToken.setUser(new LoginResponseDTO.UserLogin(currentUser.getId(),
                currentUser.getEmail(),
                scope));

        // delete old token
        this.refreshTokenService.deteleById(currentRefreshToken.getId());

        return exToken;
    }
}
