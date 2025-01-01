CREATE TABLE market_data (
    id BIGSERIAL PRIMARY KEY,
    symbol VARCHAR(10) NOT NULL,
    interval VARCHAR(10),
    close_price DOUBLE PRECISION,
    high_price DOUBLE PRECISION,
    low_price DOUBLE PRECISION,
    volume DOUBLE PRECISION,
    moving_average_5 DOUBLE PRECISION,
    moving_average_8 DOUBLE PRECISION,
    moving_average_21 DOUBLE PRECISION,
    rsi DOUBLE PRECISION,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);