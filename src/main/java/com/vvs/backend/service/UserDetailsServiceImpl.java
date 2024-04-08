package com.vvs.backend.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.vvs.backend.model.User;
import com.vvs.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        log.debug("Entering in loadUserByUsername...");
        User user = (User) userRepository.findUserByUsername(username)
            .switchIfEmpty(Mono.error(new UsernameNotFoundException("Username not found...")))
            .subscribe();
        log.info("User Authenticated Successfully...");

        return user;
    }

}
