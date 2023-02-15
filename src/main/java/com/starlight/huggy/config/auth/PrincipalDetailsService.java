package com.starlight.huggy.config.auth;

import com.starlight.huggy.model.User;
import com.starlight.huggy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class PrincipalDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("username: "+username);
		User user = userRepository.findByUsername(username);
		if(user != null) {
			return new PrincipalDetails(user);
		}
		return null;
	}
}
