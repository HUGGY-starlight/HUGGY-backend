package com.starlight.huggy.config.auth;

import com.starlight.huggy.model.User;
import com.starlight.huggy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// formLogin을 disable해놨기에 http://localhost:8080/login 여기서 동작을 안한다.
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{

	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("/login- loadUserByUsername");
		System.out.println("username: "+username);
		User user = userRepository.findByUsername(username);
		if(user != null) {
			return new PrincipalDetails(user);
		}
		return null;
	}
}
