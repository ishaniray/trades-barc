package com.ishaniray.exception;

public class MaturedTradeException extends TradeException {

	private static final long serialVersionUID = 1L;

	public MaturedTradeException() {
		super();
	}

	public MaturedTradeException(String message) {
		super(message);
	}

	public MaturedTradeException(String message, Throwable cause) {
		super(message, cause);
	}
}
