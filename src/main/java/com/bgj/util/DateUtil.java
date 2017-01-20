package com.bgj.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static String formatDate(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return df.format(date);
	}

	
	public static String formatDay(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}
	
	public static String formatDayWithHourAndMinute(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return df.format(date);
	}

	
	public static String formatDay15(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 15");
		return df.format(date);
	}
	
	

	public static Date parseDay(String sDate) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date result = null;
		try {
			result = df.parse(sDate);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return result;
	}

	public static Date getNextDay(Date date) {
		return getNextNDay(date, 1);
	}

	private static Date getNextNDay(Date date, int n) {
		Date result = new Date(date.getTime() + 24 * 60 * 60 * 1000 * n);
		return result;
	}

	private static Date getPreviousNDay(Date date, int n) {
		Date result = new Date(date.getTime() - 24 * 60 * 60 * 1000 * n);
		return result;
	}

	public static Date getNext2MarketOpenDay(Date date) {
		Date result = getNextNDay(date, 2);
		while (!StockMarketUtil.isMarketOpen(result)) {
			result = getNextNDay(date, 1);
		}
		return result;
	}

	public static Date getNextMarketOpenDay(Date date) {
		return getNextMarketOpenDay(date, 1);
	}

	public static Date getPrevious3MarketOpenDay(Date date) {
		Date result = getPreviousNDay(date, 3);
		while (!StockMarketUtil.isMarketOpen(result)) {
			result = getPreviousNDay(date, 1);
		}
		return result;
	}

	public static Date getPreviousMarketOpenDay(Date date, int n) {
		if (n == 0) {
			return date;
		}
		Date result = null;
		for (int i = 1; i <= n; i++) {
			result = getPreviousNDay(date, 1);
			if (!StockMarketUtil.isMarketOpen(result)) {
				i--;
			}
			date = result;
		}
		return result;
	}

	public static Date getLatestMarketOpenDay(Date date) {
		Date result = date;
		while (!StockMarketUtil.isMarketOpen(result)) {
			result = getPreviousNDay(result, 1);
		}
		return result;
	}
	
	public static Date getLatestMarketCloseDay() {
		Date date = new Date();
		if(StockMarketUtil.isBeforeMarketAfternoonClosing()){
			date = getPreviousNDay(date,1);
		}
		
		Date result = date;
		while (!StockMarketUtil.isMarketOpen(result)) {
			result = getPreviousNDay(result, 1);
		}
		return result;
	}
	

	public static Date getNextMarketOpenDay(Date date, int n) {
		if (n == 0) {
			return date;
		}
		Date result = null;
		for (int i = 1; i <= n; i++) {
			result = getNextNDay(date, 1);
			if (!StockMarketUtil.isMarketOpen(result)) {
				i--;
			}
			date = result;
		}
		return result;
	}

	public static Date getDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date date = calendar.getTime();
		return date;
	}

	public static boolean isToday(Date date) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.setTime(date);
		int year1 = calendar.get(Calendar.YEAR);
		int month1 = calendar.get(Calendar.MONTH);
		int day1 = calendar.get(Calendar.DAY_OF_MONTH);
		return year == year1 && month == month1 && day == day1;
	}

	public static int getCurrentYear() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		return year;
	}

	public static int getCurrentYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		return year;
	}

	public static void main(String[] args) {
		 Date now = new Date();
		 System.out.println(DateUtil.formatDayWithHourAndMinute(now));
//		Date now = DateUtil.getDate(2014, 1, 14);
//		System.out.println(DateUtil.getPreviousMarketOpenDay(now, 5));

	}

	public static Date getCentainTime(int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		Date result = calendar.getTime();
		return result;
	}
}
