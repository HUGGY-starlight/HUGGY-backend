package com.starlight.huggy.repository;

import com.starlight.huggy.model.User;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>{
	User findByUsername(String username);
	Optional<User> findByProviderAndProviderId(String provider, String providerId);
}


