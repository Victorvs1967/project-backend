package com.vvs.backend.service;

import org.springframework.stereotype.Service;

import com.vvs.backend.dto.UserDto;
import com.vvs.backend.mapper.AppMapper;
import com.vvs.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AppMapper mapper;

    @Override
    public Flux<UserDto> getUsers() {
        return userRepository.findAll()
            .switchIfEmpty(Mono.error(new Exception("Users not found...")))
            .map(user -> mapper.conver(user, UserDto.class));
    }

    @Override
    public Mono<UserDto> getUser(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUser'");
    }

    @Override
    public Mono<UserDto> updateUserData(UserDto userDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUserData'");
    }

    @Override
    public Mono<UserDto> deleteUser(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

}
