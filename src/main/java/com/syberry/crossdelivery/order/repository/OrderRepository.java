package com.syberry.crossdelivery.order.repository;

import com.syberry.crossdelivery.exception.EntityNotFoundException;
import com.syberry.crossdelivery.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    Optional<Order> findByIdAndBlockedFalseAndOwnerBlockedFalse(Long id);

    default Order findByIdAndBlockedFalseIfExists(Long id) {
        return findByIdAndBlockedFalseAndOwnerBlockedFalse(id).orElseThrow(()
                -> new EntityNotFoundException(String.format("Order with id: %s is not found", id)));
    }

    default Order findByIdIfExists(Long id) {
        return findById(id).orElseThrow(()
                -> new EntityNotFoundException(String.format("Order with id: %s is not found", id)));
    }
}
