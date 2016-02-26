package com.common.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.bgj.exception.KLineAppException;
import com.bgj.util.ReflectUtil;
import com.common.exception.ExceptionKeys;
import com.common.util.BitsUtil;
import com.common.util.EncryptionUtil;

public class TokenUtil {
	private static final int SESSION_LIVE_DURATION = 120;
	public static final int TICKET_TYPE_LENGTH = 1;
	public static final int TIMESTAMP_LENGTH = 8;
	public static final int HASH_LENGTH = 32;
	public final static byte TICKET_TYPE_WECHAT = 1;

	private final static String[] tokenFieldNames = { "openId", "nickName",
			"province", "city", "country" };

	private final static Map<String, Byte> fieldTypeMap = new HashMap<String, Byte>();

	static {
		for (byte i = 0; i < tokenFieldNames.length; i++) {
			fieldTypeMap.put(tokenFieldNames[i], new Byte((byte) (i + 1)));
		}
	}

	public static void putString(byte[] b, int off, String val) {
		byte[] varBytes = val.getBytes();
		for (int i = 0; i < varBytes.length; i++) {
			b[off + i] = varBytes[i];
		}
	}

	public static String getTicket(Token token) throws Exception {
		String ticket = null;
		byte[] majorContent = new byte[TICKET_TYPE_LENGTH
				+ token.getTicketLength() + TIMESTAMP_LENGTH];

		int pos = processTokenType(token, majorContent);

		pos = processStringFields(token, majorContent, pos);

		pos = processSexField(token, majorContent, pos);

		processTimeStamp(majorContent, pos);

		ticket = generateTicket(majorContent);
		return ticket;
	}

	private static String generateTicket(byte[] majorContent) throws Exception {
		String ticket;
		byte[] mHash = EncryptionUtil.sha2sum(majorContent);
		byte[] encrypt = EncryptionUtil.encrypt(majorContent);

		byte[] ticketB = new byte[mHash.length + encrypt.length];
		System.arraycopy(mHash, 0, ticketB, 0, mHash.length);
		System.arraycopy(encrypt, 0, ticketB, mHash.length, encrypt.length);
		ticket = new String(Base64.encodeBase64(ticketB));
		//ticket = ticket.replaceAll("=", "#");
		return ticket;
	}

	private static void processTimeStamp(byte[] majorContent, int pos) {
		long now = System.currentTimeMillis();
		BitsUtil.putLong(majorContent, pos, now);
	}

	private static int processSexField(Token token, byte[] majorContent, int pos) {
		majorContent[pos] = (byte) token.getSex();
		pos++;
		return pos;
	}

	private static int processStringFields(Token token, byte[] majorContent,
			int pos) {
		for (byte i = 0; i < tokenFieldNames.length; i++) {
			pos = putStringFieldIntoTicket(tokenFieldNames[i], token,
					majorContent, pos);
		}
		return pos;
	}

	private static int processTokenType(Token token, byte[] majorContent) {
		int pos = 0;
		majorContent[pos] = (byte) token.getTokenType();
		pos++;
		return pos;
	}

	private static int putStringFieldIntoTicket(String fieldName, Token token,
			byte[] majorContent, int pos) {
		String getMethod = "get" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
		String value = (String) ReflectUtil.excuteClassMethod(token, getMethod,
				null, null);

		if (value != null && !value.equals("")) {
			pos = putStringField(majorContent, pos, value,
					fieldTypeMap.get(fieldName).byteValue());
		}
		return pos;
	}

	private static int putStringField(byte[] majorContent, int pos,
			String stringField, byte fieldType) {
		majorContent[pos] = fieldType;
		pos++;
		short length = (short) stringField.getBytes().length;
		BitsUtil.putShort(majorContent, pos, length);
		pos = pos + 2;
		putString(majorContent, pos, stringField);
		pos = pos + stringField.getBytes().length;
		return pos;
	}

	private static String getStringField(byte[] majorContent, int pos,
			short length) {
		byte[] resultBytes = new byte[length];
		for (int i = 0; i < length; i++) {
			resultBytes[i] = majorContent[pos + i];
		}
		return new String(resultBytes);
	}

