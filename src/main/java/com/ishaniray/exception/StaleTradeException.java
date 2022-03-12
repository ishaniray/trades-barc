package com.ishaniray.exception;

public class StaleTradeException extends TradeException {

	private static final long serialVersionUID = 1L;

	public StaleTradeException() {
		super();
	}

	public StaleTradeException(String message) {
		super(message);
	}

	public StaleTradeException(String message, Throwable cause) {
		super(message, cause);
	}

	public StaleTradeException(Throwable cause) {
		super(cause);
	}
}
