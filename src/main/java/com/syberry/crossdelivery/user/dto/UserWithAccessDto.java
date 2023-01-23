package com.syberry.crossdelivery.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserWithAccessDto extends UserDto{

    @NotBlank
    @Size(max = 50)
    protected String username;
    @NotBlank
    @Size(max = 50)
    protected String firstName;
    @NotBlank
    @Size(max = 50)
    protected String lastName;
    @NotBlank
    @Size(max = 50)
    @Email
    protected String email;
    @NotBlank
    @Pattern(regexp = "^\\d{10}$", message = "The phone number must consist of 10 digits")
    protected String phoneNumber;
}
