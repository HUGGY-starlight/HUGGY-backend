package com.starlight.huggy.user.service;

import com.starlight.huggy.auth.exception.AuthenticateException;
import com.starlight.huggy.security.oauth.provider.OAuthUserInfo;
import com.starlight.huggy.user.domain.User;
import com.starlight.huggy.user.domain.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User getUserByName(OAuthUserInfo user) {
        String userName = user.getProvider() + "_" + user.getProviderId();
        return userRepository.findByUsername(userName).orElse(null);
    }

    @Override
    public User register(OAuthUserInfo user) {
        User userRequest = User.builder()
                .username(user.getProvider() + "_" + user.getProviderId())
                .password(bCryptPasswordEncoder.encode("Random+"+user.getProviderId()))
                .email(user.getEmail())
                .provider(user.getProvider())
                .providerId(user.getProviderId())
                .roles("ROLE_USER")
                .build();
        return userRepository.save(userRequest);
    }
}
