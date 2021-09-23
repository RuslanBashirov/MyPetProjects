package ru.transinfocom.nbd2.service;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import ru.transinfocom.nbd2.dao.Dao;
import ru.transinfocom.nbd2.dao.Service;
import ru.transinfocom.nbd2.entity.WPUser;


@Component
public class CustomUserDetailsService implements UserDetailsService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

	private Service dao;

	public CustomUserDetailsService() throws SQLException, ClassNotFoundException {
		this.dao = new Dao();
	}

	@Override
	public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		WPUser user = null;
		try {
			user = dao.findByUsername(username);
		} catch (SQLException e) {
			LOGGER.error("", e);
		} catch (Throwable t) {
			LOGGER.error("", t);
		}
		return CustomUserDetails.fromWPUserToCustomUserDetails(user);
	}
}
