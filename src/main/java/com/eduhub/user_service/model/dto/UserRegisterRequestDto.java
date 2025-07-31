package com.eduhub.user_service.model.dto;

import com.eduhub.user_service.model.enums.Role;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequestDto {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone must start with 6-9 and be 10 digits")
    private String phone;

    private LocalDate dob;
    private boolean emailVerified;


    @NotNull(message = "Role is required")
    private Role role;
}
