package com.ishaniray;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ishaniray.dao.TradeDao;
import com.ishaniray.dto.Trade;
import com.ishaniray.dto.builder.TradeBuilder;
import com.ishaniray.enums.ExpirationStatus;
import com.ishaniray.enums.TradeAction;
import com.ishaniray.exception.MaturedTradeException;
import com.ishaniray.exception.StaleTradeException;
import com.ishaniray.exception.TradeException;
import com.ishaniray.service.TradeService;

@SpringBootTest
class TradesBarcApplicationTests {

	private TradeService tradeService;

	private TradeDao tradeDao;

	@Autowired
	public TradesBarcApplicationTests(TradeService tradeService, TradeDao tradeDao) {
		this.tradeService = tradeService;
		this.tradeDao = tradeDao;
	}

	@Test
	@Transactional
	void handleIncomingTrade_thatHasAlreadyMatured() throws TradeException {
		Trade trade = new TradeBuilder().tradeId("T1").version(1).counterPartyId("CP-1").bookId("B1")
				.maturityDate(LocalDate.of(2021, 5, 20)).createdDate(LocalDate.now())
				.expirationStatus(ExpirationStatus.Y).build();
		Assertions.assertThrows(MaturedTradeException.class, () -> {
			tradeService.handleTrade(trade);
		});
	}

	@Test
	@Transactional
	void handleIncomingTrade_thatMaturesAtALaterDate_whenStoreIsEmpty() throws TradeException {
		Trade trade = new TradeBuilder().tradeId("T1").version(1).counterPartyId("CP-1").bookId("B1")
				.maturityDate(LocalDate.of(2022, 5, 20)).createdDate(LocalDate.now())
				.expirationStatus(ExpirationStatus.N).build();
		TradeAction tradeAction = tradeService.handleTrade(trade);
		Assertions.assertEquals(TradeAction.RECORDED, tradeAction);
	}

	@Test
	@Transactional
	void handleIncomingTrade_thatMaturesAtALaterDate_whenStoreAlreadyHasAHigherVersion() throws TradeException {
		Trade tradeV2 = new TradeBuilder().tradeId("T1").version(2).counterPartyId("CP-2").bookId("B2")
				.maturityDate(LocalDate.of(2022, 5, 20)).createdDate(LocalDate.now())
				.expirationStatus(ExpirationStatus.N).build();
		TradeAction tradeV2Action = tradeService.handleTrade(tradeV2);
		Assertions.assertEquals(TradeAction.RECORDED, tradeV2Action);

		Trade tradeV1 = new TradeBuilder().tradeId("T1").version(1).counterPartyId("CP-1").bookId("B1")
				.maturityDate(LocalDate.of(2022, 5, 20)).createdDate(LocalDate.now())
				.expirationStatus(ExpirationStatus.N).build();
		Assertions.assertThrows(StaleTradeException.class, () -> {
			tradeService.handleTrade(tradeV1);
		});
	}

	@Test
	@Transactional
	void handleIncomingTrade_thatMaturesAtALaterDate_whenStoreAlreadyHasTheSameVersion() throws TradeException {
		Trade tradeV1Existing = new TradeBuilder().tradeId("T1").version(1).counterPartyId("CP-2").bookId("B2")
				.maturityDate(LocalDate.of(2022, 5, 20)).createdDate(LocalDate.now())
				.expirationStatus(ExpirationStatus.N).build();
		TradeAction tradeV1ExistingAction = tradeService.handleTrade(tradeV1Existing);
		Assertions.assertEquals(TradeAction.RECORDED, tradeV1ExistingAction);

		Trade tradeV1New = new TradeBuilder().tradeId("T1").version(1).counterPartyId("CP-1").bookId("B1")
				.maturityDate(LocalDate.of(2022, 5, 20)).createdDate(LocalDate.now())
				.expirationStatus(ExpirationStatus.N).build();
		TradeAction tradeV1NewAction = tradeService.handleTrade(tradeV1New);
		Assertions.assertEquals(TradeAction.UPDATED, tradeV1NewAction);
	}

	@Test
	@Transactional
	void handleIncomingTrade_thatMaturesAtALaterDate_whenStoreHasALowerVersion() throws TradeException {
		Trade tradeV1 = new TradeBuilder().tradeId("T1").version(1).counterPartyId("CP-2").bookId("B2")
				.maturityDate(LocalDate.of(2022, 5, 20)).createdDate(LocalDate.now())
				.expirationStatus(ExpirationStatus.N).build();
		TradeAction tradeV1Action = tradeService.handleTrade(tradeV1);
		Assertions.assertEquals(TradeAction.RECORDED, tradeV1Action);

		Trade tradeV2 = new TradeBuilder().tradeId("T1").version(2).counterPartyId("CP-1").bookId("B1")
				.maturityDate(LocalDate.of(2022, 5, 20)).createdDate(LocalDate.now())
				.expirationStatus(ExpirationStatus.N).build();
		TradeAction tradeV2Action = tradeService.handleTrade(tradeV2);
		Assertions.assertEquals(TradeAction.RECORDED, tradeV2Action);
	}

