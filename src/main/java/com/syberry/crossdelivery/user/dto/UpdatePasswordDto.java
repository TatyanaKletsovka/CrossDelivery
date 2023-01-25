package com.syberry.crossdelivery.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordDto {
    @NotBlank
    @Size(max=50)
    private String currentPassword;
    @NotBlank
    @Size(max=50)
    private String newPassword;
}
