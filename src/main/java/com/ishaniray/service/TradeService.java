package com.ishaniray.service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ishaniray.dao.TradeDao;
import com.ishaniray.dto.Trade;
import com.ishaniray.exception.MaturedTradeException;
import com.ishaniray.exception.StaleTradeException;
import com.ishaniray.exception.TradeException;

@Service
public class TradeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TradeService.class);

	private TradeDao tradeDao;

	@Autowired
	public TradeService(TradeDao tradeDao) {
		this.tradeDao = tradeDao;
	}

	public void handleTrade(Trade trade) throws TradeException {
		if (trade.getMaturityDate().isBefore(LocalDate.now())) {
			MaturedTradeException mte = new MaturedTradeException(
					MessageFormat.format("{0} has already expired.", trade));
			LOGGER.error(mte.getMessage());
			throw mte;
		}

		Optional<Trade> latestTrade = tradeDao.fetchLatestTrade(trade.getTradeId());
		if (!latestTrade.isPresent()) {
			recordTrade(trade);
			return;
		}

		if (trade.getVersion() < latestTrade.get().getVersion()) {
			StaleTradeException ste = new StaleTradeException(
					MessageFormat.format("{0} has a newer version in the store.", trade));
			LOGGER.error(ste.getMessage());
			throw ste;
		}

		if (trade.getVersion() == latestTrade.get().getVersion()) {
			tradeDao.updateTrade(trade);
			LOGGER.debug(MessageFormat.format("{0} overwritten in store.", trade));
			return;
		}

		recordTrade(trade);
	}

	private void recordTrade(Trade trade) {
		tradeDao.insertTrade(trade);
		LOGGER.debug(MessageFormat.format("{0} recorded in store.", trade));
	}
}
