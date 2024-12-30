package tradingbot.service.marketdata;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;

@Service
public class MarketDataService {

    private final BinanceApiRestClient binanceClient;

    public MarketDataService(BinanceApiRestClient binanceClient) {
        this.binanceClient = binanceClient;
    }

    public double getCurrentPrice(String symbol) {
        return Double.parseDouble(binanceClient.getPrice(symbol).getPrice());
    }

    public double getRSI(String symbol) {
        List<Candlestick> candlesticks = binanceClient.getCandlestickBars(symbol, CandlestickInterval.DAILY, 14, null, null);

        if (candlesticks.size() < 14 + 1) { //TODO:MCA How to calculate it properly
            System.err.println("Insufficient data for RSI calculation for symbol: " + symbol);
            return -1.0;
        }

        // Extract closing prices and calculate RSI
        List<Double> closes = candlesticks.stream()
                .map(c -> Double.parseDouble(c.getClose()))
                .collect(Collectors.toList());
        return calculateRSI(closes);
    }

    public double getVolume(String symbol) {
        List<Candlestick> candlesticks = binanceClient.getCandlestickBars(symbol, CandlestickInterval.DAILY, 1, null, null);
        return Double.parseDouble(candlesticks.get(0).getVolume());
    }

    public double getPreviousVolume(String symbol) {
        List<Candlestick> candlesticks = binanceClient.getCandlestickBars(symbol, CandlestickInterval.DAILY, 2, null, null);
        return Double.parseDouble(candlesticks.get(0).getVolume());
    }

    public double getMovingAverage(String symbol, int period) {
        List<Candlestick> candlesticks = binanceClient.getCandlestickBars(symbol, CandlestickInterval.DAILY, period, null, null);
        return candlesticks.stream().mapToDouble(c -> Double.parseDouble(c.getClose())).average().orElse(0.0);
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

        // Separate gains and losses
        double[] gains = new double[changes.length];
        double[] losses = new double[changes.length];
        for (int i = 0; i < changes.length; i++) {
            if (changes[i] > 0) {
                gains[i] = changes[i];
            } else {
                losses[i] = -changes[i];
            }
        }

        // Calculate the average gain and loss for the initial period
        double averageGain = 0;
        double averageLoss = 0;
        for (int i = 0; i < period; i++) {
            averageGain += gains[i];
            averageLoss += losses[i];
        }
        averageGain /= period;
        averageLoss /= period;

        // Smooth the averages using the Wilder's smoothing technique
        for (int i = period; i < changes.length; i++) {
            averageGain = (averageGain * (period - 1) + gains[i]) / period;
            averageLoss = (averageLoss * (period - 1) + losses[i]) / period;
        }

        // Calculate Relative Strength (RS)
        double rs = averageLoss == 0 ? Double.POSITIVE_INFINITY : averageGain / averageLoss;

        // Calculate RSI
        return 100 - (100 / (1 + rs));
    }
}
