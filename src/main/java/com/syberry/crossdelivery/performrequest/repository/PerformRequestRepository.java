package com.syberry.crossdelivery.performrequest.repository;

import com.syberry.crossdelivery.exception.EntityNotFoundException;
import com.syberry.crossdelivery.performrequest.entity.PerformRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerformRequestRepository extends JpaRepository<PerformRequest, Long>,
        JpaSpecificationExecutor<PerformRequest> {

    Optional<PerformRequest> findByOrderIdAndPerformerId(Long orderId, Long performerId);

    List<PerformRequest> findByOrderId(Long id);

    default PerformRequest findByIdIfExists(Long id) {
        return findById(id).orElseThrow(()
                -> new EntityNotFoundException(String.format("Perform request with id: %s is not found", id)));
    }

}
