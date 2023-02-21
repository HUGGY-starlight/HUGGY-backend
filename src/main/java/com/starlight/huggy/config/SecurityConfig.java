package com.starlight.huggy.config;

import com.starlight.huggy.config.jwt.JwtAuthenticationFilter;
import com.starlight.huggy.config.oauth.PrincipalOauth2UserService;
import com.starlight.huggy.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableWebSecurity // 필터 체인
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class SecurityConfig {
	private final CorsFilter corsFilter;
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션 사용 x
				.and()
				.addFilter(corsFilter) //@CrossOrgin(인증x) 시큐리티에 인증 등록
				/*.addFilterBefore(
						new JwtTokenFilter(),
						UsernamePasswordAuthenticationFilter.class)userService,secretKey*/
				//.formLogin().loginProcessingUrl("/login").defaultSuccessUrl("/")
				.formLogin().disable()
				.httpBasic().disable() // ID + PW 인증방식
				.addFilter(new JwtAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class))/**/))
				.authorizeRequests()
				.antMatchers("/api/v1/user/**").authenticated()
				.antMatchers("/api/v1/user/join/**","/login/**").permitAll()
				.and()
				.oauth2Login().userInfoEndpoint()//구글 로그인 완료후, 후처리
				.userService(principalOauth2UserService);

//		http.headers().frameOptions().sameOrigin();
		return http.build();

	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
	}
	@Bean
	public BCryptPasswordEncoder encodePassword() {
		return new BCryptPasswordEncoder();
	}


}




