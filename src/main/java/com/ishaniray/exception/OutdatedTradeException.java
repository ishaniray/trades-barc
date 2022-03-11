package com.ishaniray.exception;

public class OutdatedTradeException extends TradeException {

	private static final long serialVersionUID = 1L;

	public OutdatedTradeException() {
		super();
	}

	public OutdatedTradeException(String message) {
		super(message);
	}

	public OutdatedTradeException(String message, Throwable cause) {
		super(message, cause);
	}
}
