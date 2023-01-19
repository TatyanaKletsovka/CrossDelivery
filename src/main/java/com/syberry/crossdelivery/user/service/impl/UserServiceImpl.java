package com.syberry.crossdelivery.user.service.impl;

import com.syberry.crossdelivery.exception.ValidationException;
import com.syberry.crossdelivery.user.converter.UserConverter;
import com.syberry.crossdelivery.user.dto.SignUpDto;
import com.syberry.crossdelivery.user.dto.UserAdminViewDto;
import com.syberry.crossdelivery.user.dto.UserDto;
import com.syberry.crossdelivery.user.dto.UserFilterDto;
import com.syberry.crossdelivery.user.dto.UserWithAccessDto;
import com.syberry.crossdelivery.user.entity.User;
import com.syberry.crossdelivery.user.repository.UserRepository;
import com.syberry.crossdelivery.user.service.UserService;
import com.syberry.crossdelivery.user.service.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final UserSpecification specification;

    @Override
    public Page<UserAdminViewDto> getAllUsers(UserFilterDto filter, Pageable pageable) {
        return userRepository.findAll(specification.buildGetAllSpecification(filter), pageable)
                .map(userConverter::convertToUserAdminViewDto);
    }

    @Override
    public UserDto getUserById(Long id) {
        return userConverter.convertToDto(userRepository.findByIdIfExistsAndIsBlockedFalse(id));
    }

    @Override
    public UserWithAccessDto createUser(SignUpDto dto) {
//        password encoding will be implemented in feature 'Authentication and Authorization',
//        because needed security dependency
        validateFields(dto.getUsername(), dto.getEmail(), null);
        User user = userConverter.convertToEntity(dto);
        return userConverter.convertToUserWithAccessDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserWithAccessDto updateProfile(UserWithAccessDto dto) {
        validateFields(dto.getUsername(), dto.getEmail(), dto.getId());
        User userDb = userRepository.findByIdIfExistsAndIsBlockedFalse(dto.getId());
        User user = userConverter.convertToEntity(dto, userDb);
        return userConverter.convertToUserWithAccessDto(user);
    }

    @Override
    @Transactional
    public void disableUserProfile(Long id) {
        User user = userRepository.findByIdIfExistsAndIsBlockedFalse(id);
        user.setDisabledAt(LocalDateTime.now());
    }

    @Override
    @Transactional
    public UserAdminViewDto reverseIsBlocked(Long id) {
        User user = userRepository.findByIdIfExists(id);
        user.setBlocked(!user.isBlocked());
        return userConverter.convertToUserAdminViewDto(user);
    }

    private void validateFields(String username, String email, Long id) {
        Optional<User> optionalByUsername = userRepository.findByUsername(username);
        if (optionalByUsername.isPresent() && !Objects.equals(optionalByUsername.get().getId(), id)) {
            throw new ValidationException("This username is already taken. Try another one");
        }
        Optional<User> optionalByEmail = userRepository.findByEmail(email);
        if (optionalByEmail.isPresent() && !Objects.equals(optionalByEmail.get().getId(), id)) {
            throw new ValidationException("This email is already taken. Try another one");
        }
    }
}
