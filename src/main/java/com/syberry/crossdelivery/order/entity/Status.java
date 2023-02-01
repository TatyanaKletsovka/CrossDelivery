package com.syberry.crossdelivery.order.entity;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Status {

    NEW,
    WITH_REQUESTS,
    WITH_PERFORMER,
    DELIVERING,
    DELIVERED,
    CANCELLED;

    public static String getNames() {
        return Arrays.stream(Status.class.getEnumConstants()).map(Enum::name).collect(Collectors.joining(", "));
    }
}
