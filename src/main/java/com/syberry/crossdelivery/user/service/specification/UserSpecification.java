package com.syberry.crossdelivery.user.service.specification;

import com.syberry.crossdelivery.user.dto.UserFilterDto;
import com.syberry.crossdelivery.user.entity.User;
import com.syberry.crossdelivery.user.entity.User_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserSpecification {

    public Specification<User> buildGetAllSpecification(UserFilterDto filter) {
        return buildWhereDisabledAtNullSpecification()
                .and(buildWhereUserIdIsSpecification(filter.getId()))
                .and(buildUsernameLikeSpecification(filter.getUsername()))
                .and(buildFirstNameLikeSpecification(filter.getFirstName()))
                .and(buildLastNameLikeSpecification(filter.getLastName()))
                .and(buildEmailLikeSpecification(filter.getEmail()))
                .and(buildPhoneNumberLikeSpecification(filter.getPhoneNumber()))
                .and(buildWhereBlockedIsSpecification(filter.getBlocked()))
                .and(buildCreatedAtBetweenSpecification(filter.getCreatedAtStart().atStartOfDay(),
                        filter.getCreatedAtEnd().plusDays(1).atStartOfDay()));
    }

    private Specification<User> buildWhereDisabledAtNullSpecification() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get(User_.DISABLED_AT));
    }

    private Specification<User> buildWhereUserIdIsSpecification(Long id) {
        return (root, query, criteriaBuilder) -> id != null
                ? criteriaBuilder.equal(root.get(User_.ID), id)
                : null;
    }

    private Specification<User> buildUsernameLikeSpecification(String username) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                    root.get(User_.USERNAME), "%" + username + "%");
    }

    private Specification<User> buildFirstNameLikeSpecification(String firstName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        root.get(User_.FIRST_NAME), "%" + firstName + "%");
    }

    private Specification<User> buildLastNameLikeSpecification(String lastName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        root.get(User_.LAST_NAME), "%" + lastName + "%");
    }

    private Specification<User> buildEmailLikeSpecification(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        root.get(User_.EMAIL), "%" + email + "%");
    }

    private Specification<User> buildPhoneNumberLikeSpecification(String phoneNumber) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        root.get(User_.PHONE_NUMBER), "%" + phoneNumber + "%");
    }

    public Specification<User> buildWhereBlockedIsSpecification(Boolean blocked) {
        return (root, query, criteriaBuilder) -> blocked != null
                ? criteriaBuilder.equal(root.get(User_.BLOCKED), blocked)
                : null;
    }

    private Specification<User> buildCreatedAtBetweenSpecification(LocalDateTime createdAtStart,
                                                                   LocalDateTime createdAtEnd) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get(User_.CREATED_AT), createdAtStart, createdAtEnd);
    }
}
