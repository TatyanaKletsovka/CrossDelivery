package com.syberry.crossdelivery.user.repository;

import com.syberry.crossdelivery.exception.EntityNotFoundException;
import com.syberry.crossdelivery.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndBlockedFalseAndDisabledAtNull(Long id);

    Optional<User> findByEmailAndBlockedFalseAndDisabledAtNull(String username);


    Optional<User> findByIdAndDisabledAtNull(Long id);

    default User findByIdIfExistsAndBlockedFalse(Long id) {
        return findByIdAndBlockedFalseAndDisabledAtNull(id).orElseThrow(()
                -> new EntityNotFoundException(String.format("User with id: %s is not found", id)));
    }

    default User findByIdIfExists(Long id) {
        return findByIdAndDisabledAtNull(id).orElseThrow(()
                -> new EntityNotFoundException(String.format("User with id: %s is not found", id)));
    }

    default User findByEmailIfExistsAndBlockedFalse(String email) {
        return findByEmailAndBlockedFalseAndDisabledAtNull(email).orElseThrow(()
                -> new EntityNotFoundException(String.format("User with email: %s is not found", email)));
    }
}
