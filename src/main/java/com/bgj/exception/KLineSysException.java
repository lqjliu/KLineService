package com.bgj.exception;

public class KLineSysException extends KLineException {
	public KLineSysException(String message) {
		super(message);
	}

	public KLineSysException(String message, Exception ex) {
		super(message, ex);
	}

}
