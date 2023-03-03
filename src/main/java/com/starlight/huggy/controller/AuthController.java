package com.starlight.huggy.controller;

import com.starlight.huggy.domain.auth.SocialLoginType;
import com.starlight.huggy.dto.DefaultResponse;
import com.starlight.huggy.dto.auth.MemberInfoDto;
import com.starlight.huggy.dto.auth.TokenResponseDto;
import com.starlight.huggy.exception.AuthorityExceptionType;
import com.starlight.huggy.exception.BizException;
import com.starlight.huggy.exception.InternalServerExceptionType;
import com.starlight.huggy.service.auth.GoogleService;
import com.starlight.huggy.service.auth.KakaoService;
import com.starlight.huggy.service.auth.SocialAuthService;
import com.starlight.huggy.vo.ResponseMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class AuthController {
	private final GoogleService googleService;
	private final KakaoService kakaoService;

	@PostMapping("/auth/{socialType}")
	public ResponseEntity login(@RequestHeader(name = "auth-code", required = false) String authorizationCode, @PathVariable("socialType") String socialType){
		try {
			if(authorizationCode == null){
				throw new BizException(AuthorityExceptionType.NO_AUTHORIZATION_CODE);
			}

			TokenResponseDto tokenResponse;
			MemberInfoDto memberInfo;
			SocialAuthService socialAuthService;

			// api 경로 중 google, kakao, apple 여부 확인. 이외가 들어오면 exception
			if(socialType.equals(SocialLoginType.KAKAO.getValue())){
				socialAuthService = kakaoService;
			}else if(socialType.equals(SocialLoginType.GOOGLE.getValue())){
				socialAuthService = googleService;
			}else{
				throw new BizException(AuthorityExceptionType.INVALID_SOCIAL_TYPE);
			}

			tokenResponse = socialAuthService.getToken(authorizationCode);
			memberInfo = socialAuthService.getMemberInfo(tokenResponse);
			socialAuthService.signup(memberInfo);

			log.info("로그인 : {} ({})", memberInfo.getEmail(), memberInfo.getName());
			return new ResponseEntity<>(DefaultResponse.create(HttpStatus.OK.value(), ResponseMessage.LOGIN_SUCCESS, memberInfo), HttpStatus.OK);
		}
		catch (BizException e){
			log.error(e.getMessage());
			return new ResponseEntity<>(DefaultResponse.create(e.getBaseExceptionType().getHttpStatus().value(), e.getMessage()), e.getBaseExceptionType().getHttpStatus());
		}
		catch (Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<>(DefaultResponse.create(HttpStatus.INTERNAL_SERVER_ERROR.value(), InternalServerExceptionType.INTERNAL_SERVER_ERROR.getMessage()), InternalServerExceptionType.INTERNAL_SERVER_ERROR.getHttpStatus());
		}
	}
}
