package com.starlight.huggy.auth.controller;

import com.starlight.huggy.auth.exception.UserUnAuthorizedException;
import com.starlight.huggy.security.jwt.JwtTokenProvider;
import com.starlight.huggy.security.oauth.provider.GoogleUser;
import com.starlight.huggy.security.oauth.provider.OAuthProvider;
import com.starlight.huggy.security.oauth.provider.OAuthUserInfo;
import com.starlight.huggy.auth.dto.OAuthTokenDto;
import com.starlight.huggy.auth.dto.OAuthUserInfoDto;
import com.starlight.huggy.user.domain.User;
import com.starlight.huggy.user.domain.UserRepository;
import com.starlight.huggy.auth.service.GoogleAuthService;
import com.starlight.huggy.auth.service.SocialAuthService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthApiController { //로그인, 토큰 업데이트
    private final GoogleAuthService googleService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/oauth/jwt/{SOCIAL_TYPE}") //로그인
    public ResponseEntity<String> login(@RequestBody Map<String, Object> data, @PathVariable("SOCIAL_TYPE") String socialType)
            throws UserUnAuthorizedException {
        log.debug("DATA: {}", data);

        String authorizationCode = (String) data.get("code");
        log.debug("AuthorizationCode: {}", authorizationCode);

        if (authorizationCode == null) {
            log.debug("인가 코드 없음");
            throw new UserUnAuthorizedException("인가 코드 없음");
        }

        SocialAuthService socialAuthService;
        OAuthTokenDto tokenResponse;
        OAuthUserInfo user = null;
        OAuthUserInfoDto userInitialInfo;
        if (socialType.equals(OAuthProvider.GOOGLE.getProvider())) {
            socialAuthService = googleService;
            tokenResponse = socialAuthService.getToken(authorizationCode);
            userInitialInfo = socialAuthService.getUserInfo(tokenResponse);
            user = new GoogleUser(userInitialInfo);
        } else if (socialType.equals(OAuthProvider.KAKAO.getProvider())) {
            //TODO: KAKAO
        }

        // 유저 정보 DB에 저장
        User userEntity =
                userRepository.findByUsername(user.getProvider() + "_" + user.getProviderId());

        if (userEntity == null) { // 신규 회원
            User userRequest = User.builder()
                    .username(user.getProvider() + "_" + user.getProviderId())
                    .password(bCryptPasswordEncoder.encode("겟인데어"))
                    .email(user.getEmail())
                    .provider(user.getProvider())
                    .providerId(user.getProviderId())
                    .roles("ROLE_USER")
                    .build();
            userEntity = userRepository.save(userRequest);
        }

        String jwtToken = jwtTokenProvider.create(userEntity);
        log.debug("Huggy 전용 JWT TOKEN: {}", jwtToken);
        return ResponseEntity.ok(jwtToken);
    }
}
