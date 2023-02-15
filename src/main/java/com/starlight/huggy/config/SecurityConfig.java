package com.starlight.huggy.config;

import com.starlight.huggy.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity // 필터 체인
@EnableMethodSecurity(securedEnabled = true)//@secured("ROLE_MANAGER") , @PreAuthorize("hasRole(ROLE_MANAGER)")
public class SecurityConfig {

	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf()
				.disable()
				.authorizeHttpRequests()
				.antMatchers("/user/**")
				.authenticated()
				.antMatchers("/login/**")
				.permitAll()
				.anyRequest().permitAll()
				.and()
				.formLogin()
				.loginProcessingUrl("/login")
				.defaultSuccessUrl("/")
				.and()
				.oauth2Login()
				.and()
				.httpBasic()
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);


		http.headers().frameOptions().sameOrigin();
		return http.build();


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




