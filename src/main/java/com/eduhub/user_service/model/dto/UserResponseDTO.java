package com.eduhub.user_service.model.dto;

import com.eduhub.user_service.model.enums.Role;
import com.eduhub.user_service.model.enums.UserStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private UUID id;

    private String fullName;
    private String username;
    private String email;
    private String phone;
    private LocalDate dob;

    private String profileImage;
    private Role role;
    private UserStatus status;
    private boolean emailVerified;
}
