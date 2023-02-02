package com.syberry.crossdelivery.converter;

import com.syberry.crossdelivery.user.converter.UserConverter;
import com.syberry.crossdelivery.user.dto.SignUpDto;
import com.syberry.crossdelivery.user.dto.UserAdminViewDto;
import com.syberry.crossdelivery.user.dto.UserDto;
import com.syberry.crossdelivery.user.dto.UserWithAccessDto;
import com.syberry.crossdelivery.user.entity.Role;
import com.syberry.crossdelivery.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserConverterTest {

    @InjectMocks
    UserConverter userConverter;
    User user = new User();
    User newUser = new User();
    SignUpDto signUpDto = new SignUpDto();
    UserDto userDto = new UserDto();
    UserWithAccessDto withAccessDto = new UserWithAccessDto();
    UserAdminViewDto adminViewDto = new UserAdminViewDto();

    @BeforeEach
    public void mock_role_user_repositories() {
        Long id = 1L;
        String username = "username";
        String firstName = "firstName";
        String lastName = "username";
        String email = "user@gmail.com";
        String password = "password";
        String phone = "0123456789";
        LocalDateTime createdAt = LocalDateTime.now();
        Set<Role> roles = Set.of(Role.ADMIN);
        boolean blocked = false;

        user.setId(id);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhoneNumber(phone);
        user.setCreatedAt(createdAt);
        user.setRoles(roles);
        user.setBlocked(blocked);

        newUser.setUsername(username);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setPhoneNumber(phone);

        signUpDto.setUsername(username);
        signUpDto.setFirstName(firstName);
        signUpDto.setLastName(lastName);
        signUpDto.setEmail(email);
        signUpDto.setPassword(password);
        signUpDto.setPhoneNumber(phone);

        userDto.setId(id);
        userDto.setUsername(username);
        userDto.setCreatedAt(createdAt);

        withAccessDto.setId(id);
        withAccessDto.setUsername(username);
        withAccessDto.setFirstName(firstName);
        withAccessDto.setLastName(lastName);
        withAccessDto.setEmail(email);
        withAccessDto.setPhoneNumber(phone);
        withAccessDto.setCreatedAt(createdAt);

        adminViewDto.setId(id);
        adminViewDto.setUsername(username);
        adminViewDto.setFirstName(firstName);
        adminViewDto.setLastName(lastName);
        adminViewDto.setEmail(email);
        adminViewDto.setPhoneNumber(phone);
        adminViewDto.setCreatedAt(createdAt);
        adminViewDto.setCreatedAt(createdAt);
        adminViewDto.setCreatedAt(createdAt);
        adminViewDto.setRoles(roles);
        adminViewDto.setBlocked(blocked);
    }

    @Test
    public void should_SuccessfullyConvertToEntityOneArgument() {
        User createdUser = userConverter.convertToEntity(signUpDto);
        Assertions.assertThat(createdUser)
                .usingRecursiveComparison()
                .ignoringFields("createdAt")
                .isEqualTo(newUser);
    }

    @Test
    public void should_SuccessfullyConvertToEntityTwoArgument() {
        User createdUser = userConverter.convertToEntity(withAccessDto, user);
        assertEquals(createdUser, user);
    }

    @Test
    public void should_SuccessfullyConvertToDto() {
        UserDto dto = userConverter.convertToDto(user);
        assertEquals(dto, userDto);
    }

    @Test
    public void should_SuccessfullyConvertToUserWithAccessDto() {
        UserWithAccessDto dto = userConverter.convertToUserWithAccessDto(user);
        assertEquals(dto, withAccessDto);
    }

    @Test
    public void should_SuccessfullyConvertToUserAdminViewDto() {
        UserAdminViewDto dto = userConverter.convertToUserAdminViewDto(user);
        assertEquals(dto, adminViewDto);
    }
}
