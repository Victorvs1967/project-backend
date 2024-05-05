package com.vvs.backend.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.vvs.backend.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

import static com.vvs.backend.utils.ListUtils.toSingleton;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String secret;
	@Value("${jwt.expiration.in.hours}")
	private int expirationTimeInHours;

	private SecretKey key;
	public static final String KEY_ROLE = "role";

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Boolean isTokenExpirated(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Mono<Boolean> validateToken(String token, String string) {
		final String username = extractUsername(token);
		return Mono.just(username.equals(string) && !isTokenExpirated(token));
	}

	public String generateToken(User userDetails) {
		
		Map<String, Object> claims = new HashMap<>();

		String authority = userDetails
			.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(toSingleton());
		claims.put(KEY_ROLE, authority);

		return createToken(claims, userDetails.getUsername());
	}

	public Mono<Claims> extractAllClaims(String token) {
		return Mono.just(Jwts
			.parser()
			.verifyWith(getKey())
			.build()
			.parseSignedClaims(token)
			.getPayload());
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		return claimsResolver.apply(extractAllClaims(token).block());
	}

	private String createToken(Map<String, Object> claims, String username) {

		return Jwts.builder()
			.claims(claims)
			.subject(username)
			.expiration(Date.from(Instant.now().plus(Duration.ofHours(expirationTimeInHours))))
			.issuedAt(Date.from(Instant.now()))
			.signWith(getKey())
			.compact();
	}

	private SecretKey getKey() {
		if (key == null)
			key = Keys.hmacShaKeyFor(secret.getBytes());
		return key;
	}
}