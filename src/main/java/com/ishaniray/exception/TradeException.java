package com.ishaniray.exception;

public class TradeException extends RuntimeException {

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
}
