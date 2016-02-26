package com.bgj.exception;

public class KLineException extends Exception{
	public KLineException(String message) {
		super(message);
	}

	public KLineException(String message, Exception ex) {
		super(message, ex);
	}
}
