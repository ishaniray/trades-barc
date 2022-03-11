package com.ishaniray.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.ishaniray.dto.Trade;

@Component
public class TradeDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(TradeDao.class);

	private JdbcTemplate jdbcTemplate;

	private static final String INSERT_SQL = "INSERT INTO Trades "
			+ "(TradeId, Version, CounterPartyId, BookId, MaturityDate, CreatedDate, Expired) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

	private static final String UPDATE_SQL = "UPDATE Trades SET CounterPartyId = ?, BookId = ?, "
			+ "MaturityDate = ?, CreatedDate = ?, Expired = ? WHERE TradeId = ? AND Version = ?";

	private static final String EXPIRE_SQL = "UPDATE TRADES SET Expired = 'Y' WHERE TradeId = ? AND Version = ?";

	@Autowired
	public TradeDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void insertTrade(Trade trade) {
		jdbcTemplate.update(INSERT_SQL, trade.getTradeId(), trade.getVersion(), trade.getCounterPartyId(),
				trade.getBookId(), trade.getMaturityDate(), trade.getCreatedDate(),
				trade.getExpirationStatus().toString());

		LOGGER.debug("Trade [id = {}, version = {}] recorded.", trade.getTradeId(), trade.getVersion());
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
}
