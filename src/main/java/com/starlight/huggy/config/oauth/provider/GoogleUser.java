package com.starlight.huggy.config.oauth.provider;


import com.starlight.huggy.dto.auth.OAuthUserInfoDto;

public class GoogleUser implements OAuthUserInfo {

    private final OAuthUserInfoDto oAuthUserInfoDto;

    public GoogleUser(OAuthUserInfoDto userInitialInfo) {
        this.oAuthUserInfoDto = userInitialInfo;
    }

    @Override
    public String getProviderId() {
        return oAuthUserInfoDto.getProviderId();
    }

    @Override
    public String getProvider() {
        return OAuthProvider.GOOGLE.getProvider();
    }

    @Override
    public String getEmail() {
        return oAuthUserInfoDto.getEmail();
    }

    @Override
    public String getName() {
        return oAuthUserInfoDto.getName();
    }

}
