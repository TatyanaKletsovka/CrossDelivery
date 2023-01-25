package com.syberry.crossdelivery.authorization.controller;

import com.syberry.crossdelivery.authorization.dto.LoginDto;
import com.syberry.crossdelivery.authorization.dto.LoginRequestDto;
import com.syberry.crossdelivery.authorization.service.AuthService;
import com.syberry.crossdelivery.user.dto.UpdatePasswordDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        log.info("POST-request: user sign in");
        LoginDto loginDto = authService.login(loginRequestDto);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, loginDto.getCookie())
                .header(HttpHeaders.SET_COOKIE, loginDto.getRefreshCookie())
                .body(loginDto.getWithAccessDto());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        log.info("POST-request: user sign out");
        LoginDto loginDto = authService.logout();
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, loginDto.getCookie())
                .header(HttpHeaders.SET_COOKIE, loginDto.getRefreshCookie())
                .build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        log.info("POST-request: refresh token");
        LoginDto tokens = authService.refreshToken(request);
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, tokens.getCookie())
                .header(HttpHeaders.SET_COOKIE, tokens.getRefreshCookie())
                .build();
    }
}
