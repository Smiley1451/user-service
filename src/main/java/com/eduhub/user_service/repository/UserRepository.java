package com.eduhub.user_service.repository;

import com.eduhub.user_service.model.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository extends ReactiveCrudRepository<User, UUID> {
    Mono<Boolean> existsByEmail(@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email);

    Mono<Boolean> existsByUsername(@NotBlank(message = "Username is required") @Size(min = 3, max = 50) String username);
}
