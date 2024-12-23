CREATE TABLE trades (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    symbol VARCHAR(255),
    buyPrice DOUBLE,
    sellPrice DOUBLE,
    profit DOUBLE
);
