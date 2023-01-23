package com.syberry.crossdelivery.user.service.specification;

import com.syberry.crossdelivery.user.entity.Role;
import com.syberry.crossdelivery.user.entity.User_;
import com.syberry.crossdelivery.user.dto.UserFilterDto;
import com.syberry.crossdelivery.user.entity.User;
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
                .and(buildWhereRoleIsSpecification(filter.getRole()))
                .and(buildWhereIsBlockedIsSpecification(filter.getIsBlocked()))
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

    private Specification<User> buildWhereRoleIsSpecification(Role role) {
        return (root, query, criteriaBuilder) -> role != null
                ? criteriaBuilder.equal(root.get(User_.ROLE), role)
                : null;
    }

    public Specification<User> buildWhereIsBlockedIsSpecification(Boolean isBlocked) {
        return (root, query, criteriaBuilder) -> isBlocked != null
                ? criteriaBuilder.equal(root.get(User_.IS_BLOCKED), isBlocked)
                : null;
    }

    private Specification<User> buildCreatedAtBetweenSpecification(LocalDateTime createdAtStart,
                                                                   LocalDateTime createdAtEnd) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get(User_.CREATED_AT), createdAtStart, createdAtEnd);
    }

}
