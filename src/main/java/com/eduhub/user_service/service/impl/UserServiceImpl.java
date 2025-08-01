package com.eduhub.user_service.service.impl;

import com.eduhub.user_service.mapper.UserMapper;
import com.eduhub.user_service.model.dto.UserRegisterRequestDto;
import com.eduhub.user_service.model.dto.UserResponseDto;
import com.eduhub.user_service.model.entity.User;
import com.eduhub.user_service.repository.UserRepository;
import com.eduhub.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final R2dbcEntityTemplate template;

    @Override
    public Mono<UserResponseDto> register(UserRegisterRequestDto request) {
        return userRepository.existsByEmail(request.getEmail())
                .flatMap(emailExists -> {
                    if (emailExists) {
                        return Mono.error(new RuntimeException("Email already in use"));
                    }
                    return userRepository.existsByUsername(request.getUsername());
                })
                .flatMap(usernameExists -> {
                    if (usernameExists) {
                        return Mono.error(new RuntimeException("Username already in use"));
                    }

                    User user = userMapper.toEntity(request);
                    user.setId(UUID.randomUUID());
                    user.setCreatedAt(LocalDateTime.now());


                    return template.insert(User.class)
                            .using(user)
                            .flatMap(savedUser -> Mono.just(userMapper.toResponse(savedUser)));
                });
    }
}