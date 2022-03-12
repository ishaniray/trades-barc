package com.ishaniray.exception;

public class TradeException extends Exception {

	private static final long serialVersionUID = 1L;

	public TradeException() {
		super();
	}

	public TradeException(String message) {
		super(message);
	}

	public TradeException(String message, Throwable cause) {
		super(message, cause);
	}

	public TradeException(Throwable cause) {
		super(cause);
	}
}
