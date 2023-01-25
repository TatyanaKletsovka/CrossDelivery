package com.syberry.crossdelivery.authorization.service.impl;

import com.syberry.crossdelivery.authorization.dto.LoginDto;
import com.syberry.crossdelivery.authorization.dto.LoginRequestDto;
import com.syberry.crossdelivery.authorization.entity.RefreshToken;
import com.syberry.crossdelivery.authorization.security.UserDetailsImpl;
import com.syberry.crossdelivery.authorization.service.AuthService;
import com.syberry.crossdelivery.authorization.service.RefreshTokenService;
import com.syberry.crossdelivery.authorization.util.JwtUtils;
import com.syberry.crossdelivery.exception.TokenRefreshException;
import com.syberry.crossdelivery.exception.UpdateException;
import com.syberry.crossdelivery.user.converter.UserConverter;
import com.syberry.crossdelivery.user.dto.UpdatePasswordDto;
import com.syberry.crossdelivery.user.dto.UserWithAccessDto;
import com.syberry.crossdelivery.user.entity.User;
import com.syberry.crossdelivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import static com.syberry.crossdelivery.authorization.util.SecurityContextUtil.getUserDetails;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserConverter userConverter;

    @Override
    public LoginDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return authenticateUser(userDetails);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public LoginDto logout() {
        refreshTokenService.deleteByUserId(getUserDetails().getId());
        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        return new LoginDto(jwtCookie.toString(), jwtRefreshCookie.toString(), null);
    }

    @Override
    public LoginDto refreshToken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);
        if (refreshToken == null || refreshToken.length() == 0) {
            throw new TokenRefreshException("Refresh token is empty");
        }
        return refreshTokenService.refreshAccessToken(refreshToken);
    }

    private LoginDto authenticateUser(UserDetailsImpl userDetails) {
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());
        return new LoginDto(jwtCookie.toString(), jwtRefreshCookie.toString(),
                getUserById(userDetails.getId()));
    }

    private UserWithAccessDto getUserById(Long id) {
        return userConverter.convertToUserWithAccessDto(userRepository.findByIdIfExistsAndIsBlockedFalse(id));
    }
}
