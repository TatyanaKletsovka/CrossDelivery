package com.syberry.crossdelivery.order.converter;

import com.syberry.crossdelivery.order.dto.OrderDto;
import com.syberry.crossdelivery.order.dto.OrderPersonalViewDto;
import com.syberry.crossdelivery.order.dto.OrderShortViewDto;
import com.syberry.crossdelivery.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConverter {

    public Order convertToEntity(OrderDto dto) {
        return buildOrderStandardFields(dto, new Order());
    }

    public Order convertToEntity(OrderDto dto, Order order) {
        return buildOrderStandardFields(dto, order);
    }

    private Order buildOrderStandardFields(OrderDto dto, Order order) {
        order.setTitle(dto.getTitle());
        order.setDescription(dto.getDescription());
        order.setPrice(dto.getPrice());
        order.setDepartureCountry(dto.getDepartureCountry());
        order.setDepartureLocation(dto.getDepartureLocation());
        order.setDestinationCountry(dto.getDestinationCountry());
        order.setDestinationLocation(dto.getDestinationLocation());
        return order;
    }

    public OrderDto convertToDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .ownerId(order.getOwner().getId())
                .title(order.getTitle())
                .description(order.getDescription())
                .price(order.getPrice())
                .departureCountry(order.getDepartureCountry())
                .departureLocation(order.getDepartureLocation())
                .destinationCountry(order.getDestinationCountry())
                .destinationLocation(order.getDestinationLocation())
                .blocked(order.isBlocked())
                .createdAt(order.getCreatedAt())
                .build();
    }

    public OrderShortViewDto convertToShortViewDto(Order order) {
        return OrderShortViewDto.builder()
                .id(order.getId())
                .title(order.getTitle())
                .price(order.getPrice())
                .departureCountry(order.getDepartureCountry())
                .departureLocation(order.getDepartureLocation())
                .destinationCountry(order.getDestinationCountry())
                .destinationLocation(order.getDestinationLocation())
                .createdAt(order.getCreatedAt())
                .build();
    }

    public OrderPersonalViewDto convertToPersonalViewDto(Order order) {
        return OrderPersonalViewDto.builder()
                .id(order.getId())
                .ownerId(order.getOwner().getId())
                .performerId(order.getPerformer() != null ? order.getPerformer().getId() : null)
                .status(order.getStatus())
                .title(order.getTitle())
                .price(order.getPrice())
                .blocked(order.isBlocked())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
