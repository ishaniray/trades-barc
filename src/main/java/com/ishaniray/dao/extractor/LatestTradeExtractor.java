package com.ishaniray.dao.extractor;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.ishaniray.dto.Trade;
import com.ishaniray.dto.builder.TradeBuilder;
import com.ishaniray.enums.ExpirationStatus;

@Component
public class LatestTradeExtractor implements ResultSetExtractor<Trade> {

	@Override
	public Trade extractData(ResultSet rs) throws SQLException, DataAccessException {
		rs.next();
		return new TradeBuilder().tradeId(rs.getString("TradeId")).version(rs.getInt("Version"))
				.counterPartyId(rs.getString("CounterPartyId")).bookId(rs.getString("BookId"))
				.maturityDate(rs.getDate("MaturityDate").toLocalDate())
				.createdDate(rs.getDate("CreatedDate").toLocalDate())
				.expirationStatus(ExpirationStatus.valueOf(rs.getString("Expired"))).build();
	}
}
