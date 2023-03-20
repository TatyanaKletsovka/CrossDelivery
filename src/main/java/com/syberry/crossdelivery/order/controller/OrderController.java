package com.syberry.crossdelivery.order.controller;

import com.syberry.crossdelivery.order.dto.OrderDto;
import com.syberry.crossdelivery.order.dto.OrderFilterDto;
import com.syberry.crossdelivery.order.dto.OrderPersonalViewDto;
import com.syberry.crossdelivery.order.dto.OrderShortViewDto;
import com.syberry.crossdelivery.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    
    @GetMapping
    public Page<OrderShortViewDto> getAllOrders(OrderFilterDto filter, Pageable pageable) {
        log.info("GET-request: getting all orders");
        return orderService.getAllOrders(filter, pageable);
    }

    @GetMapping("/users/{id}")
    public Page<OrderShortViewDto> getOrdersByUserId(@PathVariable("id") Long id, OrderFilterDto filter,
                                                     Pageable pageable) {
        log.info("GET-request: getting all owned orders of the user with id: {}", id);
        return orderService.getOwnedOrdersByUserId(id, filter, pageable);
    }

    @GetMapping("/owns")
    public Page<OrderPersonalViewDto> getAuthorisedUserOwnOrders(OrderFilterDto filter, Pageable pageable) {
        log.info("GET-request: getting authorised user own orders");
        return orderService.getAuthorisedUserOwnOrders(filter, pageable);
    }

    @GetMapping("/performs")
    public Page<OrderPersonalViewDto> getAuthorisedUserPerformOrders(OrderFilterDto filter, Pageable pageable) {
        log.info("GET-request: getting authorised user perform orders");
        return orderService.getAuthorisedUserPerformOrders(filter, pageable);
    }

    @GetMapping("/{id}")
    public OrderDto getOrderById(@PathVariable("id") Long id) {
        log.info("GET-request: getting order with id: {}", id);
        return orderService.getOrderById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@Valid @RequestBody OrderDto dto) {
        log.info("POST-request: creating order");
        return orderService.createOrder(dto);
    }

    @PutMapping("/{id}")
    public OrderDto updateOrder(@PathVariable("id") Long id, @Valid @RequestBody OrderDto dto) {
        log.info("PUT-request: updating order with id: {}", id);
        dto.setId(id);
        return orderService.updateOrderById(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteOrderById(@PathVariable("id") Long id) {
        log.info("DELETE-request: deleting order with id: {}", id);
        orderService.deleteOrderById(id);
    }

    @PutMapping("/blocked/{id}")
    public OrderDto reverseBlockedOrderById(@PathVariable("id") Long id) {
        log.info("PUT-request: reverse blocked status of the order with id: {} ", id);
        return orderService.reverseBlockedOrderById(id);
    }
}
