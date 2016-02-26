package com.bgj.util;

import java.math.BigDecimal;

public class MathUtil {
	
	public static int getNSquareM(int n, int m) {
		int result = 1;
		for (int i = 0; i < m; i++) {
			result *= n;
		}
		return result;
	}

	public static boolean isInt(String abc) {
		try {
			Integer.parseInt(abc);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public static boolean isFloat(String abc) {
		try {
			Float.parseFloat(abc);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public static final int TYPE_QUANSHE = 1;
	public static final int TYPE_QUANJIN = 2;
	public static final int TYPE_SISHEWURU = 3;

	private static double formatDouble(double abc, int servedNumber, int type) {
		BigDecimal bd = new BigDecimal(abc);
		int flag = bd.ROUND_HALF_UP;
		if (type == TYPE_QUANSHE) {
			flag = bd.ROUND_DOWN;
		} else if (type == TYPE_QUANJIN) {
			flag = bd.ROUND_UP;
		}

		BigDecimal bd1 = bd.setScale(servedNumber, flag);
		double result = bd1.doubleValue();
		return result;
	}

	public static double formatDoubleWith4(double abc) {
		return formatDouble(abc, 4, TYPE_SISHEWURU);
	}

	public static double formatDoubleWith2(double abc) {
		return formatDouble(abc, 2, TYPE_SISHEWURU);
	}

	public static double formatDoubleWith2QuanShe(double abc) {
		return formatDouble(abc, 2, TYPE_QUANSHE);
	}

	public static double formatDoubleWith2QuanJin(double abc) {
		return formatDouble(abc, 2, TYPE_QUANJIN);
	}

}
