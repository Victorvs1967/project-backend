package com.vvs.backend.service;

import com.vvs.backend.dto.ResponseDto;
import com.vvs.backend.dto.UserDto;

import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<UserDto> signUp(UserDto user);
    Mono<ResponseDto> login(String username, String password);
}
