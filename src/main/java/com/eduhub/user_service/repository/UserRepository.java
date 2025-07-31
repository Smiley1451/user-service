package com.eduhub.user_service.repository;

import com.eduhub.user_service.model.entity.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface UserRepository extends R2dbcRepository<User, UUID> {
}
