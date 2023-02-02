package com.syberry.crossdelivery.authorization.repository;

import com.syberry.crossdelivery.authorization.entity.RefreshToken;
import com.syberry.crossdelivery.exception.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserId(Long id);

    void deleteByUserId(Long id);

    default RefreshToken findByUserIdIfExists(Long id) {
        return findByUserId(id).orElseThrow(()
                -> new EntityNotFoundException(String.format("Token for user with id: %s is not found", id)));
    }
}
