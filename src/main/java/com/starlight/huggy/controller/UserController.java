package com.starlight.huggy.controller;

import com.starlight.huggy.exception.ResourceNotFoundException;
import com.starlight.huggy.model.User;
import com.starlight.huggy.repository.UserRepository;
import com.starlight.huggy.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    

    @GetMapping("/")
    public User getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
    	System.out.println(userPrincipal.getId());
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}
