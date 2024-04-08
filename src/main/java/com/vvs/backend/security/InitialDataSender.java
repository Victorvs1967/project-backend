package com.vvs.backend.security;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.vvs.backend.model.User;
import com.vvs.backend.model.UserRole;
import com.vvs.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitialDataSender implements ApplicationListener<ApplicationStartedEvent> {
    
    @Value("${admin.username}")
    private String username;

    @Value("${admin.password}")
    private String password;

    @Value("${admin.email}")
    private String email;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @SuppressWarnings("null")
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        userRepository.findUserByUsername(username)
            .switchIfEmpty(createAdmin())
            .subscribe();
    }

    @SuppressWarnings("null")
    private Mono<User> createAdmin() {
        User user = User.builder()
            .username(username)
            .password(passwordEncoder.encode(password))
            .email(email)
            .role(UserRole.ADMIN)
            .onCreate(Date.from(Instant.now()))
            .onUpdate(Date.from(Instant.now()))
            .isActivate(true)
            .build();
        
        return userRepository.save(user)
            .doOnNext(admin -> log.info("Admin user created successfully..."));
    }

}
