package com.starlight.huggy.user.service;

import com.starlight.huggy.security.oauth.provider.OAuthUserInfo;
import com.starlight.huggy.user.domain.User;

public interface UserService {
    User getUserByName(OAuthUserInfo user);

    User register(OAuthUserInfo user); // 회원 가입

}
