package com.syberry.crossdelivery.service;

import com.syberry.crossdelivery.authorization.dto.LoginDto;
import com.syberry.crossdelivery.authorization.dto.LoginRequestDto;
import com.syberry.crossdelivery.authorization.entity.RefreshToken;
import com.syberry.crossdelivery.authorization.security.UserDetailsImpl;
import com.syberry.crossdelivery.authorization.service.RefreshTokenService;
import com.syberry.crossdelivery.authorization.service.impl.AuthServiceImpl;
import com.syberry.crossdelivery.authorization.util.JwtUtils;
import com.syberry.crossdelivery.exception.EntityNotFoundException;
import com.syberry.crossdelivery.exception.TokenRefreshException;
import com.syberry.crossdelivery.user.converter.UserConverter;
import com.syberry.crossdelivery.user.dto.UserWithAccessDto;
import com.syberry.crossdelivery.user.entity.User;
import com.syberry.crossdelivery.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthServiceTest {
    @InjectMocks
    private AuthServiceImpl authService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private UserConverter userConverter;

    @BeforeEach
    public void mock() {
        ResponseCookie jwtCookie = ResponseCookie.from("cookie", "cookie").build();
        when(jwtUtils.generateJwtCookie(any(UserDetailsImpl.class))).thenReturn(jwtCookie);
        when(refreshTokenService.createRefreshToken(any(Long.class))).thenReturn(new RefreshToken());
        ResponseCookie refreshJwtCookie = ResponseCookie
                .from("refresh-token", "refresh-token").build();
        when(jwtUtils.generateRefreshJwtCookie(any())).thenReturn(refreshJwtCookie);
        when(authentication.getPrincipal())
                .thenReturn(new UserDetailsImpl(1L, null, "user@gmail.com",  List.of()));
    }

    @Test
    void should_SuccessfullyLogin() {
        ResponseCookie jwtCookie = ResponseCookie.from("cookie", "cookie").build();
        ResponseCookie refreshJwtCookie = ResponseCookie
                .from("refresh-token", "refresh-token").build();
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(userRepository.findByIdIfExistsAndBlockedFalse(any())).thenReturn(new User());
        when(userConverter.convertToDto(any(User.class))).thenReturn(new UserWithAccessDto());
        assertEquals(authService.login(new LoginRequestDto("user@gmail.com", "password")),
                new LoginDto(jwtCookie.toString(), refreshJwtCookie.toString(), null));
    }

    @Test
    void should_ThrowError_When_SigningInWithNoneExistingEmail() {
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(userRepository.findByIdIfExistsAndBlockedFalse(any())).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, ()
                -> authService.login(new LoginRequestDto("user@gmail.com", "password")));
    }

    @Test
    void should_SuccessfullyRefreshToken() {
        String token = "refreshToken";
        when(jwtUtils.getJwtRefreshFromCookies(any())).thenReturn(token);
        ResponseCookie jwtCookie = ResponseCookie.from("cookie", token).build();
        LoginDto loginDto = new LoginDto(jwtCookie.toString(),"refresh",null);
        when(refreshTokenService.refreshAccessToken(any())).thenReturn(loginDto);
        assertEquals(authService.refreshToken(null), loginDto);
    }

    @Test
    void should_ThrowError_When_RefreshingWithEmptyToken() {
        when(jwtUtils.getJwtRefreshFromCookies(any())).thenReturn("");
        assertThrows(TokenRefreshException.class, () -> authService.refreshToken(null));
    }

    @Test
    void should_ThrowError_When_RefreshingWithExpiredToken() {
        when(jwtUtils.getJwtRefreshFromCookies(any())).thenReturn("token");
        when(refreshTokenService.refreshAccessToken(any())).thenThrow(TokenRefreshException.class);
        assertThrows(TokenRefreshException.class, () -> authService.refreshToken(null));
    }

    @Test
    void should_SuccessfullyLogout() {
        ResponseCookie jwtCookie = ResponseCookie.from("cookie", null).build();
        when(jwtUtils.getCleanJwtCookie()).thenReturn(jwtCookie);
        when(jwtUtils.getCleanJwtRefreshCookie()).thenReturn(jwtCookie);
        LoginDto expectedLoginDto = new LoginDto(jwtCookie.toString(), jwtCookie.toString(), null);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal())
                .thenReturn(new UserDetailsImpl(1L, null, "user@gmail.com", null));
        assertEquals(authService.logout(), expectedLoginDto);
    }
}
