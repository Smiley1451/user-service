package com.eduhub.user_service.config;

import com.eduhub.user_service.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedEvent {
    private String userId;
    private String userName;
    private String email;
    private Role role;
    private Instant createdAt;
    private String source;
}