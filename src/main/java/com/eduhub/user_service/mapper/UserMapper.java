package com.eduhub.user_service.mapper;

import com.eduhub.user_service.model.dto.*;
import com.eduhub.user_service.model.entity.User;
import com.eduhub.user_service.model.enums.Role;
import com.eduhub.user_service.model.enums.UserStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class UserMapper {

    public User toEntity(UserRegisterRequestDto dto) {
        return User.builder()
                .fullName(dto.getFullName())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .dob(dto.getDob())
                .role(dto.getRole() != null ? dto.getRole() : Role.STUDENT)
                .status(UserStatus.ACTIVE)
                .emailVerified(dto.isEmailVerified())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public UserResponseDto toResponse(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .dob(user.getDob())
                .role(user.getRole())
                .status(user.getStatus())
                .profileImage(user.getProfileImage())
                .emailVerified(user.isEmailVerified())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public void updateEntityFromDto(UserUpdateRequestDto dto, User user) {
        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setDob(dto.getDob());
        user.setProfileImage(dto.getProfileImage());
        user.setUpdatedAt(LocalDateTime.now());
    }
}