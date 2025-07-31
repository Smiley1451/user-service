package com.eduhub.user_service.service.impl;
import com.eduhub.user_service.mapper.UserMapper;
import com.eduhub.user_service.model.dto.UserRegisterRequestDto;
import com.eduhub.user_service.model.dto.UserResponseDto;
import com.eduhub.user_service.model.entity.User;
import com.eduhub.user_service.model.enums.UserStatus;
import com.eduhub.user_service.repository.UserRepository;
import com.eduhub.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    @Override
    public Mono<UserResponseDto> register(UserRegisterRequestDto request) {
        return userRepository.existByEmail(request.getEmail())
                .flatMap(response -> {
                    if(response){
                        return Mono.error(new RuntimeException("Email already in use"));
                    }
                    return userRepository.existByUsername(request.getUsername());
                })
                .flatMap(response ->{
                    if(response){
                        return Mono.error(new RuntimeException("Username already in use"));
                    }
                 User user= userMapper.toEntity(request);
                    user.setStatus(UserStatus.ACTIVE);
                    return userRepository.save(user);
                })
                .map(userMapper::toResponse);



    }
}
