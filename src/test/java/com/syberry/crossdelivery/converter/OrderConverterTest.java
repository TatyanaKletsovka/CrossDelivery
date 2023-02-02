package com.syberry.crossdelivery.converter;

import com.syberry.crossdelivery.order.converter.OrderConverter;
import com.syberry.crossdelivery.order.dto.OrderDto;
import com.syberry.crossdelivery.order.dto.OrderPersonalViewDto;
import com.syberry.crossdelivery.order.dto.OrderShortViewDto;
import com.syberry.crossdelivery.order.entity.Order;
import com.syberry.crossdelivery.order.entity.Status;
import com.syberry.crossdelivery.user.entity.User;
import org.assertj.core.api.Assertions;
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
public class OrderConverterTest {

    @InjectMocks
    OrderConverter orderConverter;
    Order order = new Order();
    Order newOrder = new Order();
    OrderDto orderDto = new OrderDto();
    OrderShortViewDto shortViewDto = new OrderShortViewDto();
    OrderPersonalViewDto personalViewDto = new OrderPersonalViewDto();

    @BeforeEach
    public void mock_role_user_repositories() {
        Long id = 1L;
        Long ownerId = 1L;
        Long performerId = 2L;
        Status status = Status.NEW;
        String title = "title";
        String description = "description";
        double price = 10;
        String departureCountry = "departureCountry";
        String departureLocation = "departureLocation";
        String destinationCountry = "destinationCountry";
        String destinationLocation = "destinationLocation";
        boolean blocked = false;
        LocalDateTime createdAt = LocalDateTime.now();

        User owner = new User();
        owner.setId(ownerId);
        User performer = new User();
        performer.setId(performerId);

        order.setId(id);
        order.setOwner(owner);
        order.setPerformer(performer);
        order.setStatus(status);
        order.setTitle(title);
        order.setDescription(description);
        order.setPrice(price);
        order.setDepartureCountry(departureCountry);
        order.setDepartureLocation(departureLocation);
        order.setDestinationCountry(destinationCountry);
        order.setDestinationLocation(destinationLocation);
        order.setBlocked(blocked);
        order.setCreatedAt(createdAt);

        newOrder.setTitle(title);
        newOrder.setDescription(description);
        newOrder.setPrice(price);
        newOrder.setDepartureCountry(departureCountry);
        newOrder.setDepartureLocation(departureLocation);
        newOrder.setDestinationCountry(destinationCountry);
        newOrder.setDestinationLocation(destinationLocation);

        orderDto.setId(id);
        orderDto.setOwnerId(ownerId);
        orderDto.setTitle(title);
        orderDto.setDescription(description);
        orderDto.setPrice(price);
        orderDto.setDepartureCountry(departureCountry);
        orderDto.setDepartureLocation(departureLocation);
        orderDto.setDestinationCountry(destinationCountry);
        orderDto.setDestinationLocation(destinationLocation);
        orderDto.setBlocked(blocked);
        orderDto.setCreatedAt(createdAt);

        shortViewDto.setId(id);
        shortViewDto.setTitle(title);
        shortViewDto.setPrice(price);
        shortViewDto.setDepartureCountry(departureCountry);
        shortViewDto.setDepartureLocation(departureLocation);
        shortViewDto.setDestinationCountry(destinationCountry);
        shortViewDto.setDestinationLocation(destinationLocation);
        shortViewDto.setCreatedAt(createdAt);

        personalViewDto.setId(id);
        personalViewDto.setOwnerId(ownerId);
        personalViewDto.setPerformerId(performerId);
        personalViewDto.setStatus(status);
        personalViewDto.setTitle(title);
        personalViewDto.setPrice(price);
        personalViewDto.setBlocked(blocked);
        personalViewDto.setCreatedAt(createdAt);
    }

    @Test
    public void should_SuccessfullyConvertToEntityOneArgument() {
        Order createdOrder = orderConverter.convertToEntity(orderDto);
        Assertions.assertThat(createdOrder)
                .usingRecursiveComparison()
                .ignoringFields("createdAt")
                .isEqualTo(newOrder);
    }

    @Test
    public void should_SuccessfullyConvertToEntityTwoArgument() {
        Order createdOrder = orderConverter.convertToEntity(orderDto, order);
        assertEquals(createdOrder, order);
    }

    @Test
    public void should_SuccessfullyConvertToOrderDto() {
        OrderDto dto = orderConverter.convertToDto(order);
        assertEquals(dto, orderDto);
    }

    @Test
    public void should_SuccessfullyConvertToUserWithAccessDto() {
        OrderShortViewDto dto = orderConverter.convertToShortViewDto(order);
        assertEquals(dto, shortViewDto);
    }

    @Test
    public void should_SuccessfullyConvertToUserAdminViewDto() {
        OrderPersonalViewDto dto = orderConverter.convertToPersonalViewDto(order);
        assertEquals(dto, personalViewDto);
    }
}
