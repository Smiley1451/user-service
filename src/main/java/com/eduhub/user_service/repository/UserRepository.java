package com.eduhub.user_service.repository;

import com.eduhub.user_service.model.dto.UserResponseDto;
import com.eduhub.user_service.model.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository extends R2dbcRepository<User, UUID> {
    Mono<Boolean> existByEmail(@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email);

    Mono<Boolean> existByUsername(@NotBlank(message = "Username is required") @Size(min = 3, max = 50) String username);
}
