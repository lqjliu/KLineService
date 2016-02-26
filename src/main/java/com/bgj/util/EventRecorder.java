package com.bgj.util;

import org.apache.log4j.Logger;


public class EventRecorder {
	private static Logger logger = Logger.getLogger(EventRecorder.class);

	public static void recordEvent(Class happenedClass, String message) {
		logger.info(happenedClass + message);
	}
}
