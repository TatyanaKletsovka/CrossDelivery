package com.syberry.crossdelivery.converter;

import com.syberry.crossdelivery.order.entity.Order;
import com.syberry.crossdelivery.performrequest.converter.PerformRequestConverter;
import com.syberry.crossdelivery.performrequest.dto.PerformRequestDto;
import com.syberry.crossdelivery.performrequest.entity.PerformRequest;
import com.syberry.crossdelivery.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PerformRequestConverterTest {

    @InjectMocks
    PerformRequestConverter converter;
    PerformRequest request = new PerformRequest();
    PerformRequestDto dto = new PerformRequestDto();

    @BeforeEach
    public void mock_role_user_repositories() {
        Long id = 1L;
        Long performerId = 2L;
        Boolean approved = false;
        LocalDateTime createdAt = LocalDateTime.now();

        User performer = new User();
        performer.setId(performerId);

        Order order = new Order();
        order.setId(id);

        request.setId(id);
        request.setOrder(order);
        request.setPerformer(performer);
        request.setApproved(approved);
        request.setCreatedAt(createdAt);

        dto.setId(id);
        dto.setOrderId(id);
        dto.setPerformerId(performerId);
        dto.setApproved(approved);
        dto.setCreatedAt(createdAt);
    }

    @Test
    public void should_SuccessfullyConvertToDto() {
        assertEquals(converter.convertToDto(request), dto);
    }
}
