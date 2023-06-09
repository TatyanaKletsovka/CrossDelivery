package com.syberry.crossdelivery.user.dto;

import com.syberry.crossdelivery.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserAdminViewDto extends UserWithAccessDto{

    private Set<Role> roles;
    private boolean blocked;
}
