package com.vvs.backend.router;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.vvs.backend.dto.UserDto;
import com.vvs.backend.service.JwtService;
import com.vvs.backend.service.UserService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {

	private final UserService userService;
	private final JwtService jwtService;

	Mono<ServerResponse> getUsers(ServerRequest request) {
		String token = request.headers().firstHeader("authorization").substring(7);
		return jwtService.validateToken(token, jwtService.extractUsername(token))
			.map(result -> !result)
			.switchIfEmpty(Mono.error(new Exception()))
			.flatMap(tok -> ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(userService.getUsers(), UserDto.class));
	}

	Mono<ServerResponse> getUser(ServerRequest request) {
		String username = request.pathVariable("username");
		String token = request.headers().firstHeader("authorization").substring(7);
		return jwtService.validateToken(token, username)
			.switchIfEmpty(Mono.error(new Exception("Username error...")))
			.map(result -> !result)
			.map(isUsername -> username)
			.map(userService::getUser)
			.flatMap(user -> ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(user, UserDto.class));
	}

}