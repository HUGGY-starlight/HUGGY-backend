package com.starlight.huggy.service.auth;

import com.starlight.huggy.domain.auth.SocialLoginType;
import com.starlight.huggy.domain.auth.Member;
import com.starlight.huggy.dto.auth.GoogleMemberInfoDto;
import com.starlight.huggy.dto.auth.TokenResponseDto;
import com.starlight.huggy.dto.auth.MemberInfoDto;
import com.starlight.huggy.exception.AuthorityExceptionType;
import com.starlight.huggy.exception.BizException;
import com.starlight.huggy.exception.InternalServerExceptionType;
import com.starlight.huggy.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleService extends SocialAuthService{
    private static final String GOOGLE_TOKEN_REQUEST_URL = "https://oauth2.googleapis.com";

    @Value("${auth.google.client-id}")
    private String CLIENT_ID;

    @Value("${auth.google.client-secret}")
    private String CLIENT_SECRET;

    @Value("${auth.google.redirect-uri}")
    private String REDIRECT_URI;

    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;

    /**
     * 인가 코드로 토큰 받기
     */
    @Override
    public TokenResponseDto getToken(String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("code", code);
        params.put("client_id", CLIENT_ID);
        params.put("client_secret", CLIENT_SECRET);
        params.put("redirect_uri", REDIRECT_URI);

        JSONParser parser;
        JSONObject elem;

        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_REQUEST_URL + "/token", params, String.class);
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                log.error("구글 API 연결 실패 : code = {}", code);
                throw new BizException(AuthorityExceptionType.GOOGLE_CONNECTION_ERROR);
            }
            String responseBody = responseEntity.getBody();

            parser = new JSONParser();
            elem = (JSONObject) parser.parse(responseBody);

            return TokenResponseDto.builder()
                    .token_type(elem.get("token_type").toString())
                    .access_token(elem.get("access_token").toString())
                    .expires_in(elem.get("expires_in").toString())
                    .refresh_token(elem.get("refresh_token").toString())
                    .scope(elem.get("scope").toString())
                    .id_token(elem.get("id_token").toString())
                    .build();

        } catch (ParseException e){
            log.error("JSON 파싱 실패 : {}", e.getMessage());
            throw new BizException(InternalServerExceptionType.INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            log.error("구글 API 연결 실패 : code = {}", code);
            throw new BizException(AuthorityExceptionType.GOOGLE_CONNECTION_ERROR);
        }
    }

    /**
     * 받은 토큰을 이용하여 사용자 정보를 구글 서버로부터 불러오기
     */
    @Override
    public GoogleMemberInfoDto getMemberInfo(TokenResponseDto tokenResponse){
        String idToken = tokenResponse.getId_token();
        String requestUrl = UriComponentsBuilder.fromHttpUrl(GOOGLE_TOKEN_REQUEST_URL + "/tokeninfo").queryParam("id_token", idToken).toUriString();
        GoogleMemberInfoDto memberInfo;
        JSONParser parser;
        JSONObject elem;
        String resultJson;

        try {
            resultJson = restTemplate.getForObject(requestUrl, String.class);

            parser = new JSONParser();
            elem = (JSONObject) parser.parse(resultJson);


            memberInfo = GoogleMemberInfoDto.builder()
                    .iss(elem.get("iss").toString())
                    .azp(elem.get("azp").toString())
                    .aud(elem.get("aud").toString())
                    .iat(elem.get("iat").toString())
                    .exp(elem.get("exp").toString())
                    .alg(elem.get("alg").toString())
                    .kid(elem.get("kid").toString())
                    .typ(elem.get("typ").toString())
                    .email_verified(elem.get("email_verified").toString())
                    .at_hash(elem.get("at_hash").toString())
                    .given_name(elem.get("given_name").toString())
                    .family_name(elem.get("family_name").toString())
                    .locale(elem.get("locale").toString())
                    .build();
            memberInfo.setName(elem.get("name").toString());
            memberInfo.setEmail(elem.get("email").toString());
            memberInfo.setNickname(elem.get("name").toString());
            memberInfo.setProfileImg(elem.get("picture").toString());
            memberInfo.setSocialId(elem.get("sub").toString());

//            ObjectMapper objectMapper = new ObjectMapper();
//                if(resultJson != null){
//                    GoogleMemberInfoDto memberInfoDto = objectMapper.readValue(resultJson, new TypeReference<GoogleMemberInfoDto>() {});
//                    return memberInfoDto;
//                }
//                else{
//                    throw new Exception("Google OAuth failed!");
//                }
            return memberInfo;
        }catch (ParseException e){
            log.error("JSON 파싱 실패 : {}", e.getMessage());
            throw new BizException(InternalServerExceptionType.INTERNAL_SERVER_ERROR);
        }catch (Exception e) {
            log.error("구글 API 연결 실패 : id_token = {}", idToken);
            log.error("구글 API 연결 실패 : {}", e.getMessage());
            throw new BizException(AuthorityExceptionType.GOOGLE_CONNECTION_ERROR);
        }
    }

    /**
     * 회원 등록이 안 되어 있을 경우 회원가입
     */
    @Override
    public void signup(MemberInfoDto memberInfo) {
        String socialId = memberInfo.getSocialId();

        Boolean isRegistered = !memberRepository.findBySocialId(socialId).equals(Optional.empty());
        if (isRegistered) {
            return;
        }

        String name = memberInfo.getName();
        String email = memberInfo.getEmail();
        String profileImg = memberInfo.getProfileImg();

        Member member = Member.builder()
                .socialId(socialId)
                .nickname(name)
                .email(email)
                .profileImg(profileImg)
                .socialLoginType(SocialLoginType.GOOGLE)
                .build();
        memberRepository.save(member);
        log.info("회원가입 : {} ({})", member.getEmail(), member.getNickname());
    }
}