package com.common.util;

public class OSUtil {
	public static String getOSSeparator() {
		if (System.getProperty("os.name").startsWith("Win")) {
			return "\\";
		} else {
			return "/";
		}
	}

}
