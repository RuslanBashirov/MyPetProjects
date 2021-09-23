package ru.transinfocom.nbd2.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import ru.transinfocom.nbd2.service.CustomUserDetails;
import ru.transinfocom.nbd2.service.CustomUserDetailsService;

@Component
public class JwtFilter extends GenericFilterBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	public JwtProvider jwtProvider;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		String token = JwtProvider.getTokenFromRequest(httpRequest);

		if (jwtProvider.validateToken(token)) {
			LOGGER.info("Token is valid");
			String userName = jwtProvider.getUserNameFromToken(token);
			CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userName);
			if (customUserDetails != null) {
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(customUserDetails,
						null, customUserDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}

}
