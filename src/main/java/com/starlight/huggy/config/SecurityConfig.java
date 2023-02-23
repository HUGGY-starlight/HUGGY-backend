package com.starlight.huggy.config;

import com.starlight.huggy.model.CustomOAuth2Provider;
import com.starlight.huggy.repository.UserRepository;
import com.starlight.huggy.security.CustomUserDetailsService;
import com.starlight.huggy.security.jwt.JwtBasicAuthenticationFilter;
import com.starlight.huggy.security.jwt.JwtCommonAuthorizationFilter;
import com.starlight.huggy.security.jwt.JwtTokenProvider;
import com.starlight.huggy.security.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Bean
    public BCryptPasswordEncoder encodePWD() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() //csrf 토큰
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsFilter) //@CrossOrgin(인증x) 시큐리티에 인증 등록
                .formLogin().disable()//.loginPage("/login").loginProcessingUrl("/doLogin")
                .httpBasic().disable()

                .addFilter(new JwtBasicAuthenticationFilter())
                .addFilter(new JwtCommonAuthorizationFilter(authenticationManager(http.getSharedObject(
                        AuthenticationConfiguration.class)), tokenProvider, userRepository))

                .authorizeRequests()
                //.antMatchers("/", "/oauth2/**", "/login/**", "/css/**",
                //        "/images/**", "/js/**", "/console/**").permitAll()
                .antMatchers("/api/v1/home").permitAll()
                .antMatchers("/api/v1/user/**").access("hasRole('ROLE_USER')")
                .antMatchers("/api/v1/admin/**").access("hasRole('ROLE_ADMIN')")
                //.anyRequest().authenticated()
                //.anyRequest().denyAll()
                .and()
                .oauth2Login().userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler((request, response, authentication) -> {
                    String token = tokenProvider.create(authentication);
                    response.addHeader("Authorization", "Bearer " + token);
                    String targetUrl = "/api/v1/auth/success";
                    RequestDispatcher dis = request.getRequestDispatcher(targetUrl);
                    dis.forward(request, response);
                })
                .failureHandler(
                        (request, response, exception) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties clientProperties,
                                                                     @Value("${spring.security.oauth2.client.registration.kakao.client-id}") String kakaoClientId,
                                                                     @Value("${spring.security.oauth2.client.registration.kakao.client-secret}") String kakaoClientSecret) {

        List<ClientRegistration> registrations =
                clientProperties.getRegistration().keySet().stream()
                        .map(client -> getRegistration(clientProperties, client))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

        // 카카오 OAuth2 정보 추가
        registrations.add(
                CustomOAuth2Provider.KAKAO.getBuilder("kakao")
                        .clientId(kakaoClientId)
                        .clientSecret(kakaoClientSecret)
                        .jwkSetUri("temp")
                        .build()
        );

        return new InMemoryClientRegistrationRepository(registrations);
    }

    private ClientRegistration getRegistration(OAuth2ClientProperties clientProperties, String provider) {
        if ("google".equals(provider)) {
            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration()
                    .get("google");

            return CommonOAuth2Provider.GOOGLE.getBuilder(provider)
                    .clientId(registration.getClientId())
                    .clientSecret(registration.getClientSecret())
                    .scope("email", "profile")
                    .build();
        }
        return null;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }
}
