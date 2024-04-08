package com.vvs.backend.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuthRouter {

	@Bean
	public RouterFunction<ServerResponse> authRouterFunction(AuthHandler authHandler) {
		return route()
			.nest(path("/auth"), builder -> builder
				.POST("/signup", authHandler::signUp)
				.POST("/login", authHandler::login))
			.build();
	}
}
