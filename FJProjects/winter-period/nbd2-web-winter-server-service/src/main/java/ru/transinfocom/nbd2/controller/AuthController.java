package ru.transinfocom.nbd2.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.io.BaseEncoding;

import ru.transinfocom.nbd2.config.jwt.JwtProvider;
import ru.transinfocom.nbd2.dao.Dao;
import ru.transinfocom.nbd2.dao.Service;
import ru.transinfocom.nbd2.entity.WPUser;
import ru.transinfocom.nbd2.entity.requests.AuthRequest;
import ru.transinfocom.nbd2.entity.requests.RefreshRequest;
import ru.transinfocom.nbd2.entity.response.AuthResponse;
import ru.transinfocom.nbd2.utils.HashUtils;
import ru.transinfocom.nbd2.utils.Utils;

@CrossOrigin(origins = "*")
@RestController
public class AuthController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	public JwtProvider jwtProvider;

	private Service dao;

	public AuthController() throws SQLException, ClassNotFoundException {
		this.dao = new Dao();
	}

	@PostMapping("/auth")
	public ResponseEntity<AuthResponse> authUser(HttpServletRequest request, @RequestBody AuthRequest authRequest) {
		LOGGER.trace("start");
		ResponseEntity<AuthResponse> response = null;
		WPUser user = null;
		try {
			String ip = request.getRemoteAddr();
			byte[] decodedBytes = BaseEncoding.base64().decode(authRequest.getLogopassBase64());
			String logopass = new String(decodedBytes);
			String userName = null;
			String password = null;
			String[] encodedSplittedLogopass = logopass.split(":");

			if (encodedSplittedLogopass.length == 2) {
				userName = encodedSplittedLogopass[0].trim();
				password = encodedSplittedLogopass[1].trim();
			}

			if ((userName != null) && (password != null)) {
				String passwordHash = HashUtils.hashPassword(password, userName);
				user = dao.auth(userName, passwordHash, ip);

				if (user != null) {
					response = generateTokenByWPUser(user);
				} else {
					response = new ResponseEntity<AuthResponse>(new AuthResponse(null, "Неверный логин или пароль"),
							HttpStatus.OK);
				}
			} else {
				response = new ResponseEntity<AuthResponse>(new AuthResponse(null, "Пустой логин или пароль"),
						HttpStatus.OK);
			}
		} catch (SQLException sql) {
			LOGGER.error("SQLException", sql);
		} catch (Throwable t) {
			LOGGER.error("Unknown exception", t);
		} finally {
			LOGGER.trace("stop");
		}
		return response;
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refresh(HttpServletRequest request,
			@RequestBody RefreshRequest refreshRequest) {
		LOGGER.trace("start");
		ResponseEntity<AuthResponse> response = null;
		WPUser user = null;
		try {
			String ip = request.getRemoteAddr();
			String secretString = refreshRequest.getSecretString();
			int sessionId = Utils.decryptAndGetSessionId(secretString); // decrypt and check validation: return -1 if
																		// not valid

			if (sessionId != -1) {
				user = dao.refresh(sessionId, ip);
				if (user != null) {
					response = generateTokenByWPUser(user);
				} else {
					response = new ResponseEntity<AuthResponse>(new AuthResponse(null, "Не удалось обновить токен"),
							HttpStatus.OK);
				}
			} else {
				response = new ResponseEntity<AuthResponse>(new AuthResponse(null, "Неверная секретная строка"),
						HttpStatus.OK);
			}
		} catch (SQLException sql) {
			LOGGER.error("SQLException", sql);
		} catch (Throwable t) {
			LOGGER.error("Unknown exception", t);
		} finally {
			LOGGER.trace("stop");
		}
		return response;
	}

	@PostMapping("/exit")
	public void exit(HttpServletRequest request) {
		LOGGER.trace("start");
		try {
			String token = JwtProvider.getTokenFromRequest(request);
			int sessionId = jwtProvider.getSessionIdFromToken(token);
			dao.exit(sessionId);
		} catch (SQLException sql) {
			LOGGER.error("SQLException", sql);
		} catch (Throwable t) {
			LOGGER.error("Unknown exception", t);
		} finally {
			LOGGER.trace("stop");
		}
	}

	private ResponseEntity<AuthResponse> generateTokenByWPUser(WPUser user) {
		ResponseEntity<AuthResponse> response = null;
		response = new ResponseEntity<AuthResponse>(new AuthResponse(jwtProvider.generateToken(user.getSessionId(),
				user.getAsutUserId(), user.getUserName(), user.getPredId(), user.getPredName(), user.getRole(), user.getFirstName(),
				user.getLastName(), user.getPatrName()), "Token was generated"), HttpStatus.OK);
		return response;
	}
}
