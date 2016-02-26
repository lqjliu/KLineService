package com.bgj.util;

public class MiscUtil {
	public static boolean isComment(String line) {
		return line.indexOf("#") == 0;
	}
}
