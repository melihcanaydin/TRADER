package tradingbot.service.marketdata;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;

import tradingbot.dto.CandlestickDto;
import tradingbot.model.Coin;
import tradingbot.model.MarketData;
import tradingbot.repository.MarketDataRepository;
import tradingbot.service.trading.BinanceService;

@Service
public class MarketDataService {

    private static final Logger logger = LoggerFactory.getLogger(MarketDataService.class);

    private final BinanceApiRestClient binanceApiClient;
    private final MarketDataRepository marketDataRepository;
    private final BinanceService binanceService;

    public MarketDataService(BinanceApiRestClient binanceApiClient,
            MarketDataRepository marketDataRepository, BinanceService binanceService) {
        this.binanceApiClient = binanceApiClient;
        this.marketDataRepository = marketDataRepository;
        this.binanceService = binanceService;
    }

    public double getCurrentPrice(String symbol) {
        try {
            logger.info("Fetching current price for {}", symbol);
            return Double.parseDouble(binanceApiClient.getPrice(symbol).getPrice());
        } catch (Exception e) {
            logger.error("Error fetching current price for {}: {}", symbol, e.getMessage());
            throw new RuntimeException("Failed to fetch current price for " + symbol, e);
        }
    }

    public List<Candlestick> getCandlesticks(String symbol, CandlestickInterval interval) {
        try {
            logger.info("Fetching candlestick data for {} with interval {}", symbol, interval);
            return binanceApiClient.getCandlestickBars(symbol, interval);
        } catch (Exception e) {
            logger.error("Error fetching candlestick data for {}: {}", symbol, e.getMessage());
            throw new RuntimeException("Failed to fetch candlestick data: " + e.getMessage(), e);
        }
    }

    public List<MarketData> getMarketData(Coin coin) {
        // Invalidate Caches here in the future to hold info on DB


        logger.warn("Insufficient market data in DB for {}. Fetching from Binance API.",
                coin.name());

        List<CandlestickDto> candlestickDtos = binanceService
                .getHistoricalCandlesticks(coin.name(), CandlestickInterval.DAILY, 201).stream()
                .sorted((a, b) -> Long.compare(b.getOpenTime(), a.getOpenTime()))
                .collect(Collectors.toList());

        List<MarketData> parsedMarketData = candlestickDtos.stream()
                .map(dto -> new MarketData(coin.name(), dto.getOpenTime(),
                        Double.parseDouble(dto.getOpen()), Double.parseDouble(dto.getHigh()),
                        Double.parseDouble(dto.getLow()), Double.parseDouble(dto.getClose()),
                        Double.parseDouble(dto.getVolume()), dto.getCloseTime()))
                .collect(Collectors.toList());

        // saveMarketData(parsedMarketData);
        return parsedMarketData;

    }

    public void saveMarketData(List<MarketData> marketDataList) {
        marketDataRepository.saveAll(marketDataList);
    }
}
