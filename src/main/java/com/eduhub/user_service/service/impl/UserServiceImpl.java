package com.eduhub.user_service.service.impl;

import com.eduhub.user_service.config.UserCreatedEvent;
import com.eduhub.user_service.mapper.UserMapper;
import com.eduhub.user_service.model.dto.UserRegisterRequestDto;
import com.eduhub.user_service.model.dto.UserResponseDto;
import com.eduhub.user_service.model.entity.User;
import com.eduhub.user_service.model.enums.Role;
import com.eduhub.user_service.model.enums.UserStatus;
import com.eduhub.user_service.repository.UserRepository;
import com.eduhub.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;
//import org.springframework.data.relational.core.query.Criteria;

import java.time.LocalDateTime;
import java.util.UUID;
@Slf4j
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

    @KafkaListener(topics = "${kafka.topics.user-created}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeUserCreatedEvent(
            @Payload UserCreatedEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment) {

        try {
            log.info("Processing event for user: {}", event.getUserId());

            processEvent(event)
                    .doOnSuccess(__ -> {
                        log.info("Successfully processed user: {}", event.getUserId());
                        acknowledgment.acknowledge();
                    })
                    .doOnError(e -> {
                        log.error("Error processing user {}: {}", event.getUserId(), e.getMessage());
                        throw new RuntimeException("Failed to process event", e);
                    })
                    .subscribe();

        } catch (Exception e) {
            log.error("Unexpected error processing event", e);
            throw e;
        }
    }

    private Mono<Void> processEvent(UserCreatedEvent event) {
        return validateEvent(event)
                .then(Mono.defer(() -> userRepository.existsById(UUID.fromString(event.getUserId()))))
                .flatMap(exists -> exists ?
                        handleExistingUser(event) :
                        createNewUser(event))
                .onErrorResume(e -> {
                    log.error("Failed to process event for user {}: {}", event.getUserId(), e.getMessage());
                    return Mono.error(new RuntimeException("Event processing failed", e));
                });
    }

    private Mono<Void> validateEvent(UserCreatedEvent event) {
        if (event.getUserId() == null || event.getEmail() == null) {
            return Mono.error(new IllegalArgumentException("Invalid event: missing required fields"));
        }
        return Mono.empty();
    }

    private Mono<Void> handleExistingUser(UserCreatedEvent event) {
        return template.update(User.class)
                .matching(query(where("id").is(UUID.fromString(event.getUserId()))))
                .apply(Update.update("email", event.getEmail())
                        .set("username", event.getUserName())
                        .set("role", event.getRole())
                        .set("updated_at", LocalDateTime.now()))
                .doOnSuccess(updated ->
                        log.info("Updated user {} with new details", event.getUserId()))
                .then();
    }



    private Mono<Void> createNewUser(UserCreatedEvent event) {
        return Mono.fromCallable(() -> User.builder()
                        .id(UUID.fromString(event.getUserId()))
                        .username(event.getUserName())
                        .email(event.getEmail())
                        .role(event.getRole())
                        .status(UserStatus.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .emailVerified(true)
                        .build())
                .flatMap(user -> template.insert(user))
                .doOnSuccess(savedUser ->
                        log.info("Created new user from event: {}", savedUser.getId()))
                .then();
    }
}




