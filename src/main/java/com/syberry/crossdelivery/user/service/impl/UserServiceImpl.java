package com.syberry.crossdelivery.user.service.impl;

import com.syberry.crossdelivery.authorization.repository.RefreshTokenRepository;
import com.syberry.crossdelivery.exception.AccessException;
import com.syberry.crossdelivery.exception.UpdateException;
import com.syberry.crossdelivery.exception.ValidationException;
import com.syberry.crossdelivery.order.entity.Order;
import com.syberry.crossdelivery.order.repository.OrderRepository;
import com.syberry.crossdelivery.order.service.specification.OrderSpecification;
import com.syberry.crossdelivery.user.converter.RoleConverter;
import com.syberry.crossdelivery.user.converter.UserConverter;
import com.syberry.crossdelivery.user.dto.SignUpDto;
import com.syberry.crossdelivery.user.dto.UpdatePasswordDto;
import com.syberry.crossdelivery.user.dto.UserAdminViewDto;
import com.syberry.crossdelivery.user.dto.UserDto;
import com.syberry.crossdelivery.user.dto.UserFilterDto;
import com.syberry.crossdelivery.user.dto.UserWithAccessDto;
import com.syberry.crossdelivery.user.entity.Role;
import com.syberry.crossdelivery.user.entity.User;
import com.syberry.crossdelivery.user.repository.UserRepository;
import com.syberry.crossdelivery.user.service.UserService;
import com.syberry.crossdelivery.user.service.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.syberry.crossdelivery.authorization.util.SecurityContextUtil.getUserDetails;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository tokenRepository;
    private final OrderRepository orderRepository;
    private final UserConverter userConverter;
    private final RoleConverter roleConverter;
    private final UserSpecification userSpecification;
    private final OrderSpecification orderSpecification;
    private final PasswordEncoder encoder;

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<UserAdminViewDto> getAllUsers(UserFilterDto filter, Pageable pageable) {
        return userRepository.findAll(userSpecification.buildGetAllSpecification(filter), pageable)
                .map(userConverter::convertToUserAdminViewDto);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public UserDto getUserById(Long id) {
        List<Order> orders = orderRepository.findAll(
                orderSpecification.buildGetRelatedSpecification(id, getUserDetails().getId()));
        if (!orders.isEmpty() || id.equals(getUserDetails().getId())) {
            return userConverter.convertToUserWithAccessDto(userRepository.findByIdIfExistsAndBlockedFalse(id));
        } else {
            return userConverter.convertToDto(userRepository.findByIdIfExistsAndBlockedFalse(id));
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public UserWithAccessDto getUserProfile() {
        Long id = getUserDetails().getId();
        return userConverter.convertToUserWithAccessDto(userRepository.findByIdIfExistsAndBlockedFalse(id));
    }

    @Override
    public UserWithAccessDto createProfile(SignUpDto dto) {
        validateFields(dto.getUsername(), dto.getEmail(), null);
        User user = userConverter.convertToEntity(dto);
        user.setPassword(encoder.encode(dto.getPassword()));
        return userConverter.convertToUserWithAccessDto(userRepository.save(user));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public UserWithAccessDto updateProfile(UserWithAccessDto dto) {
        Long id = getUserDetails().getId();
        validateFields(dto.getUsername(), dto.getEmail(), id);
        User userDb = userRepository.findByIdIfExistsAndBlockedFalse(id);
        User user = userConverter.convertToEntity(dto, userDb);
        return userConverter.convertToUserWithAccessDto(user);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void disableUserProfile() {
        Long id = getUserDetails().getId();
        User user = userRepository.findByIdIfExistsAndBlockedFalse(id);
        user.setDisabledAt(LocalDateTime.now());
        tokenRepository.deleteById(tokenRepository.findByUserIdIfExists(id).getId());
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void updatePassword(UpdatePasswordDto dto) {
        User user = userRepository.findByIdIfExistsAndBlockedFalse(getUserDetails().getId());
        if (!encoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new UpdateException("Invalid current password");
        }
        user.setPassword(encoder.encode(dto.getNewPassword()));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public UserAdminViewDto reverseBlocked(Long id) {
        if (id.equals(getUserDetails().getId())) {
            throw new AccessException("Blocking can't be changed in personal profile");
        }
        User user = userRepository.findByIdIfExists(id);
        user.setBlocked(!user.isBlocked());
        return userConverter.convertToUserAdminViewDto(user);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public UserAdminViewDto addRoleToUser(Long id, String role) {
        if (id.equals(getUserDetails().getId())) {
            throw new AccessException("Role can't be added to personal profile");
        }
        User user = userRepository.findByIdIfExists(id);
        user.getRoles().add(roleConverter.convertToRole(role));
        return userConverter.convertToUserAdminViewDto(user);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public UserAdminViewDto removeRoleFromUser(Long id, String role) {
        if (id.equals(getUserDetails().getId())) {
            throw new AccessException("Role can't be deleted from personal profile");
        }
        User user = userRepository.findByIdIfExists(id);
        Set<Role> roles = user.getRoles();
        if (roles.size() == 1) {
            throw new UpdateException("Last role can't be deleted");
        }
        user.getRoles().remove(roleConverter.convertToRole(role));
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
