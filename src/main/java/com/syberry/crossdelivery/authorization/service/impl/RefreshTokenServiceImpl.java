package com.syberry.crossdelivery.authorization.service.impl;

import com.syberry.crossdelivery.authorization.dto.LoginDto;
import com.syberry.crossdelivery.authorization.entity.RefreshToken;
import com.syberry.crossdelivery.authorization.repository.RefreshTokenRepository;
import com.syberry.crossdelivery.authorization.service.RefreshTokenService;
import com.syberry.crossdelivery.authorization.util.JwtUtils;
import com.syberry.crossdelivery.exception.TokenRefreshException;
import com.syberry.crossdelivery.user.entity.User;
import com.syberry.crossdelivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${cross-delivery.security.jwtRefreshExpirationHr}")
    private Long refreshTokenDurationHr;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findByIdIfExistsAndIsBlockedFalse(userId);
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUserId(userId);
        RefreshToken refreshToken;
        if (optionalRefreshToken.isPresent() && optionalRefreshToken.get().getExpiryDate().isAfter(Instant.now())) {
            refreshToken = optionalRefreshToken.get();
        } else {
            refreshToken = new RefreshToken();
            refreshToken.setUser(user);
            refreshToken.setExpiryDate(Instant.now().plus(refreshTokenDurationHr, ChronoUnit.HOURS));
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshTokenRepository.save(refreshToken);
        }
        return refreshToken;
    }

    @Override
    public LoginDto refreshAccessToken(String token) {
        User user = verifyExpiration(token).getUser();
        ResponseCookie accessToken = jwtUtils.generateJwtCookie(user);
        ResponseCookie refreshToken = jwtUtils.generateRefreshJwtCookie(createRefreshToken(user.getId()).getToken());
        return new LoginDto(accessToken.toString(), refreshToken.toString(), null);
    }

    @Override
    public RefreshToken verifyExpiration(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenRefreshException("Token is not found"));
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new TokenRefreshException("Refresh token was expired. Please make a new sign in request");
        }
        return refreshToken;
    }

    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        try {
            userRepository.findByIdIfExistsAndIsBlockedFalse(userId);
            refreshTokenRepository.deleteByUserId(userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
