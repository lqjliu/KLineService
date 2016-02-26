package com.bgj.authentication;

import java.security.MessageDigest;



public class Encryption {
	private static String hash(String input, String hashAlgorith) {
		String result = "";
		if (input == null || input.trim().equals("")) {
			return result;
		}
		byte[] i_str = input.getBytes();
		byte[] o_str;
		try {
			MessageDigest sha = MessageDigest.getInstance(hashAlgorith);
			o_str = sha.digest(i_str);
			result = byte2hex(o_str);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("unable to do one way hashing",
					e);
		}
		return result;
	}

	private static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = java.lang.Integer.toHexString(b[n] & 0xff);
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	public static String oneWayHash(String input) {
		return hash(input, "SHA-256");
	}
}
