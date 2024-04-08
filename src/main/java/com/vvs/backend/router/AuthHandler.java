package com.vvs.backend.router;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.vvs.backend.dto.LoginDto;
import com.vvs.backend.dto.ResponseDto;
import com.vvs.backend.dto.UserDto;
import com.vvs.backend.service.AuthService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthHandler {

	private final AuthService authService;

	public Mono<ServerResponse> signUp(ServerRequest request) {

		Mono<UserDto> userDto = request.bodyToMono(UserDto.class)
			.flatMap(credentials -> authService.signUp(credentials))
			.map(userDetails -> userDetails);

		return ServerResponse
			.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(userDto, UserDto.class);
	}

	public Mono<ServerResponse> login(ServerRequest request) {

		Mono<ResponseDto> response = request.bodyToMono(LoginDto.class)
			.flatMap(credentials -> authService.login(credentials.getUsername(), credentials.getPassword()))
			.switchIfEmpty(Mono.error(new Exception("Login error...")));

		return ServerResponse
			.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(response, ResponseDto.class);
	}
}
