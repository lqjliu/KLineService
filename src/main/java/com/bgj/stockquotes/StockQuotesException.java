package com.bgj.stockquotes;

public class StockQuotesException extends Exception {
	private static final long serialVersionUID = 349813282542703507L;
	private Exception rootException;
	private String message;

	public StockQuotesException(String message, Exception rootException) {
		this.message = message;
		this.rootException = rootException;
	}

	public StockQuotesException(String message) {
		this.message = message;
	}

	public StockQuotesException(Exception rootException) {
		this.rootException = rootException;
	}

	public void printStackTrace() {
		if (rootException != null) {
			rootException.printStackTrace();
		}
		if (message != null) {
			System.out.print("Message = " + message);
		}
	}
}
