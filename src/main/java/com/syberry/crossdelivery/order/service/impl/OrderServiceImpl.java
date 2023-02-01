package com.syberry.crossdelivery.order.service.impl;

import com.syberry.crossdelivery.exception.AccessException;
import com.syberry.crossdelivery.order.converter.OrderConverter;
import com.syberry.crossdelivery.order.dto.OrderDto;
import com.syberry.crossdelivery.order.dto.OrderFilterDto;
import com.syberry.crossdelivery.order.dto.OrderPersonalViewDto;
import com.syberry.crossdelivery.order.dto.OrderShortViewDto;
import com.syberry.crossdelivery.order.entity.Order;
import com.syberry.crossdelivery.order.repository.OrderRepository;
import com.syberry.crossdelivery.order.service.OrderService;
import com.syberry.crossdelivery.order.service.specification.OrderSpecification;
import com.syberry.crossdelivery.user.entity.User;
import com.syberry.crossdelivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.syberry.crossdelivery.authorization.util.SecurityContextUtil.getUserDetails;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderConverter orderConverter;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderSpecification specification;

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<OrderShortViewDto> getAllOrders(OrderFilterDto filter, Pageable pageable) {
        return orderRepository.findAll(specification.buildGetAllSpecification(filter), pageable)
                .map(orderConverter::convertToShortViewDto);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<OrderShortViewDto> getOwnedOrdersByUserId(Long id, OrderFilterDto filter, Pageable pageable) {
        return orderRepository.findAll(specification.buildGetAllByUserIdSpecification(filter, id), pageable)
                .map(orderConverter::convertToShortViewDto);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<OrderPersonalViewDto> getAuthorisedUserOwnOrders(OrderFilterDto filter, Pageable pageable) {
        return orderRepository.findAll(specification.buildGetOwnedSpecification(filter, getUserDetails().getId()),
                        pageable).map(orderConverter::convertToPersonalViewDto);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<OrderPersonalViewDto> getAuthorisedUserPerformOrders(OrderFilterDto filter, Pageable pageable) {
        return orderRepository.findAll(specification.buildGetPerformedSpecification(filter, getUserDetails().getId()),
                        pageable).map(orderConverter::convertToPersonalViewDto);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public OrderDto getOrderById(Long id) {
        return orderConverter.convertToDto(orderRepository.findByIdIfExists(id));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public OrderDto createOrder(OrderDto dto) {
        User owner = userRepository.findByIdIfExistsAndBlockedFalse(getUserDetails().getId());
        Order order = orderConverter.convertToEntity(dto);
        order.setOwner(owner);
        return orderConverter.convertToDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public OrderDto updateOrderById(OrderDto dto) {
        Order orderDb = orderRepository.findByIdAndBlockedFalseIfExists(dto.getId());
        validateIfOwnedOrder(orderDb);
        validateIfCanBeChanged(orderDb);
        Order order = orderConverter.convertToEntity(dto, orderDb);
        return orderConverter.convertToDto(order);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Transactional
    public void deleteOrderById(Long id) {
        Order order = orderRepository.findByIdAndBlockedFalseIfExists(id);
        validateIfOwnedOrder(order);
        validateIfCanBeChanged(order);
        orderRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Transactional
    public OrderDto reverseBlockedOrderById(Long id) {
        Order order = orderRepository.findByIdIfExists(id);
        order.setBlocked(!order.isBlocked());
        return orderConverter.convertToDto(order);
    }

    private void validateIfOwnedOrder(Order order) {
        if (!order.getOwner().getId().equals(getUserDetails().getId())) {
            throw new AccessException("The user can change only his own orders");
        }
    }

    private void validateIfCanBeChanged(Order order) {
        if (order.getPerformer() != null) {
            throw new AccessException("The order with performer can't be changed");
        }
    }
}
