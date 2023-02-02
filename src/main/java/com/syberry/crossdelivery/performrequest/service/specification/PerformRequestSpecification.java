package com.syberry.crossdelivery.performrequest.service.specification;

import com.syberry.crossdelivery.order.entity.Order_;
import com.syberry.crossdelivery.performrequest.dto.PerformRequestFilterDto;
import com.syberry.crossdelivery.performrequest.entity.PerformRequest;
import com.syberry.crossdelivery.performrequest.entity.PerformRequest_;
import com.syberry.crossdelivery.user.entity.User_;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.syberry.crossdelivery.authorization.util.SecurityContextUtil.getUserDetails;

@Service
@RequiredArgsConstructor
public class PerformRequestSpecification {

    public Specification<PerformRequest> buildGetPersonalOrdersRequestsSpecification(PerformRequestFilterDto filter) {
        return buildPerformerIdSpecification(filter.getPerformerId())
                .and(buildOwnerIdSpecification(getUserDetails().getId()))
                .and(buildStandardFieldsSpecification(filter));
    }

    public Specification<PerformRequest> buildGetPersonalRequestsSpecification(PerformRequestFilterDto filter) {
        return buildPerformerIdSpecification(getUserDetails().getId())
                .and(buildOwnerIdSpecification(filter.getOwnerId()))
                .and(buildStandardFieldsSpecification(filter));
    }

    private Specification<PerformRequest> buildStandardFieldsSpecification(PerformRequestFilterDto filter) {
        return buildIdSpecification(filter.getId())
                .and(buildOrderIdSpecification(filter.getOrderId()))
                .and(buildApprovedSpecification(filter.getApproved()))
                .and(buildCreatedAtBetweenSpecification(filter.getCreatedAtStart().atStartOfDay(),
                        filter.getCreatedAtEnd().plusDays(1).atStartOfDay()));
    }

    private Specification<PerformRequest> buildIdSpecification(Long id) {
        return (root, query, criteriaBuilder) -> id != null
                ? criteriaBuilder.equal(root.get(PerformRequest_.ID), id)
                : null;
    }

    private Specification<PerformRequest> buildOrderIdSpecification(Long orderId) {
        return (root, query, criteriaBuilder) -> orderId != null
                ? criteriaBuilder.equal(root.get(PerformRequest_.ORDER).get(Order_.ID), orderId)
                : null;
    }

    private Specification<PerformRequest> buildPerformerIdSpecification(Long performerId) {
        return (root, query, criteriaBuilder) -> performerId != null
                ? criteriaBuilder.equal(root.get(PerformRequest_.PERFORMER).get(User_.ID), performerId)
                : null;
    }

    private Specification<PerformRequest> buildApprovedSpecification(Boolean approved) {
        return (root, query, criteriaBuilder) -> approved != null
                ? criteriaBuilder.equal(root.get(PerformRequest_.APPROVED), approved)
                : null;
    }

    private Specification<PerformRequest> buildCreatedAtBetweenSpecification(LocalDateTime createdAtStart,
                                                                    LocalDateTime createdAtEnd) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get(PerformRequest_.CREATED_AT), createdAtStart, createdAtEnd);
    }

    private Specification<PerformRequest> buildOwnerIdSpecification(Long ownerId) {
        return (root, query, criteriaBuilder) -> ownerId != null
                ? criteriaBuilder.equal(root.get(PerformRequest_.ORDER).get(Order_.OWNER).get(User_.ID), ownerId)
                : null;
    }
}
