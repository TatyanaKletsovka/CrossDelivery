package com.syberry.crossdelivery.authorization.dto;

import com.syberry.crossdelivery.user.dto.UserWithAccessDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    private String cookie;
    private String refreshCookie;
    private UserWithAccessDto withAccessDto;
}
