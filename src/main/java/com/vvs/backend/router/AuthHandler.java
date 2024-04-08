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
		return request.bodyToMono(UserDto.class)
			.map(credentials -> authService.signUp(credentials))
			.flatMap(userDetails -> ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(userDetails, UserDto.class))

		 ;
	}

	public Mono<ServerResponse> login(ServerRequest request) {
		return request.bodyToMono(LoginDto.class)
			.map(credentials -> authService.login(credentials.getUsername(), credentials.getPassword()))
			.switchIfEmpty(Mono.error(new Exception("Login error...")))
			.flatMap(response -> ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(response, ResponseDto.class));
	}
}
