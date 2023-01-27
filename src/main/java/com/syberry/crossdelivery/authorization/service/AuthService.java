package com.syberry.crossdelivery.authorization.service;

import com.syberry.crossdelivery.authorization.dto.LoginDto;
import com.syberry.crossdelivery.authorization.dto.LoginRequestDto;
import com.syberry.crossdelivery.user.dto.UpdatePasswordDto;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {

    LoginDto login(LoginRequestDto loginRequestDto);

    LoginDto logout();

    LoginDto refreshToken(HttpServletRequest request);
}
