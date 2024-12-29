package tradingbot.service.trading;

import java.util.Arrays;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;

import tradingbot.model.Coin;

@Service
public class BinanceService {

    private final BinanceApiRestClient binanceApiRestClient;
    private final FibonacciService fibonacciService;

    public BinanceService(BinanceApiRestClient binanceApiRestClient, FibonacciService fibonacciService) {
        this.binanceApiRestClient = binanceApiRestClient;
        this.fibonacciService = fibonacciService;
    }

    public List<Candlestick> getHistoricalCandlesticks(String symbol, CandlestickInterval interval, int limit) {
        return binanceApiRestClient.getCandlestickBars(symbol, interval, limit, null, null);
    }

    public double getCurrentPrice(String symbol) {
        return Double.parseDouble(binanceApiRestClient.getPrice(symbol).getPrice());
    }

    public void pingServer() {
        binanceApiRestClient.ping();
        System.out.println("Ping successful to Binance API");
    }

    @Scheduled(fixedRate = 60000) // Executes every 1 minute
    public void checkPrices() {
        System.out.println("Starting scheduled task to calculate moving averages...");
        calculateMovingAverages(CandlestickInterval.DAILY, 5, 8);
        System.out.println("Finished scheduled task to calculate moving averages.");
    }

    public void calculateMovingAverages(CandlestickInterval interval, int ma5Period, int ma8Period) {
        for (Coin coin : Coin.values()) {
            String symbol = coin.name();

            try {
                // Fetch historical candlesticks
                List<Candlestick> candlesticks = getHistoricalCandlesticks(symbol, interval, Math.max(ma5Period, ma8Period));

                // Calculate MA5
                double ma5 = calculateAverage(candlesticks, ma5Period);

                // Calculate MA8
                double ma8 = calculateAverage(candlesticks, ma8Period);

                // Calculate Fibonacci levels
                double high = candlesticks.stream().mapToDouble(c -> Double.parseDouble(c.getHigh())).max().orElse(0);
                double low = candlesticks.stream().mapToDouble(c -> Double.parseDouble(c.getLow())).min().orElse(0);
                double[] fibonacciLevels = fibonacciService.calculateFibonacciLevels(high, low);

                // Logging the results
                System.out.println("Coin: " + coin);
                System.out.println("MA5: " + ma5);
                System.out.println("MA8: " + ma8);
                System.out.println("Fibonacci Levels: " + Arrays.toString(fibonacciLevels));

            } catch (Exception e) {
                System.err.println("Error calculating moving averages or Fibonacci levels for " + symbol + ": " + e.getMessage());
            }
        }
    }

    private double calculateAverage(List<Candlestick> candlesticks, int period) {
        return candlesticks.stream()
                .skip(Math.max(0, candlesticks.size() - period)) // Use only the last 'period' candlesticks
                .mapToDouble(candlestick -> Double.parseDouble(candlestick.getClose()))
                .average()
                .orElse(0.0);
    }
}
