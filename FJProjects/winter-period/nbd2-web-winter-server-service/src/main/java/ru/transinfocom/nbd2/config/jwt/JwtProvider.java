package ru.transinfocom.nbd2.config.jwt;

import static org.springframework.util.StringUtils.hasText;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtProvider.class);

	@Value("${jwt.secret}")
	private String jwtSecret;
	
	@Value("${jwt.sessionTime}")
	private long sessionTime; // milliseconds

	public String generateToken(int sessionId, int asutUserId, String login, int predId, String predName, String role, String firstName,
			String lastName, String patrName) {
		Date date = new Date(System.currentTimeMillis() + sessionTime);
		Map<String, Object> claims = new HashMap<>();
		claims.put("sessionId", sessionId);
		claims.put("asutUserId", asutUserId);
		claims.put("predId", predId);
		claims.put("predName", predName);
		claims.put("role", role);
		claims.put("firstName", firstName);
		claims.put("lastName", lastName);
		claims.put("patrName", patrName);

		return Jwts.builder().addClaims(claims).setSubject(login).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(date).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public boolean validateToken(String token) throws ExpiredJwtException {
		try {
			if (token != null) {
				Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("Invalid Token");
		}
		return false;
	}

	public static String getTokenFromRequest(HttpServletRequest request) throws ExpiredJwtException {
		String token = request.getHeader("Authorization");
		if (hasText(token) && token.startsWith("Bearer ")) {
			return token.substring(7);
		}
		return null;
	}

	public int getAsutUserIdFromToken(String token) throws ExpiredJwtException {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		return (int) claims.get("asutUserId");
	}

	public int getSessionIdFromToken(String token) throws ExpiredJwtException {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		return (int) claims.get("sessionId");
	}

	public String getUserNameFromToken(String token) throws ExpiredJwtException {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	public int getPredIdFromToken(String token) throws ExpiredJwtException {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		return (int) claims.get("predId");
	}
}
