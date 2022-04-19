package com.pm.aiost.misc.utils;

public class StringUtils {

	public static boolean isInteger(String s, int radix) {
		if (s.isEmpty())
			return false;
		for (int i = 0; i < s.length(); i++) {
			if (i == 0 && s.charAt(i) == '-') {
				if (s.length() == 1)
					return false;
				else
					continue;
			}
			if (Character.digit(s.charAt(i), radix) < 0)
				return false;
		}
		return true;
	}

	public static boolean isInteger(String str) {
		// if(str.matches("-?\\d+(\\.\\d+)?"))
		if (str.matches("^-?\\d{1,8}$"))
			return true;
		return false;
	}

	public static boolean isDouble(String str) {
		// if(str.matches("^-?\\d{1,23}\\.\\d{1,8}$"))
		if (str.matches("(?=^.{1,30}$)^-?\\d{1,23}\\.\\d{1,8}$"))
			return true;
		return false;
	}

	// probably wrong!
	public static boolean isFloat(String str) {
		if (str.matches("^-?\\d{1,9}\\.\\d{1,9}$"))
			return true;
		return false;
	}

	public static boolean isShort(String str) {
		if (str.matches("^-?\\d{1,4}$"))
			return true;
		return false;
	}

	public static boolean isByte(String str) {
		if (str.matches("^-?\\d{1,2}$"))
			return true;
		return false;
	}

	public static String fillSpaceBeforeUpperCase(String text) {
		StringBuilder stringBuilder = new StringBuilder();
		int size = text.length();
		for (int i = 0; i < size; i++) {
			char currentChar = text.charAt(i);
			if (Character.isUpperCase(currentChar))
				stringBuilder.append(' ');
			stringBuilder.append(currentChar);
		}
		return stringBuilder.toString();
	}
}
