package com.mifel.test.service.user.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.mifel.test.dto.auth.LoginDto;
import com.mifel.test.dto.auth.SignUpDto;
import com.mifel.test.entity.auth.Role;
import com.mifel.test.entity.auth.User;
import com.mifel.test.repository.auth.RoleRepository;
import com.mifel.test.repository.auth.UserRepository;
import com.mifel.test.security.CustomUserDetails;
import com.mifel.test.security.CustomUserDetailsService;
import com.mifel.test.security.JwtManager;
import com.mifel.test.service.user.UserService;


@Service
public class UserServiceImpl implements UserService {
	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtManager jwtManager;

	@Override
	public SignUpDto signUp(SignUpDto signUpDto) {
		log.info("calling signUp....");
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());

		User user = User.builder().email(signUpDto.getEmail()).username(signUpDto.getUsername())
				.password(encodedPassword).build();
		user = userRepository.save(user);

		List<Role> authorities = new ArrayList<>();
		authorities.add(Role.builder().user(user).name("USER").build());
		authorities.add(Role.builder().user(user).name("ADMIN").build());
		roleRepository.saveAll(authorities);

		return SignUpDto.builder().status("success").build();
	}

	@Override
	public LoginDto login(LoginDto loginDto) {
		log.info("calling login....");
		CustomUserDetails userDetails;
		try {
			userDetails = customUserDetailsService.loadUserByUsername(loginDto.getUsername());

		} catch (UsernameNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
		}

		if (passwordEncoder.matches(loginDto.getPassword(), userDetails.getPassword())) {
			log.info("password matched");

			Map<String, Object> claims = new HashMap<>();
			claims.put("username", userDetails.getUsername());

			String authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
					.collect(Collectors.joining(","));
			claims.put("roles", authorities);
			claims.put("userId", userDetails.getId());
			String subject = userDetails.getUsername();
			String jwt = jwtManager.generateToken(claims, subject);
			return LoginDto.builder().token(jwt).build();
		}

		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
	}

}
