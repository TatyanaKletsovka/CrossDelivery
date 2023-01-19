package com.syberry.crossdelivery.user.controller;

import com.syberry.crossdelivery.user.dto.SignUpDto;
import com.syberry.crossdelivery.user.dto.UserAdminViewDto;
import com.syberry.crossdelivery.user.dto.UserDto;
import com.syberry.crossdelivery.user.dto.UserFilterDto;
import com.syberry.crossdelivery.user.dto.UserWithAccessDto;
import com.syberry.crossdelivery.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    Page<UserAdminViewDto> getAllUsers(UserFilterDto filter, Pageable pageable) {
        log.info("GET-request: getting all users");
        return userService.getAllUsers(filter, pageable);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") Long id) {
        log.info("GET-request: getting user with id: {}", id);
        return userService.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserWithAccessDto createUser(@Valid @RequestBody SignUpDto signUpDto) {
        log.info("POST-request: creating user");
        return userService.createUser(signUpDto);
    }

    @PutMapping("/{id}")
    public UserWithAccessDto updateProfile(@PathVariable("id") Long id,
                                           @Valid @RequestBody UserWithAccessDto userWithAccessDto) {
        log.info("PUT-request: updating user profile");
//      The id will be taken out of the context after adding the authorization
        userWithAccessDto.setId(id);
        return userService.updateProfile(userWithAccessDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disableProfile(@PathVariable("id") Long id) {
        log.info("DELETE-request: deleting user profile with id: {}", id);
//      The id will be taken out of the context after adding the authorization
        userService.disableUserProfile(id);
    }

    @PutMapping("/blocked/{id}")
    public UserAdminViewDto reverseIsBlockedUserById(@PathVariable("id") Long id) {
        return userService.reverseIsBlocked(id);
    }

}
