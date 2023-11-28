package com.github.ioloolo.template.jwt_auth.domain.auth.service;

import java.util.EnumSet;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.ioloolo.template.jwt_auth.common.ExceptionFactory;
import com.github.ioloolo.template.jwt_auth.common.security.util.JwtUtil;
import com.github.ioloolo.template.jwt_auth.domain.auth.data.Role;
import com.github.ioloolo.template.jwt_auth.domain.auth.data.User;
import com.github.ioloolo.template.jwt_auth.domain.auth.repository.RoleRepository;
import com.github.ioloolo.template.jwt_auth.domain.auth.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder encoder;
	private final JwtUtil jwtUtil;

	private final ExceptionFactory exceptionFactory;

	public String login(String username, String password) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
			password);
		Authentication authentication = authenticationManager.authenticate(authenticationToken);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return jwtUtil.from(authentication);
	}

	public void register(String username, String email, String password) throws Exception {

		if (userRepository.existsByUsername(username)) {
			throw exceptionFactory.of(ExceptionFactory.Type.ALREADY_EXIST_USERNAME);
		}

		if (userRepository.existsByEmail(email)) {
			throw exceptionFactory.of(ExceptionFactory.Type.ALREADY_EXIST_EMAIL);
		}

		Role userRole = roleRepository.findByName(Role.Roles.ROLE_USER).orElseThrow();

		User user = User.builder()
			.username(username)
			.email(email)
			.password(encoder.encode(password))
			.role(userRole)
			.build();

		userRepository.save(user);
	}

	@PostConstruct
	public void init() {
		EnumSet<Role.Roles> enumSet = EnumSet.allOf(Role.Roles.class);

		for (Role.Roles role : enumSet) {
			if (roleRepository.findByName(role).isEmpty()) {
				roleRepository.save(Role.builder()
					.name(role)
					.build());
			}
		}
	}
}