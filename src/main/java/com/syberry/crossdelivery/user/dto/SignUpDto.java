package com.syberry.crossdelivery.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {

    @NotBlank
    @Size(max = 50)
    private String username;
    @NotBlank
    @Size(max = 50)
    private String firstName;
    @NotBlank
    @Size(max = 50)
    private String lastName;
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    @Pattern(regexp = "^\\d{10}$", message = "The phone number must consist of 10 digits")
    private String phoneNumber;
}
