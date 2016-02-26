package com.bgj.util;

import org.apache.log4j.Logger;

public class TimeUtil {
	private static Logger logger = Logger
			.getLogger(TimeUtil.class);

	public static void pause(long minisecond) {
		try {
			Thread.sleep(minisecond);
		} catch (Exception ex) {
			logger.error("Pause throw exception : ", ex);
		}
	}

	public static void pauseSeconds(long second) {
		pause(second * 1000);
	}

}
