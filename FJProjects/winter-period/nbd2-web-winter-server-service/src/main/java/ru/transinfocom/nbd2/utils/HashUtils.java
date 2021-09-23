package ru.transinfocom.nbd2.utils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.google.common.io.BaseEncoding;

public class HashUtils {

	public static String hashPassword(String password, String salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		byte[] saltBytes = salt.getBytes();

		KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 65536, 128);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

		byte[] passwordHashBytes = factory.generateSecret(spec).getEncoded();

		String passwordHash = BaseEncoding.base64().encode(passwordHashBytes);

		return passwordHash;
	}
	
//	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
//		System.out.println(hashPassword("depo_edit", "depo_edit"));
//	}
}
