package com.syberry.crossdelivery.service;

import com.syberry.crossdelivery.authorization.security.UserDetailsImpl;
import com.syberry.crossdelivery.exception.EntityNotFoundException;
import com.syberry.crossdelivery.exception.UpdateException;
import com.syberry.crossdelivery.exception.ValidationException;
import com.syberry.crossdelivery.user.converter.RoleConverter;
import com.syberry.crossdelivery.user.converter.UserConverter;
import com.syberry.crossdelivery.user.dto.SignUpDto;
import com.syberry.crossdelivery.user.dto.UpdatePasswordDto;
import com.syberry.crossdelivery.user.dto.UserAdminViewDto;
import com.syberry.crossdelivery.user.dto.UserDto;
import com.syberry.crossdelivery.user.dto.UserWithAccessDto;
import com.syberry.crossdelivery.user.entity.Role;
import com.syberry.crossdelivery.user.entity.User;
import com.syberry.crossdelivery.user.repository.UserRepository;
import com.syberry.crossdelivery.user.service.impl.UserServiceImpl;
import com.syberry.crossdelivery.user.service.specification.UserSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserConverter userConverter;
    @Mock
    private RoleConverter roleConverter;
    @Mock
    private UserRepository userRepository;
    @Mock
    UserSpecification specification;
    @Mock
    private PasswordEncoder encoder;

    User user = new User();
    SignUpDto signUpDto = new SignUpDto();
    UserDto userDto = new UserDto();
    UserWithAccessDto withAccessDto = new UserWithAccessDto();
    UserAdminViewDto adminViewDto = new UserAdminViewDto();

    @BeforeEach
    public void auth() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal())
                .thenReturn(new UserDetailsImpl(2L, null, "user@gmail.com",
                        List.of(new SimpleGrantedAuthority(Role.ADMIN.name()))));
        when(roleConverter.convertToRole(any())).thenReturn(Role.USER);
    }

    @BeforeEach
    public void mockUsers() {
        Long id = 1L;
        String username = "username";
        String firstName = "firstName";
        String lastName = "username";
        String email = "user@gmail.com";
        String password = "password";
        String phone = "0123456789";
        LocalDateTime dateTime = LocalDateTime.now();
        Set<Role> roles = Set.of(Role.ADMIN);

        user.setId(id);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhoneNumber(phone);
        user.setRoles(roles);
        user.setBlocked(false);
        user.setCreatedAt(dateTime);

        signUpDto.setUsername(username);
        signUpDto.setFirstName(firstName);
        signUpDto.setLastName(lastName);
        signUpDto.setEmail(email);
        signUpDto.setPassword(password);
        signUpDto.setPhoneNumber(phone);

        userDto.setId(id);
        userDto.setUsername(username);
        userDto.setCreatedAt(dateTime);

        withAccessDto.setId(id);
        withAccessDto.setUsername(username);
        withAccessDto.setFirstName(firstName);
        withAccessDto.setLastName(lastName);
        withAccessDto.setEmail(email);
        withAccessDto.setPhoneNumber(phone);
        withAccessDto.setCreatedAt(dateTime);

        adminViewDto.setId(id);
        adminViewDto.setUsername(username);
        adminViewDto.setFirstName(firstName);
        adminViewDto.setLastName(lastName);
        adminViewDto.setEmail(email);
        adminViewDto.setPhoneNumber(phone);
        adminViewDto.setRoles(roles);
        adminViewDto.setBlocked(true);
        adminViewDto.setCreatedAt(dateTime);
    }

    @Test
    void should_SuccessfullyReturnAllUsers() {
        when(userRepository.findAll(specification.buildGetAllSpecification(any()), PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(List.of()));
        assertEquals(userService.getAllUsers(any(), PageRequest.of(0, 20)), new PageImpl<>(List.of()));
    }

    @Test
    void should_SuccessfullyGetUserById() {
        when(userRepository.findByIdIfExistsAndIsBlockedFalse(any())).thenReturn(user);
        when(userConverter.convertToDto(any())).thenReturn(userDto);
        assertEquals(userService.getUserById(any()), userDto);
    }

    @Test
    void should_ThrowError_WhenGettingByIdNoneExistingUser() {
        when(userRepository.findByIdIfExistsAndIsBlockedFalse(-1L)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(-1L));
    }

    @Test
    void should_SuccessfullyGetUserProfile() {
        when(userRepository.findByIdIfExistsAndIsBlockedFalse(any())).thenReturn(user);
        when(userConverter.convertToDto(any())).thenReturn(userDto);
        assertEquals(userService.getUserById(any()), userDto);
    }

    @Test
    void should_SuccessfullyCreateUser() {
        when(encoder.encode(any())).thenReturn("encoded");
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userConverter.convertToEntity(any(SignUpDto.class))).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);
        when(userConverter.convertToUserWithAccessDto(any())).thenReturn(withAccessDto);
        assertEquals(userService.createProfile(signUpDto), withAccessDto);
    }

    @Test
    void should_ThrowError_When_CreatingUserWithExistingUsername() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        assertThrows(ValidationException.class, () -> userService.createProfile(signUpDto));
    }

    @Test
    void should_ThrowError_When_CreatingUserWithExistingEmail() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        assertThrows(ValidationException.class, () -> userService.createProfile(signUpDto));
    }

    @Test
    void should_SuccessfullyUpdateProfile() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.findByIdIfExistsAndIsBlockedFalse(any())).thenReturn(new User());
        when(userConverter.convertToEntity(any(), any())).thenReturn(user);
        when(userConverter.convertToUserWithAccessDto(any())).thenReturn(withAccessDto);
        assertEquals(userService.updateProfile(withAccessDto), withAccessDto);
    }

    @Test
    void should_ThrowError_When_UpdatingUserWithExistingUsername() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        assertThrows(ValidationException.class, () -> userService.updateProfile(withAccessDto));
    }

    @Test
    void should_ThrowError_When_UpdatingUserWithExistingEmail() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        assertThrows(ValidationException.class, () -> userService.updateProfile(withAccessDto));
    }

    @Test
    void should_SuccessfullyDisableUser() {
        when(userRepository.findByIdIfExistsAndIsBlockedFalse(any())).thenReturn(user);
        userService.disableUserProfile();
        assertNull(userService.getUserProfile());
    }

    @Test
    void should_SuccessfullyUpdatePassword() {
        when(userRepository.findByIdIfExistsAndIsBlockedFalse(any())).thenReturn(user);
        when(encoder.matches(any(), any())).thenReturn(true);
        when(encoder.encode(any())).thenReturn("newPassword");
        userService.updatePassword(new UpdatePasswordDto("password", "newPassword"));
        assertEquals(user.getPassword(), "newPassword");
    }

    @Test
    void should_ThrowError_When_UpdatingPasswordWithInvalidPassword() {
        when(userRepository.findByIdIfExistsAndIsBlockedFalse(any())).thenReturn(user);
        when(encoder.matches(any(), any())).thenReturn(false);
        assertThrows(UpdateException.class, () -> userService.updatePassword(
                new UpdatePasswordDto("password", "newPassword")));
    }

    @Test
    void should_SuccessfullyReverseIsBlockedUser() {
        when(userRepository.findByIdIfExists(any())).thenReturn(user);
        when(userConverter.convertToUserAdminViewDto(any())).thenReturn(adminViewDto);
        assertEquals(userService.reverseIsBlocked(1L), adminViewDto);
    }
}
