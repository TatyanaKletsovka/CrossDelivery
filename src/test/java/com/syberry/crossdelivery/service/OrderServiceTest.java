package com.syberry.crossdelivery.service;

import com.syberry.crossdelivery.authorization.security.UserDetailsImpl;
import com.syberry.crossdelivery.exception.AccessException;
import com.syberry.crossdelivery.exception.EntityNotFoundException;
import com.syberry.crossdelivery.order.converter.OrderConverter;
import com.syberry.crossdelivery.order.dto.OrderDto;
import com.syberry.crossdelivery.order.entity.Order;
import com.syberry.crossdelivery.order.entity.Status;
import com.syberry.crossdelivery.order.repository.OrderRepository;
import com.syberry.crossdelivery.order.service.impl.OrderServiceImpl;
import com.syberry.crossdelivery.order.service.specification.OrderSpecification;
import com.syberry.crossdelivery.user.entity.Role;
import com.syberry.crossdelivery.user.entity.User;
import com.syberry.crossdelivery.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OrderServiceTest {

    @InjectMocks
    private OrderServiceImpl orderService;
    @Mock
    private OrderConverter orderConverter;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    OrderSpecification specification;

    Order order = new Order();
    OrderDto orderDto = new OrderDto();
    User owner = new User();
    User performer = new User();

    @BeforeEach
    public void auth() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal())
                .thenReturn(new UserDetailsImpl(1L, "user@gmail.com", "",
                        List.of(new SimpleGrantedAuthority(Role.ADMIN.name()))));
    }

    @BeforeEach
    public void mockOrders() {
        Long id1 = 1L;
        Long id2 = 2L;
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

        owner.setId(id1);
        performer.setId(id2);

        order.setId(id1);
        order.setOwner(owner);
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

        orderDto.setId(id1);
        orderDto.setTitle(title);
        orderDto.setDescription(description);
        orderDto.setPrice(price);
        orderDto.setDepartureCountry(departureCountry);
        orderDto.setDepartureLocation(departureLocation);
        orderDto.setDestinationCountry(destinationCountry);
        orderDto.setDestinationLocation(destinationLocation);
        orderDto.setBlocked(blocked);
        orderDto.setCreatedAt(createdAt);
    }

    @Test
    void should_SuccessfullyReturnAllUsers() {
        when(orderRepository.findAll(specification.buildGetAllSpecification(any()), PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(List.of()));
        assertEquals(orderService.getAllOrders(any(), PageRequest.of(0, 20)), new PageImpl<>(List.of()));
    }

    @Test
    void should_SuccessfullyReturnOwnedOrdersByUserId() {
        when(orderRepository.findAll(specification.buildGetAllByUserIdSpecification(any(), any()), PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(List.of()));
        assertEquals(orderService.getAllOrders(any(), PageRequest.of(0, 20)), new PageImpl<>(List.of()));
    }

    @Test
    void should_SuccessfullyReturnAuthorisedUserOwnOrders() {
        when(orderRepository.findAll(specification.buildGetOwnedSpecification(any(), any()), PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(List.of()));
        assertEquals(orderService.getAllOrders(any(), PageRequest.of(0, 20)), new PageImpl<>(List.of()));
    }

    @Test
    void should_SuccessfullyReturnAuthorisedUserPerformOrders() {
        when(orderRepository.findAll(specification.buildGetPerformedSpecification(any(), any()), PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(List.of()));
        assertEquals(orderService.getAllOrders(any(), PageRequest.of(0, 20)), new PageImpl<>(List.of()));
    }

    @Test
    void should_SuccessfullyGetOrderById() {
        when(orderRepository.findByIdIfExists(any())).thenReturn(order);
        when(orderConverter.convertToDto(any())).thenReturn(orderDto);
        assertEquals(orderService.getOrderById(any()), orderDto);
    }

    @Test
    void should_ThrowError_WhenGettingByIdNoneExistingOrder() {
        when(orderRepository.findByIdIfExists(-1L)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> orderService.getOrderById(-1L));

    }

    @Test
    void should_SuccessfullyCreateOrder() {
        when(userRepository.findByIdIfExistsAndBlockedFalse(any())).thenReturn(new User());
        when(orderConverter.convertToEntity(any())).thenReturn(order);
        when(orderConverter.convertToDto(any())).thenReturn(orderDto);
        assertEquals(orderService.createOrder(orderDto), orderDto);
    }

    @Test
    void should_SuccessfullyUpdateOrder() {
        when(orderRepository.findByIdAndBlockedFalseIfExists(any())).thenReturn(order);
        when(orderConverter.convertToEntity(any(), any())).thenReturn(order);
        when(orderConverter.convertToDto(any())).thenReturn(orderDto);
        assertEquals(orderService.updateOrderById(orderDto), orderDto);
    }

    @Test
    void should_ThrowError_When_UpdatingNotOwnOrder() {
        Order updatedOrder = new Order();
        updatedOrder.setOwner(performer);
        when(orderRepository.findByIdAndBlockedFalseIfExists(any())).thenReturn(updatedOrder);
        assertThrows(AccessException.class, () -> orderService.updateOrderById(orderDto));
    }

    @Test
    void should_ThrowError_When_UpdatingOrderWithPerformer() {
        Order updatedOrder = new Order();
        updatedOrder.setOwner(owner);
        updatedOrder.setPerformer(performer);
        when(orderRepository.findByIdAndBlockedFalseIfExists(any())).thenReturn(updatedOrder);
        assertThrows(AccessException.class, () -> orderService.updateOrderById(orderDto));
    }

    @Test
    void should_SuccessfullyDeleteOrder() {
        when(orderRepository.findByIdAndBlockedFalseIfExists(any())).thenReturn(order);
        orderService.deleteOrderById(order.getId());
        assertNull(orderService.getOrderById(order.getId()));
    }

    @Test
    void should_ThrowError_When_DeletingNotOwnOrder() {
        Order updatedOrder = new Order();
        updatedOrder.setOwner(performer);
        when(orderRepository.findByIdAndBlockedFalseIfExists(any())).thenReturn(updatedOrder);
        assertThrows(AccessException.class, () -> orderService.deleteOrderById(any()));
    }

    @Test
    void should_ThrowError_When_DeletingOrderWithPerformer() {
        Order updatedOrder = new Order();
        updatedOrder.setOwner(owner);
        updatedOrder.setPerformer(performer);
        when(orderRepository.findByIdAndBlockedFalseIfExists(any())).thenReturn(updatedOrder);
        assertThrows(AccessException.class, () -> orderService.deleteOrderById(any()));
    }

    @Test
    void should_SuccessfullyReverseOrderBlocked() {
        when(orderRepository.findByIdIfExists(any())).thenReturn(order);
        when(orderConverter.convertToDto(any())).thenReturn(orderDto);
        orderDto.setBlocked(true);
        assertEquals(orderService.reverseBlockedOrderById(1L), orderDto);
    }
}
