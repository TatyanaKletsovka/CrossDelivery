package com.syberry.crossdelivery.converter;

import com.syberry.crossdelivery.exception.InvalidArgumentTypeException;
import com.syberry.crossdelivery.order.converter.StatusConverter;
import com.syberry.crossdelivery.order.entity.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class StatusConverterTest {

    @InjectMocks
    StatusConverter statusConverter;

    @Test
    public void should_SuccessfullyConvertToRole() {
        assertEquals(statusConverter.convertToStatus("new"), Status.NEW);
    }

    @Test
    void should_ThrowError_When_ConvertingInvalidString() {
        assertThrows(InvalidArgumentTypeException.class, () -> statusConverter.convertToStatus(""));
    }
}
