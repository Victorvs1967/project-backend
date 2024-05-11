package com.vvs.backend.service;

import java.time.Instant;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.vvs.backend.dto.UserDto;
import com.vvs.backend.mapper.AppMapper;
import com.vvs.backend.model.User;
import com.vvs.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final AppMapper mapper;

	@Override
	public Flux<UserDto> getUsers() {
		return userRepository.findAll()
			.switchIfEmpty(Mono.error(new Exception("Users not found...")))
			.map(user -> mapper.convert(user, UserDto.class));
	}

	@Override
	public Mono<UserDto> getUser(String username) {
		return userRepository.findUserByUsername(username)
			.switchIfEmpty(Mono.error(new Exception("User not found...")))
			.map(user -> mapper.convert(user, UserDto.class));
	}

	@Override
	public Mono<UserDto> updateUserData(UserDto userDto) {
		return userRepository.findUserByUsername(userDto.getUsername())
			.switchIfEmpty(Mono.error(new Exception("User not found...")))
			.map(user -> User
				.builder()
					.id(user.getId())
					.username(user.getUsername())
					.password(user.getPassword())
					.email(user.getEmail())
					.firstName(userDto.getFirstName())
					.lastName(userDto.getLastName())
					.phone(userDto.getPhone())
					.address(userDto.getAddress())
					.onCreate(null)
					.onCreate(user.getOnCreate())
					.onUpdate(Date.from(Instant.now()))
					.isActive(userDto.isActive())
					.role(userDto.getRole())
				.build())
			.flatMap(userRepository::save)
			.map(user -> mapper.convert(user, UserDto.class));
	}

	@Override
	public Mono<UserDto> deleteUser(String username) {
		return userRepository.findUserByUsername(username)
			.switchIfEmpty(Mono.error(new Exception("User not found... ")))
			.flatMap(this::delete)
			.map(user -> mapper.convert(user, UserDto.class));
	}

	private Mono<User> delete(User user) {
		return Mono.fromSupplier(() -> {
			userRepository
				.delete(user)
				.subscribe();
			return user;
		});
	}
}