package com.syberry.crossdelivery.converter;

import com.syberry.crossdelivery.user.converter.UserConverter;
import com.syberry.crossdelivery.user.dto.SignUpDto;
import com.syberry.crossdelivery.user.dto.UserAdminViewDto;
import com.syberry.crossdelivery.user.dto.UserDto;
import com.syberry.crossdelivery.user.dto.UserWithAccessDto;
import com.syberry.crossdelivery.user.entity.Role;
import com.syberry.crossdelivery.user.entity.User;
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
    SignUpDto signUpDto = new SignUpDto();
    UserWithAccessDto withAccessDto = new UserWithAccessDto();

    @BeforeEach
    public void mock_role_user_repositories() {
        Long id = 1L;
        String username = "username";
        String firstName = "firstName";
        String lastName = "username";
        String email = "user@gmail.com";
        String password = "password";
        String phone = "0123456789";

        user.setId(id);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhoneNumber(phone);
        user.setRoles(Set.of(Role.ADMIN));
        user.setBlocked(false);
        user.setCreatedAt(LocalDateTime.now());

        signUpDto.setUsername(username);
        signUpDto.setFirstName(firstName);
        signUpDto.setLastName(lastName);
        signUpDto.setEmail(email);
        signUpDto.setPassword(password);
        signUpDto.setPhoneNumber(phone);

        withAccessDto.setUsername(username);
        withAccessDto.setFirstName(firstName);
        withAccessDto.setLastName(lastName);
        withAccessDto.setEmail("new@gmail.com");
        withAccessDto.setPhoneNumber(phone);
    }

    @Test
    public void should_SuccessfullyConvertToEntityOneArgument() {
        User createdUser = userConverter.convertToEntity(signUpDto);
        assertEquals(createdUser.getEmail(), "user@gmail.com");
    }

    @Test
    public void should_SuccessfullyConvertToEntityTwoArgument() {
        User createdUser = userConverter.convertToEntity(withAccessDto, user);
        assertEquals(createdUser.getEmail(), "new@gmail.com");
    }

    @Test
    public void should_SuccessfullyConvertToDto() {
        UserDto dto = userConverter.convertToDto(user);
        assertEquals(dto.getId(), 1L);
    }

    @Test
    public void should_SuccessfullyConvertToUserWithAccessDto() {
        UserWithAccessDto dto = userConverter.convertToUserWithAccessDto(user);
        assertEquals(dto.getEmail(), "user@gmail.com");
    }

    @Test
    public void should_SuccessfullyConvertToUserAdminViewDto() {
        UserAdminViewDto dto = userConverter.convertToUserAdminViewDto(user);
        assertEquals(dto.getRoles(), Set.of(Role.ADMIN));
    }
}
