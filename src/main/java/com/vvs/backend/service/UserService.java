package com.vvs.backend.service;

import com.vvs.backend.dto.UserDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    public Flux<UserDto> getUsers();
    public Mono<UserDto> getUser(String username);
    public Mono<UserDto> updateUserData(UserDto userDto);
    public Mono<UserDto> deleteUser(String username);

}
