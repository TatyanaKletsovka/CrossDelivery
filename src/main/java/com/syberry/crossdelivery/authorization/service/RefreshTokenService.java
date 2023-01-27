package com.syberry.crossdelivery.authorization.service;

import com.syberry.crossdelivery.authorization.dto.LoginDto;
import com.syberry.crossdelivery.authorization.entity.RefreshToken;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(Long userId);

    LoginDto refreshAccessToken(String accessToken);

    RefreshToken verifyExpiration(String token);

    void deleteByUserId(Long userId);
}
