package com.ishaniray.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ishaniray.dao.TradeDao;

@Service
public class TradeService {

	private TradeDao tradeDao;

	@Autowired
	public TradeService(TradeDao tradeDao) {
		this.tradeDao = tradeDao;
	}
}
