package com.eduhub.user_service.service;

import com.eduhub.user_service.model.dto.UserRegisterRequestDto;
import com.eduhub.user_service.model.dto.UserResponseDto;
import reactor.core.publisher.Mono;

public interface UserService {
    public Mono<UserResponseDto> register(UserRegisterRequestDto request);
}
