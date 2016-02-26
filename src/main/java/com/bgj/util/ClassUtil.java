package com.bgj.util;

public class ClassUtil {
	public static Class getClass(String className) {
		if (className.equals("int")) {
			return int.class;
		}
		if (className.equals("double")) {
			return double.class;
		}
		if (className.equals("boolean")) {
			return boolean.class;
		}
		return String.class;
	}

	public static Object getObject(String className, String field) {
		if (className.equals("int")) {
			return Integer.parseInt(field);
		}
		if (className.equals("double")) {
			return Double.parseDouble(field);
		}
		if (className.equals("boolean")) {
			return Boolean.parseBoolean(field);
		}
		return field;
	}

	public static String getDefaultValue(String className, String field) {
		if (className.equals("int") && (field == null || "".equals(field))) {
			return "0";
		}
		if (className.equals("double") && (field == null || "".equals(field))) {
			return "0";
		}
		return field;
	}

}