	@Test
	@Transactional
	void handleIncomingTrade_thatMaturesToday_whenStoreIsEmpty() throws TradeException {
		Trade trade = new TradeBuilder().tradeId("T1").version(1).counterPartyId("CP-1").bookId("B1")
				.maturityDate(LocalDate.now()).createdDate(LocalDate.now()).expirationStatus(ExpirationStatus.N)
				.build();
		TradeAction tradeAction = tradeService.handleTrade(trade);
		Assertions.assertEquals(TradeAction.RECORDED, tradeAction);
	}

	@Test
	@Transactional
	void handleIncomingTrade_thatMaturesToday_whenStoreAlreadyHasAHigherVersion() throws TradeException {
		Trade tradeV2 = new TradeBuilder().tradeId("T1").version(2).counterPartyId("CP-2").bookId("B2")
				.maturityDate(LocalDate.now()).createdDate(LocalDate.now()).expirationStatus(ExpirationStatus.N)
				.build();
		TradeAction tradeV2Action = tradeService.handleTrade(tradeV2);
		Assertions.assertEquals(TradeAction.RECORDED, tradeV2Action);

		Trade tradeV1 = new TradeBuilder().tradeId("T1").version(1).counterPartyId("CP-1").bookId("B1")
				.maturityDate(LocalDate.now()).createdDate(LocalDate.now()).expirationStatus(ExpirationStatus.N)
				.build();
		Assertions.assertThrows(StaleTradeException.class, () -> {
			tradeService.handleTrade(tradeV1);
		});
	}

	@Test
	@Transactional
	void handleIncomingTrade_thatMaturesToday_whenStoreAlreadyHasTheSameVersion() throws TradeException {
		Trade tradeV1Existing = new TradeBuilder().tradeId("T1").version(1).counterPartyId("CP-2").bookId("B2")
				.maturityDate(LocalDate.now()).createdDate(LocalDate.now()).expirationStatus(ExpirationStatus.N)
				.build();
		TradeAction tradeV1ExistingAction = tradeService.handleTrade(tradeV1Existing);
		Assertions.assertEquals(TradeAction.RECORDED, tradeV1ExistingAction);

		Trade tradeV1New = new TradeBuilder().tradeId("T1").version(1).counterPartyId("CP-1").bookId("B1")
				.maturityDate(LocalDate.now()).createdDate(LocalDate.now()).expirationStatus(ExpirationStatus.N)
				.build();
		TradeAction tradeV1NewAction = tradeService.handleTrade(tradeV1New);
		Assertions.assertEquals(TradeAction.UPDATED, tradeV1NewAction);
	}

	@Test
	@Transactional
	void handleIncomingTrade_thatMaturesToday_whenStoreHasALowerVersion() throws TradeException {
		Trade tradeV1 = new TradeBuilder().tradeId("T1").version(1).counterPartyId("CP-2").bookId("B2")
				.maturityDate(LocalDate.now()).createdDate(LocalDate.now()).expirationStatus(ExpirationStatus.N)
				.build();
		TradeAction tradeV1Action = tradeService.handleTrade(tradeV1);
		Assertions.assertEquals(TradeAction.RECORDED, tradeV1Action);

		Trade tradeV2 = new TradeBuilder().tradeId("T1").version(2).counterPartyId("CP-1").bookId("B1")
				.maturityDate(LocalDate.now()).createdDate(LocalDate.now()).expirationStatus(ExpirationStatus.N)
				.build();
		TradeAction tradeV2Action = tradeService.handleTrade(tradeV2);
		Assertions.assertEquals(TradeAction.RECORDED, tradeV2Action);
	}

	@Test
	@Transactional
	void markExpiredTrades_when2TradesHaveExpired() throws TradeException {

		Trade trade1 = new TradeBuilder().tradeId("T1").version(1).counterPartyId("CP-1").bookId("B1")
				.maturityDate(LocalDate.of(2022, 3, 10)).createdDate(LocalDate.now())
				.expirationStatus(ExpirationStatus.N).build();
		Trade trade2 = new TradeBuilder().tradeId("T2").version(1).counterPartyId("CP-2").bookId("B2")
				.maturityDate(LocalDate.of(2022, 3, 10)).createdDate(LocalDate.now())
				.expirationStatus(ExpirationStatus.N).build();
		tradeDao.insertTrade(trade1);
		tradeDao.insertTrade(trade2);

		int markedTrades = tradeService.markExpiredTrades();
		Assertions.assertEquals(2, markedTrades);
	}
}
