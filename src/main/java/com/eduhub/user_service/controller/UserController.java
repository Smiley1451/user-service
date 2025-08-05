package com.eduhub.user_service.controller;

import com.eduhub.user_service.model.dto.UserRegisterRequestDto;
import com.eduhub.user_service.model.dto.UserResponseDto;
import com.eduhub.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

        @PostMapping("/register")
    public Mono<ResponseEntity<UserResponseDto>> register(@Valid @RequestBody UserRegisterRequestDto requestDto) {
        return userService.register(requestDto)
                .map(userResponse -> ResponseEntity.status(HttpStatus.CREATED).body(userResponse));
    }


}
