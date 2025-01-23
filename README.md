# TRADER

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.5-brightgreen)
![Maven](https://img.shields.io/badge/Maven-Build-blue)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-lightgrey)
![Docker](https://img.shields.io/badge/Docker-Supported-blue)

## Overview

This project is a **Spring Boot-based trading bot** that interacts with the **Binance API** to fetch market data, evaluate trading rules, and execute trades. The bot supports various trading strategies, including moving average crossovers, RSI-based strategies, and Fibonacci retracement levels.

---

## Features

1. **Binance API Integration**:

   - Fetch real-time market data (e.g., candlesticks, prices).
   - Execute trades based on predefined rules.

2. **Trading Rules**:

   - **Moving Average Crossovers**: MA5 crosses MA8, MA8 crosses MA21.
   - **RSI with Volume Increase**: Buy when RSI is between 30-40 and volume increases.
   - **Fibonacci Retracement**: Identify support/resistance levels for trading.

3. **Scheduled Price Checking**:

   - Periodically fetch market data and evaluate trading rules.

4. **Order Management**:

   - Save, retrieve, and delete execution orders.
   - Prevent duplicate orders.

5. **Exception Handling**:

   - Custom exceptions for duplicate orders and order not found.
   - Global exception handler for consistent error responses.

6. **Environment Configuration**:
   - Load API keys and sensitive data from environment variables.

---

## Prerequisites

Before running the project, ensure you have the following:

1. **Java Development Kit (JDK)**:

   - Version 17 or higher.

2. **Maven**:

   - For building and managing dependencies.

3. **Binance Account**:

   - Create an account on [Binance](https://www.binance.com/).
   - Generate API keys with trading permissions.

4. **Environment Variables**:

   - Create a `.env` file in the root directory with the following variables:
     ```env
     BINANCE_API_KEY="your_api_key"
     BINANCE_API_SECRET="your_api_secret"
     DB_URL="jdbc:postgresql://localhost:5432/tradingdb"
     DB_USERNAME="ADMIN"
     DB_PASSWORD="PASS"
     ```

   ```

   ```

---

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/melihcanaydin/TRADER.git
cd trading-bot
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

### 4. Access the API

- The application will start on port `8080`.
- Use tools like **Postman** or **cURL** to interact with the API.

---

## API Endpoints

### 1. Get Current Price

- **Endpoint**: `GET /api/price`
- **Parameters**:
  - `symbol`: The trading pair (e.g., `BTCUSDT`).
- **Response**:
  ```json
  {
    "symbol": "BTCUSDT",
    "price": 42000.0
  }
  ```

### 2. Get Historical Candlesticks

- **Endpoint**: `GET /api/candlesticks`
- **Parameters**:
  - `symbol`: The trading pair (e.g., `BTCUSDT`).
  - `interval`: The candlestick interval (e.g., `DAILY`).
  - `limit`: The number of candlesticks to fetch.
- **Response**:
  ```json
  [
    {
      "openTime": 1638316800000,
      "closeTime": 1638403200000,
      "open": "42000.0",
      "close": "43000.0",
      "high": "43500.0",
      "low": "41500.0",
      "volume": "1000.0"
    }
  ]
  ```

### 3. Ping Binance Server

- **Endpoint**: `GET /api/ping`
- **Response**:
  ```
  Ping Successful!
  ```

---

## Trading Rules

### 1. MA5 Crosses MA8

- **Description**: Buy when the 5-period moving average crosses below the 8-period moving average.
- **Rule Class**: `MA5CrossesMA8Rule.java`

### 2. MA8 Crosses MA21

- **Description**: Buy when the 8-period moving average crosses above the 21-period moving average.
- **Rule Class**: `MA8CrossesMA21Rule.java`

### 3. RSI with Volume Increase

- **Description**: Buy when RSI is between 30-40 and volume increases.
- **Rule Class**: `RSIWithVolumeIncreaseRule.java`

---

## Database Schema

The project uses the following entities:

### 1. ExecutionOrder

- Represents an execution order.
- Fields: `id`, `symbol`, `orderType`, `price`, `quantity`, `timestamp`.

### 2. MarketData

- Represents market data.
- Fields: `id`, `symbol`, `interval`, `closePrice`, `highPrice`, `lowPrice`, `volume`, `movingAverage5`, `movingAverage8`, `movingAverage21`, `rsi`, `createdAt`.

### 3. Trade

- Represents a trade.
- Fields: `id`, `symbol`, `price`, `action`.

---

## Environment Variables

The following environment variables are required:

| Variable             | Description         |
| -------------------- | ------------------- |
| `BINANCE_API_KEY`    | Binance API key.    |
| `BINANCE_API_SECRET` | Binance API secret. |

---

## Scheduled Tasks

The `PriceCheckerService` runs every **60 seconds** to:

1. Fetch market data for all supported coins.
2. Calculate technical indicators (e.g., RSI, moving averages, Fibonacci levels).
3. Evaluate trading rules and generate buy/sell signals.

---

## Error Handling

The bot handles the following exceptions:

1. **DuplicateOrderException**: Thrown when a duplicate order is detected.
2. **OrderNotFoundException**: Thrown when an order is not found.
3. **Generic Exception**: Handles all other unexpected errors.

---

## Contributing

Contributions are welcome! Follow these steps:

1. Fork the repository.
2. Create a new branch for your feature/bugfix.
3. Submit a pull request.

---
