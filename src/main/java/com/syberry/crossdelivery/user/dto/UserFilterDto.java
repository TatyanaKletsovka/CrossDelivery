package com.syberry.crossdelivery.user.dto;

import com.syberry.crossdelivery.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFilterDto {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.of(2000, 1, 1);
    private static final LocalDate DEFAULT_END_DATE = LocalDate.of(3000, 1, 1);

    private Long id;
    private String username = "";
    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private String phoneNumber = "";
    private Role role;
    private Boolean isBlocked;
    private LocalDate createdAtStart = DEFAULT_START_DATE;
    private LocalDate createdAtEnd = DEFAULT_END_DATE;

}