	public static void main(String[] args) {
		Token token = new Token();
		token.setOpenId("oLVPpjqs9BhvzwPj5A-vTYAX3GLc");
		token.setNickName("·½±¶");
		// token.setProvince("Guangdong");
		token.setCity("Shenzhen");
		token.setCountry("CN");
		token.setSex(1);
		try {
			String ticket = getTicket(token);
			Token token1 = getToken(ticket);
			System.out.println(token1.equals(token));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Token getToken(String ticket) throws Exception {
		//ticket = ticket.replaceAll("#", "=");
		Token token = new Token();
		byte[] majorContent = parsePlainBytes(ticket);
		int pos = 0;
		if (majorContent[pos] == TICKET_TYPE_WECHAT) {
			pos++;

			pos = parseStringFields(token, majorContent, pos);

			pos = parseSexField(token, majorContent, pos);

			parseTimeStamp(majorContent, pos);
			return token;
		} else {
			throw new KLineAppException(ExceptionKeys.INVALID_KLINE_TICKET);
		}

	}

	private static void parseTimeStamp(byte[] majorContent, int pos)
			throws Exception {
		long timeStamp = BitsUtil.getLong(majorContent, pos);
		long now = System.currentTimeMillis();
		if (now - timeStamp > SESSION_LIVE_DURATION * 60 * 1000) {
			//throw new Exception(ExceptionKeys.INVALID_KLINE_TICKET);
		}
	}

	private static int parseSexField(Token token, byte[] majorContent, int pos) {
		token.setSex(majorContent[pos]);
		pos++;
		return pos;
	}

	private static int parseStringFields(Token token, byte[] majorContent,
			int pos) {
		for (byte i = 0; i < tokenFieldNames.length; i++) {
			pos = parseFileValueFromTicket(tokenFieldNames[i], token,
					majorContent, pos);
		}
		return pos;
	}

	private static byte[] parsePlainBytes(String ticket) throws Exception,
			KLineAppException {
		try {
			byte[] decodeBytes = Base64.decodeBase64(ticket.getBytes());
			byte[] hash = new byte[HASH_LENGTH];
			byte[] encrypt = new byte[decodeBytes.length - HASH_LENGTH];
			System.arraycopy(decodeBytes, 0, hash, 0, HASH_LENGTH);
			System.arraycopy(decodeBytes, HASH_LENGTH, encrypt, 0,
					decodeBytes.length - HASH_LENGTH);

			byte[] majorContent = EncryptionUtil.decrypt(encrypt);

			byte[] mHash = EncryptionUtil.sha2sum(majorContent);
			if (!Arrays.equals(hash, mHash)) {
				throw new KLineAppException(ExceptionKeys.INVALID_KLINE_TICKET);
			}
			return majorContent;
		} catch (NegativeArraySizeException ex) {
			throw new KLineAppException(ExceptionKeys.INVALID_KLINE_TICKET);
		}
	}

	private static int parseFileValueFromTicket(String fieldName, Token token,
			byte[] majorContent, int pos) {
		String setMethod = "set" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);

		byte fieldType = majorContent[pos];

		if (fieldType == fieldTypeMap.get(fieldName).byteValue()) {
			ParsingFieldBean field = parseFieldValue(majorContent, pos);
			pos = ParsingFieldBean.getPos();
			ReflectUtil.excuteClassMethodNoReturn(token, setMethod,
					new Class[] { String.class },
					new String[] { ParsingFieldBean.getFieldValue() });
		}
		return pos;

	}

	private static ParsingFieldBean parseFieldValue(byte[] majorContent, int pos) {
		ParsingFieldBean result = new ParsingFieldBean();
		pos++;
		short length = BitsUtil.getShort(majorContent, pos);
		pos += 2;
		String field = getStringField(majorContent, pos, length);
		pos += length;
		ParsingFieldBean.setFieldValue(field);
		ParsingFieldBean.setPos(pos);
		return result;
	}

	private static class ParsingFieldBean {
		private static int pos;

		public static int getPos() {
			return pos;
		}

		public static void setPos(int pos) {
			ParsingFieldBean.pos = pos;
		}

		private static String fieldValue;

		public static String getFieldValue() {
			return fieldValue;
		}

		public static void setFieldValue(String fieldValue) {
			ParsingFieldBean.fieldValue = fieldValue;
		}

	}
}
