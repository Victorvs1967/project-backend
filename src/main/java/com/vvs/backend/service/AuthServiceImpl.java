package com.vvs.backend.service;

import java.time.Instant;
import java.util.Date;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vvs.backend.dto.ResponseDto;
import com.vvs.backend.dto.UserDto;
import com.vvs.backend.mapper.AppMapper;
import com.vvs.backend.model.User;
import com.vvs.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final JwtService jwtService;
	private final AppMapper mapper;

	@Override
	public Mono<UserDto> signUp(UserDto userDto) {
		return isUsernameExist(userDto.getUsername())
			.filter(userExist -> !userExist)
			.switchIfEmpty(Mono.error(new Exception("Username allready exist...")))
			.flatMap(userExist -> isEmailExist(userDto.getEmail())
					.filter(emailExist -> !emailExist)
					.switchIfEmpty(Mono.error(new Exception("Email already exist..."))))
			.map(aBoolean -> userDto)
			.map(usrDto -> mapper.convert(usrDto, User.class))
			.doOnNext(user -> user.setPassword(passwordEncoder.encode(user.getPassword())))
			.doOnNext(user -> user.setOnCreate(Date.from(Instant.now())))
			.doOnNext(user -> user.setOnUpdate(user.getOnCreate()))
			.doOnNext(user -> user.setActive(true))
			.flatMap(userRepository::save)
			.map(user -> mapper.convert(user, UserDto.class));
	}

	@Override
	public Mono<ResponseDto> login(String username, String password) {
		return userRepository.findUserByUsername(username)
			.filter(userDetails -> passwordEncoder.matches(password, userDetails.getPassword()))
			.map(userDetails -> jwtService.generateToken(userDetails))
			.map(token -> ResponseDto
				.builder()
					.token(token)
				.build());
	}

	private Mono<Boolean> isUsernameExist(String username) {
		return userRepository.findUserByUsername(username)
			.map(user -> true)
			.switchIfEmpty(Mono.just(false));
	}

	private Mono<Boolean> isEmailExist(String email) {
		return userRepository.findUserByEmail(email)
			.map(mail -> true)
			.switchIfEmpty(Mono.just(false));
	}

}
