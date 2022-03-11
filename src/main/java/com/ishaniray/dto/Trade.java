package com.ishaniray.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.ishaniray.enums.ExpirationStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class Trade implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String tradeId;

	private final int version;

	private final String counterPartyId;

	private final String bookId;

	private final LocalDate maturityDate;

	private final LocalDate createdDate;

	private final ExpirationStatus expirationStatus;
}
