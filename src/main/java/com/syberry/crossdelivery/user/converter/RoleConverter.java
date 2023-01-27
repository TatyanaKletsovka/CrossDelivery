package com.syberry.crossdelivery.user.converter;

import com.syberry.crossdelivery.exception.InvalidArgumentTypeException;
import com.syberry.crossdelivery.user.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleConverter {

    public Role convertToRole(String role) {
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidArgumentTypeException(
                    String.format("Error while converting invalid role: %s. Valid roles: ADMIN, USER", role));
        }
    }
}
