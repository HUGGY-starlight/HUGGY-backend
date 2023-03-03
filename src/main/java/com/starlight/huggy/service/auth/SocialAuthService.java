package com.starlight.huggy.service.auth;

import com.starlight.huggy.dto.auth.MemberInfoDto;
import com.starlight.huggy.dto.auth.TokenResponseDto;

public abstract class SocialAuthService {
    abstract public TokenResponseDto getToken(String code);
    abstract public MemberInfoDto getMemberInfo(TokenResponseDto dto);
    abstract public void signup(MemberInfoDto dto);
}