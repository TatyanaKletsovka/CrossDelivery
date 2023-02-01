package com.syberry.crossdelivery.order.converter;

import com.syberry.crossdelivery.exception.InvalidArgumentTypeException;
import com.syberry.crossdelivery.order.entity.Status;
import org.springframework.stereotype.Component;

@Component
public class StatusConverter {

    public Status convertToStatus(String status) {
        try {
            return Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidArgumentTypeException(
                    String.format("Error while converting invalid status: %s. Valid statuses: %s",
                            status, Status.getNames()));
        }
    }
}
