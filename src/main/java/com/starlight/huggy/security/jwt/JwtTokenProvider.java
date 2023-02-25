package com.starlight.huggy.security.jwt;

import com.starlight.huggy.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtTokenProvider {
	@Value("${custom.jwt.secret}")
	public String tokenSecret = null;
	public static final String tokenExpirationMsec = "86400000";

	public String create(Authentication authentication) {
		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
		
		LocalDateTime localDateTime = LocalDateTime.now();
		int sec = Integer.parseInt(tokenExpirationMsec) / 1000;
		localDateTime = localDateTime.plusSeconds(sec);
		ZoneId defaultZoneId = ZoneId.systemDefault();
		Date expireDate = Date.from(localDateTime.atZone(defaultZoneId).toInstant());
		
		String token = Jwts.builder()
				.setSubject(Long.toString(principal.getId()))
				.setIssuedAt(new Date())
				.setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS256, tokenSecret).compact();
		
		return token;
	}
	
	public Claims getClaims(String token) {
		Claims claims = Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws(token)
                .getBody();
		
		return claims;
	}
}
