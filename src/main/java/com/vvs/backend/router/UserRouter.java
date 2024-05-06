package com.vvs.backend.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class UserRouter {

    @Bean
    public RouterFunction<ServerResponse> userRouterFunction(UserHandler userHandler) {
        return RouterFunctions.route()
            .nest(RequestPredicates.path("/api/users"), builder -> builder
                .GET("", userHandler::getUsers)
                .GET("/{username}", userHandler::getUser))
            .build();
    }
}