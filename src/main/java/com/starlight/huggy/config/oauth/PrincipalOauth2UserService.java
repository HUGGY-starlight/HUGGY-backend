package com.starlight.huggy.config.oauth;

import com.starlight.huggy.config.auth.PrincipalDetails;
import com.starlight.huggy.config.oauth.provider.GoogleUserInfo;
import com.starlight.huggy.config.oauth.provider.OAuth2UserInfo;
import com.starlight.huggy.model.User;
import com.starlight.huggy.repository.UserRepository;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

	@Autowired
	private UserRepository userRepository;
	/**
	 * 구글로부터 받은 데이터 후처리
	 * */
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest); // google의 회원 프로필 조회

		// code를 통해 구성한 정보
		System.out.println("userRequest clientRegistration : " + userRequest.getClientRegistration());
		// token을 통해 응답받은 회원정보
		System.out.println("oAuth2User : " + oAuth2User);
	
		return processOAuth2User(userRequest, oAuth2User);
	}

	private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

		OAuth2UserInfo oAuth2UserInfo = null;
		if (userRequest.getClientRegistration().getRegistrationId().equals("google")) { // 구글 로그인
			System.out.println("구글 로그인 + 회원가입 진행");
			oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
		} else {
			System.out.println("지원하지 않는 로그인");
		}

		Optional<User> userOptional =
				userRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());
		
		User user;
		if (userOptional.isPresent()) {// 이미 회원
			user = userOptional.get();
			user.setEmail(oAuth2UserInfo.getEmail());
			userRepository.save(user);
		} else {// user의 패스워드가 null이기 때문에 OAuth 유저는 일반적인 로그인을 할 수 없음.
			user = User.builder()
					.username(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
					.email(oAuth2UserInfo.getEmail())
					.role("ROLE_USER")
					.provider(oAuth2UserInfo.getProvider())
					.providerId(oAuth2UserInfo.getProviderId())
					.build();
			userRepository.save(user);
		}

		return new PrincipalDetails(user, oAuth2User.getAttributes());
	}
}
