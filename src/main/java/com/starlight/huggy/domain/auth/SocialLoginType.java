package com.starlight.huggy.domain.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialLoginType {
    GOOGLE("google"),
    KAKAO("kakao");

    private final String value;
}