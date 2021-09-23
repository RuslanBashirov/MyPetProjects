package ru.transinfocom.nbd2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ru.transinfocom.nbd2.config.jwt.JwtFilter;

@Configuration
@ComponentScan({ "ru.transinfocom.nbd2" })
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtFilter jwtFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().httpBasic().disable().csrf().disable().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests().antMatchers("/api/**")
				.hasAnyRole("DEFAULT", "DEPO", "DIR", "DEPO_EDIT", "DIR_EDIT").antMatchers("/exit").hasAnyRole("DEFAULT", "DEPO", "DIR").antMatchers("/swagger-ui/").permitAll()
				.antMatchers("/refresh").permitAll().antMatchers("/auth").permitAll().and().exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint()).and()
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new CustomAuthenticationEntryPoint();
	}
}
