CREATE TABLE IF NOT EXISTS Trades (
	TradeId VARCHAR(255),
	Version INTEGER,
	CounterPartyId VARCHAR(255),
	BookId VARCHAR(255),
	MaturityDate DATE,
	CreatedDate DATE,
	Expired ENUM('Y', 'N'),
	PRIMARY KEY (TradeId, Version)
);
