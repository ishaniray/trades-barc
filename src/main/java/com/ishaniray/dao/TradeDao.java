package com.ishaniray.dao;

import java.text.MessageFormat;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.ishaniray.dao.extractor.LatestTradeExtractor;
import com.ishaniray.dto.Trade;

@Component
public class TradeDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(TradeDao.class);

	private JdbcTemplate jdbcTemplate;

	private LatestTradeExtractor latestTradeExtractor;

	private static final String INSERT_SQL = "INSERT INTO Trades "
			+ "(TradeId, Version, CounterPartyId, BookId, MaturityDate, CreatedDate, Expired) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

	private static final String UPDATE_SQL = "UPDATE Trades SET CounterPartyId = ?, BookId = ?, "
			+ "MaturityDate = ?, CreatedDate = ?, Expired = ? WHERE TradeId = ? AND Version = ?";

	private static final String EXPIRE_SQL = "UPDATE Trades SET Expired = 'Y' WHERE TradeId = ? AND Version = ?";

	private static final String FETCH_LATEST_SQL = "SELECT * FROM Trades WHERE TradeId = ? "
			+ "ORDER BY Version DESC LIMIT 1";

	@Autowired
	public TradeDao(JdbcTemplate jdbcTemplate, LatestTradeExtractor latestTradeExtractor) {
		this.jdbcTemplate = jdbcTemplate;
		this.latestTradeExtractor = latestTradeExtractor;
	}

	public void insertTrade(Trade trade) {
		jdbcTemplate.update(INSERT_SQL, trade.getTradeId(), trade.getVersion(), trade.getCounterPartyId(),
				trade.getBookId(), trade.getMaturityDate(), trade.getCreatedDate(),
				trade.getExpirationStatus().toString());

		LOGGER.debug("Trade [id = {}, version = {}] inserted.", trade.getTradeId(), trade.getVersion());
	}

	public void updateTrade(Trade trade) {
		jdbcTemplate.update(UPDATE_SQL, trade.getCounterPartyId(), trade.getBookId(), trade.getMaturityDate(),
				trade.getCreatedDate(), trade.getExpirationStatus().toString(), trade.getTradeId(), trade.getVersion());

		LOGGER.debug("Trade [id = {}, version = {}] updated.", trade.getTradeId(), trade.getVersion());
	}

	public void expireTrade(String tradeId, int version) {
		jdbcTemplate.update(EXPIRE_SQL, tradeId, version);

		LOGGER.debug("Trade [id = {}, version = {}] marked as expired.", tradeId, version);
	}

	public Optional<Trade> fetchLatestTrade(String tradeId) {
		Optional<Trade> trade = jdbcTemplate.query(FETCH_LATEST_SQL, latestTradeExtractor);

		String logMessage = trade.isPresent()
				? MessageFormat.format("Latest trade for TradeId = {0} fetched: {1}", tradeId, trade.get().toString())
				: MessageFormat.format("No trade found in the store for TradeId = {0}", tradeId);
		LOGGER.debug(logMessage);

		return trade;
	}
}
