package com.syberry.crossdelivery.user.dto;

import com.syberry.crossdelivery.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserAdminViewDto extends UserWithAccessDto{

    private Role role;
    private boolean isBlocked;
}
