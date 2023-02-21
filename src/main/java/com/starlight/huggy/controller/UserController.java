package com.starlight.huggy.controller;

import com.starlight.huggy.config.auth.PrincipalDetails;
import com.starlight.huggy.model.User;
import com.starlight.huggy.repository.UserRepository;
import java.util.Iterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principal) {
        System.out.println("Principal : " + principal);
        System.out.println("OAuth2 : " + principal.getUser().getProvider());
        // iterator 순차 출력 해보기
        Iterator<? extends GrantedAuthority> iter = principal.getAuthorities().iterator();
        while (iter.hasNext()) {
            GrantedAuthority auth = iter.next();
            System.out.println(auth.getAuthority());
        }

        return "유저 페이지입니다.";
    }

    @PostMapping("/join")
    public String joinProc(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return "redirect:/login";
    }
}
