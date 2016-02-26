package com.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {
	static byte[] iv = { (byte) 0xcb, (byte) 0x53, (byte) 0x03, (byte) 0x0f,
			(byte) 0xe0, (byte) 0x79, (byte) 0x9d, (byte) 0xdc, (byte) 0x80,
			(byte) 0xa9, (byte) 0x83, (byte) 0xf1, (byte) 0x03, (byte) 0xb6,
			(byte) 0x59, (byte) 0x83 };

	private static IvParameterSpec ivSpec = new IvParameterSpec(iv);
	private static SecretKey skey = getSecretKey();
	static {
		try {
			Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static byte[] decrypt(byte[] cipherText) throws Exception {
		try {
			Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			decryptCipher.init(Cipher.DECRYPT_MODE, skey, ivSpec);
			byte[] plainText = decryptCipher.doFinal(cipherText);
			return plainText;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static byte[] encrypt(byte[] plainText) throws Exception {
		try {
			Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			encryptCipher.init(Cipher.ENCRYPT_MODE, skey, ivSpec);
			byte[] cipherText = encryptCipher.doFinal(plainText);
			return cipherText;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private static SecretKey getSecretKey() {
		byte[] key = getKey();
		SecretKey skey = new SecretKeySpec(key, 0, 16, "AES");
		return skey;
	}

	public static byte[] sha2sum(byte[] buffer) {
		try {
			MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
			sha2.update(buffer);
			return sha2.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] getKey() {
		try {
			String key1 = "UZ4mG$;J9Y)0(4W7e})!>E'..8)G5l<l\"T2kD\"+*c/r+eb6zu{}:&2TpJ;8+}-uF";
			String key2 = "l%]LP<D=0*\"7P)=pZWYp8.*;@cj\":|d8L.s^*L@401><u*Sc+:ZDT<\\oAFbT^Lk1";
			byte[] bkey1 = sha2sum(key1.getBytes("iso8859-1"));
			byte[] bkey2 = sha2sum(key2.getBytes("iso8859-1"));
			byte[] key = new byte[bkey2.length];
			for (int i = 0; i < bkey2.length; i++) {
				int a = bkey1[i];
				int b = bkey2[i];
				key[i] = (byte) (a ^ b);
			}
			return key;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
