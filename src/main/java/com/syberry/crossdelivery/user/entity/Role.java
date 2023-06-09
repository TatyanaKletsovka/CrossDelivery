package com.syberry.crossdelivery.user.entity;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Role {

    ADMIN,
    USER;

    public static String getNames() {
        return Arrays.stream(Role.class.getEnumConstants()).map(Enum::name).collect(Collectors.joining(", "));
    }
}
