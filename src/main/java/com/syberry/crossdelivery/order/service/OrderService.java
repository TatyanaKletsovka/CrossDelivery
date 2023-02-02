package com.syberry.crossdelivery.order.service;

import com.syberry.crossdelivery.order.dto.OrderDto;
import com.syberry.crossdelivery.order.dto.OrderFilterDto;
import com.syberry.crossdelivery.order.dto.OrderPersonalViewDto;
import com.syberry.crossdelivery.order.dto.OrderShortViewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    
    Page<OrderShortViewDto> getAllOrders(OrderFilterDto filter, Pageable pageable);

    Page<OrderShortViewDto> getOwnedOrdersByUserId(Long id, OrderFilterDto filter, Pageable pageable);

    Page<OrderPersonalViewDto> getAuthorisedUserOwnOrders(OrderFilterDto filter, Pageable pageable);

    Page<OrderPersonalViewDto> getAuthorisedUserPerformOrders(OrderFilterDto filter, Pageable pageable);

    OrderDto getOrderById(Long id);

    OrderDto createOrder(OrderDto dto);

    OrderDto updateOrderById(OrderDto dto);

    void deleteOrderById(Long id);

    OrderDto reverseBlockedOrderById(Long id);
}
