package ru.transinfocom.nbd2.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.google.common.io.BaseEncoding;

public class Utils {

	public static int decryptAndGetSessionId(String data) throws NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		int sessionId = -1;

		String key = "ZE5hbWUiOm51bGws";
		String iv = "ZE5hbWUiOm51bGws";

		byte[] encrypted1 = BaseEncoding.base64().decode(data);

		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
		IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

		cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

		byte[] original = cipher.doFinal(encrypted1);
		String originalString = new String(original);
		originalString = originalString.trim();

		if (checkIfValid(originalString)) {
			int i = 0;
			for (; i < originalString.length(); i++) {
				if (originalString.charAt(i) == '/') {
					break;
				}
			}

			String sessionIdString = originalString.substring(i + 1);
			sessionId = Integer.valueOf(sessionIdString);
		}

		return sessionId;
	}

	private static boolean checkIfValid(String secretString) {
		if (secretString.contains("/")) {
			return true;
		} else {
			return false;
		}
	}

}