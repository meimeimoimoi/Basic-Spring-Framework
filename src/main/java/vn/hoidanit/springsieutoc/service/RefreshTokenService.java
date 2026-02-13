package vn.hoidanit.springsieutoc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.hoidanit.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.hoidanit.springsieutoc.model.RefreshToken;
import vn.hoidanit.springsieutoc.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor

public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void createRefreshToken(RefreshToken rf){
        this.refreshTokenRepository.save(rf);
    }

    public RefreshToken findByToken(String token){
        return this.refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));
    }

    public void deteleById(Long id){
        this.refreshTokenRepository.deleteById(id);
    }
}
