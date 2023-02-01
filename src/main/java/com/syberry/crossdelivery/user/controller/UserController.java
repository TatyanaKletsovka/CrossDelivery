package com.syberry.crossdelivery.user.controller;

import com.syberry.crossdelivery.user.dto.SignUpDto;
import com.syberry.crossdelivery.user.dto.UpdatePasswordDto;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public Page<UserAdminViewDto> getAllUsers(UserFilterDto filter, Pageable pageable) {
        log.info("GET-request: getting all users");
        return userService.getAllUsers(filter, pageable);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") Long id) {
        log.info("GET-request: getting user with id: {}", id);
        return userService.getUserById(id);
    }

    @GetMapping("/profile")
    public UserWithAccessDto getUserProfile() {
        log.info("GET-request: getting user profile");
        return userService.getUserProfile();
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public UserWithAccessDto createProfile(@Valid @RequestBody SignUpDto signUpDto) {
        log.info("POST-request: creating user profile");
        return userService.createProfile(signUpDto);
    }

    @PutMapping
    public UserWithAccessDto updateProfile(@Valid @RequestBody UserWithAccessDto userWithAccessDto) {
        log.info("PUT-request: updating user profile");
        return userService.updateProfile(userWithAccessDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disableProfile() {
        log.info("DELETE-request: deleting user profile");
        userService.disableUserProfile();
    }

    @PutMapping("/update-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto) {
        log.info("POST-request: update password");
        userService.updatePassword(updatePasswordDto);
    }

    @PutMapping("/blocked/{id}")
    public UserAdminViewDto reverseBlockedUserById(@PathVariable("id") Long id) {
        log.info("PUT-request: reverse the user's with id: {} blocked status", id);
        return userService.reverseBlocked(id);
    }

    @PutMapping("/{id}/add-role")
    public UserAdminViewDto addRoleToUser(@PathVariable("id") Long id, @NotNull @RequestParam String role) {
        log.info("PUT-request: add role: {} to user with id: {}", role, id);
        return userService.addRoleToUser(id, role);
    }

    @PutMapping("/{id}/remove-role")
    public UserAdminViewDto removeRoleFromUser(@PathVariable("id") Long id, @NotNull @RequestParam String role) {
        log.info("PUT-request: remove role: {} from user with id: {}", role, id);
        return userService.removeRoleFromUser(id, role);
    }
}
