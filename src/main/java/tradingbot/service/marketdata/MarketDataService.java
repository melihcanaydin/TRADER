package tradingbot.service.marketdata;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;

@Service
public class MarketDataService {

    private static final Logger logger = LoggerFactory.getLogger(MarketDataService.class);
    private final BinanceApiRestClient binanceApiClient;

    public MarketDataService(BinanceApiRestClient binanceApiClient) {
        this.binanceApiClient = binanceApiClient;
    }

    public List<Candlestick> getCandlesticks(String symbol, CandlestickInterval interval) {
        logger.info("Fetching candlestick data for {} with interval {}", symbol, interval);
        try {
            return binanceApiClient.getCandlestickBars(symbol, interval);
        } catch (Exception e) {
            logger.error("Failed to fetch candlesticks for {}: {}", symbol, e.getMessage(), e);
            throw e;
        }
    }

    public double getCurrentPrice(String symbol) {
        try {
            return Double.parseDouble(binanceApiClient.getPrice(symbol).getPrice());
        } catch (Exception e) {
            logger.error("Failed to fetch current price for {}: {}", symbol, e.getMessage(), e);
            throw e;
        }
    }

    public double getRSI(String symbol) {
        List<Candlestick> candlesticks = binanceApiClient.getCandlestickBars(symbol, CandlestickInterval.DAILY, 16, null, null);

        if (candlesticks.size() < 14 + 1) {
            logger.info("Insufficient data for RSI calculation for symbol: " + symbol);
            return -1.0;
        }

        List<Double> closes = candlesticks.stream()
                .map(c -> Double.parseDouble(c.getClose()))
                .collect(Collectors.toList());
        return calculateRSI(closes);
    }

    private double calculateRSI(List<Double> closes) {
        if (closes == null || closes.size() < 2) {
            throw new IllegalArgumentException("At least two closing prices are required to calculate RSI.");
        }

        int period = 14;
        if (closes.size() < period + 1) {
            throw new IllegalArgumentException("Not enough data points to calculate RSI for the given period.");
        }

        double[] changes = new double[closes.size() - 1];
        for (int i = 1; i < closes.size(); i++) {
            changes[i - 1] = closes.get(i) - closes.get(i - 1);
        }

        double[] gains = new double[changes.length];
        double[] losses = new double[changes.length];
        for (int i = 0; i < changes.length; i++) {
            if (changes[i] > 0) {
                gains[i] = changes[i];
            } else {
                losses[i] = -changes[i];
            }
        }

        double averageGain = 0;
        double averageLoss = 0;
        for (int i = 0; i < period; i++) {
            averageGain += gains[i];
            averageLoss += losses[i];
        }
        averageGain /= period;
        averageLoss /= period;

        for (int i = period; i < changes.length; i++) {
            averageGain = (averageGain * (period - 1) + gains[i]) / period;
            averageLoss = (averageLoss * (period - 1) + losses[i]) / period;
        }

        double rs = averageLoss == 0 ? Double.POSITIVE_INFINITY : averageGain / averageLoss;

        return 100 - (100 / (1 + rs));
    }

    public double calculateVolume(List<Candlestick> candlesticks) {
        return candlesticks.stream().mapToDouble(c -> Double.parseDouble(c.getVolume())).sum();
    }

    public double calculatePreviousVolume(List<Candlestick> candlesticks) {
        if (candlesticks.size() < 2) {
            logger.warn("Not enough data for previous volume");
            return 0;
        }
        return Double.parseDouble(candlesticks.get(candlesticks.size() - 2).getVolume());
    }

    public double calculateMovingAverage(List<Candlestick> candlesticks, int period) {
        if (candlesticks.size() < period) {
            logger.warn("Not enough data for {}-period moving average", period);
            return 0;
        }
        return candlesticks.subList(candlesticks.size() - period, candlesticks.size())
                .stream()
                .mapToDouble(c -> Double.parseDouble(c.getClose()))
                .average()
                .orElse(0);
    }

    public double calculatePreviousMovingAverage(List<Candlestick> candlesticks, int period) {
        if (candlesticks.size() < period + 1) {
            logger.warn("Not enough data for previous {}-period moving average", period);
            return 0;
        }
        return candlesticks.subList(candlesticks.size() - period - 1, candlesticks.size() - 1)
                .stream()
                .mapToDouble(c -> Double.parseDouble(c.getClose()))
                .average()
                .orElse(0);
    }

    public double[] calculateFibonacciLevels(List<Candlestick> candlesticks) {
        if (candlesticks.isEmpty()) {
            logger.warn("No candlesticks available for Fibonacci calculation");
            return new double[0];
        }

        double high = candlesticks.stream()
                .mapToDouble(c -> Double.parseDouble(c.getHigh()))
                .max()
                .orElse(0);
        double low = candlesticks.stream()
                .mapToDouble(c -> Double.parseDouble(c.getLow()))
                .min()
                .orElse(0);

        double diff = high - low;
        return new double[]{
            high,
            high - 0.236 * diff,
            high - 0.382 * diff,
            high - 0.5 * diff,
            high - 0.618 * diff,
            low
        };
    }
}
