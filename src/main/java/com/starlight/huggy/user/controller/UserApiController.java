package com.starlight.huggy.user.controller;

import com.starlight.huggy.security.config.PrincipalDetails;
import com.starlight.huggy.user.domain.User;
import com.starlight.huggy.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor 
public class UserApiController {
	
	@GetMapping("user")
	public ResponseEntity<User> user(Authentication authentication) {
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		return ResponseEntity.ok(principal.getUser());
	}
}











