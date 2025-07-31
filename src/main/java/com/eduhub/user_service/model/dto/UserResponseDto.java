package com.eduhub.user_service.model.dto;

import com.eduhub.user_service.model.enums.Role;
import com.eduhub.user_service.model.enums.UserStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private UUID id;
    private String fullName;
    private String username;
    private String email;
    private String phone;
    private LocalDate dob;
    private Role role;
    private UserStatus status;
    private String profileImage;
    private boolean emailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
