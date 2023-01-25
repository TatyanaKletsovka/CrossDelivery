package com.syberry.crossdelivery.user.service;

import com.syberry.crossdelivery.user.dto.SignUpDto;
import com.syberry.crossdelivery.user.dto.UpdatePasswordDto;
import com.syberry.crossdelivery.user.dto.UserAdminViewDto;
import com.syberry.crossdelivery.user.dto.UserDto;
import com.syberry.crossdelivery.user.dto.UserFilterDto;
import com.syberry.crossdelivery.user.dto.UserWithAccessDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserAdminViewDto> getAllUsers(UserFilterDto filter, Pageable pageable);

    UserDto getUserById(Long id);

    UserWithAccessDto getUserProfile();

    UserWithAccessDto createProfile(SignUpDto dto);

    UserWithAccessDto updateProfile(UserWithAccessDto dto);

    void disableUserProfile();

    void updatePassword(UpdatePasswordDto dto);

    UserAdminViewDto reverseIsBlocked(Long id);

    UserAdminViewDto addRoleToUser(Long id, String role);

    UserAdminViewDto removeRoleFromUser(Long id, String role);
}
