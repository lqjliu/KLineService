package com.bgj.util;

public class StringUtil {

	public static String getDefaultNumberWithZero(String number) {
		if (number == null || number.trim().equals("")) {
			return "0";
		} else {
			return number;
		}
	}

	public static String makeFirstLetterToUpper(String fieldName) {
		return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}

}
