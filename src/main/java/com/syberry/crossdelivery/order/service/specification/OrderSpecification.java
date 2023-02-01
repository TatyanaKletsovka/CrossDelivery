package com.syberry.crossdelivery.order.service.specification;

import com.syberry.crossdelivery.order.converter.StatusConverter;
import com.syberry.crossdelivery.order.dto.OrderFilterDto;
import com.syberry.crossdelivery.order.entity.Order;
import com.syberry.crossdelivery.order.entity.Order_;
import com.syberry.crossdelivery.user.entity.User_;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderSpecification {

    private final StatusConverter statusConverter;

    public Specification<Order> buildGetAllSpecification(OrderFilterDto filter) {
        return buildBlockedFalseSpecification()
                .and(buildTitleLikeSpecification(filter.getTitle()))
                .and(buildPriceBetweenSpecification(filter.getPriceStart(), filter.getPriceEnd()))
                .and(buildDepartureCountryLikeSpecification(filter.getDepartureCountry()))
                .and(buildDepartureLocationLikeSpecification(filter.getDepartureLocation()))
                .and(buildDestinationCountryLikeSpecification(filter.getDestinationCountry()))
                .and(buildDestinationLocationLikeSpecification(filter.getDestinationLocation()))
                .and(buildCreatedAtBetweenSpecification(filter.getCreatedAtStart().atStartOfDay(),
                        filter.getCreatedAtEnd().plusDays(1).atStartOfDay()));
    }


    public Specification<Order> buildGetAllByUserIdSpecification(OrderFilterDto filter, Long userId) {
        return buildGetAllSpecification(filter)
                .and(buildWhereOwnerIdIsSpecification(userId));
    }

    public Specification<Order> buildGetOwnedSpecification(OrderFilterDto filter, Long ownerId) {
        return buildWhereOwnerIdIsSpecification(ownerId)
                .and(buildGetPersonalSpecification(filter));
    }

    public Specification<Order> buildGetPerformedSpecification(OrderFilterDto filter, Long performerId) {
        return buildWherePerformerIdIsSpecification(performerId)
                .and(buildGetPersonalSpecification(filter));
    }

    public Specification<Order> buildGetRelatedSpecification(Long userId, Long ownId) {
        return (buildWhereOwnerIdIsSpecification(userId)
                .and(buildWherePerformerIdIsSpecification(ownId)))
                .or(buildWhereOwnerIdIsSpecification(ownId))
                .and(buildWherePerformerIdIsSpecification(userId));
    }

    private Specification<Order> buildGetPersonalSpecification(OrderFilterDto filter) {
        return buildStatusSpecification(filter.getStatus())
                .and(buildTitleLikeSpecification(filter.getTitle()))
                .and(buildPriceBetweenSpecification(filter.getPriceStart(), filter.getPriceEnd()))
                .and(buildBlockedSpecification(filter.getBlocked()))
                .and(buildCreatedAtBetweenSpecification(filter.getCreatedAtStart().atStartOfDay(),
                        filter.getCreatedAtEnd().plusDays(1).atStartOfDay()));
    }

    private Specification<Order> buildBlockedFalseSpecification() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isFalse(root.get(Order_.BLOCKED));
    }

    private Specification<Order> buildBlockedSpecification(Boolean blocked) {
        return (root, query, criteriaBuilder) -> blocked != null
                ? criteriaBuilder.equal(root.get(Order_.BLOCKED), blocked)
                : null;
    }

    private Specification<Order> buildTitleLikeSpecification(String title) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        root.get(Order_.TITLE), "%" + title + "%");
    }

    private Specification<Order> buildPriceBetweenSpecification(double priceStart, double priceEnd) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get(Order_.PRICE), priceStart, priceEnd);
    }

    private Specification<Order> buildDepartureCountryLikeSpecification(String departureCountry) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        root.get(Order_.DEPARTURE_COUNTRY), "%" + departureCountry + "%");
    }

    private Specification<Order> buildDepartureLocationLikeSpecification(String departureLocation) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        root.get(Order_.DEPARTURE_LOCATION), "%" + departureLocation + "%");
    }

    private Specification<Order> buildDestinationCountryLikeSpecification(String destinationCountry) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        root.get(Order_.DESTINATION_COUNTRY), "%" + destinationCountry + "%");
    }

    private Specification<Order> buildDestinationLocationLikeSpecification(String destinationLocation) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        root.get(Order_.DESTINATION_LOCATION), "%" + destinationLocation + "%");
    }

    private Specification<Order> buildCreatedAtBetweenSpecification(LocalDateTime createdAtStart,
                                                                   LocalDateTime createdAtEnd) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get(Order_.CREATED_AT), createdAtStart, createdAtEnd);
    }

    private Specification<Order> buildStatusSpecification(String status) {
        return (root, query, criteriaBuilder) -> status != null
                ? criteriaBuilder.equal(root.get(Order_.STATUS), statusConverter.convertToStatus(status))
                : null;
    }

    private Specification<Order> buildWhereOwnerIdIsSpecification(Long ownId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(Order_.OWNER).get(User_.ID), ownId);
    }

    private Specification<Order> buildWherePerformerIdIsSpecification(Long performerId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(Order_.PERFORMER).get(User_.ID), performerId);
    }
}
