package ru.transinfocom.nbd2.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ru.transinfocom.nbd2.entity.WPUser;

public class CustomUserDetails implements UserDetails {

	private String login;
	private String password;
	private Collection<? extends GrantedAuthority> grantedAuthorities;

	public static CustomUserDetails fromWPUserToCustomUserDetails(WPUser user) {
		CustomUserDetails c = null;
		if (user != null) { 
			c = new CustomUserDetails();
			c.login = user.getUserName();
			c.password = user.getPasswordHash();
			c.grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
		}
		
		return c;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return login;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
