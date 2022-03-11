package com.ishaniray.dto.builder;

import java.time.LocalDate;

import com.ishaniray.dto.Trade;
import com.ishaniray.enums.ExpirationStatus;

public class TradeBuilder {

	private String tradeId;

	private int version;

	private String counterPartyId;

	private String bookId;

	private LocalDate maturityDate;

	private LocalDate createdDate;

	private ExpirationStatus expirationStatus;

	public TradeBuilder tradeId(String tradeId) {
		this.tradeId = tradeId;
		return this;
	}

	public TradeBuilder version(int version) {
		this.version = version;
		return this;
	}

	public TradeBuilder counterPartyId(String counterPartyId) {
		this.counterPartyId = counterPartyId;
		return this;
	}

	public TradeBuilder bookId(String bookId) {
		this.bookId = bookId;
		return this;
	}

	public TradeBuilder maturityDate(LocalDate maturityDate) {
		this.maturityDate = maturityDate;
		return this;
	}

	public TradeBuilder createdDate(LocalDate createdDate) {
		this.createdDate = createdDate;
		return this;
	}

	public TradeBuilder expirationStatus(ExpirationStatus expirationStatus) {
		this.expirationStatus = expirationStatus;
		return this;
	}

	public Trade build() {
		return new Trade(tradeId, version, counterPartyId, bookId, maturityDate, createdDate, expirationStatus);
	}
}
