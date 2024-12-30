package tradingbot.service.trading;

import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import tradingbot.model.Coin;
import tradingbot.repository.LogicRepository;
import tradingbot.service.marketdata.MarketDataService;

@Service
public class PriceCheckerService {

    private final LogicRepository logicRepository;
    private final MarketDataService marketDataService;
    private final FibonacciService fibonacciService;

    public PriceCheckerService(LogicRepository logicRepository, MarketDataService marketDataService, FibonacciService fibonacciService) {
        this.logicRepository = logicRepository;
        this.marketDataService = marketDataService;
        this.fibonacciService = fibonacciService;
    }

    @Scheduled(fixedRate = 60000)
    public void checkPrices() {
        updateCoinData();

        logicRepository.getAllCoinData().forEach((coin, data) -> {
            double currentPrice = data.getPrice();
            double[] fibonacciLevels = data.getFibonacciLevels();
            for (double level : fibonacciLevels) {
                if (currentPrice <= level) {
                    System.out.println("Buy signal for " + coin + " at price: " + currentPrice);
                }
            }
        });
    }

    private void updateCoinData() {
        System.out.println("Starting updateCoinData...");

        for (Coin coin : Coin.values()) {
            try {
                double currentPrice = marketDataService.getCurrentPrice(coin.name());

                double rsi = marketDataService.getRSI(coin.name());
                if (rsi < 0) {
                    System.out.println("There is not enough data to calculate RSI. Skipping for: " + coin.name());
                }

                double volume = marketDataService.getVolume(coin.name());
                double previousVolume = marketDataService.getPreviousVolume(coin.name());

                Map<Integer, Double> movingAverages = Map.of(
                        5, marketDataService.getMovingAverage(coin.name(), 5),
                        8, marketDataService.getMovingAverage(coin.name(), 8),
                        21, marketDataService.getMovingAverage(coin.name(), 21)
                );

                double high = 20000.0; // Replace with actual high value logic
                double low = 19000.0;  // Replace with actual low value logic
                double[] fibonacciLevels = fibonacciService.calculateFibonacciLevels(high, low);

                logicRepository.updateCoinData(coin, currentPrice, fibonacciLevels, rsi, volume, previousVolume, movingAverages);

                System.out.println("Updated data for " + coin.name() + ": {"
                        + "currentPrice=" + currentPrice
                        + ", rsi=" + rsi
                        + ", volume=" + volume
                        + ", previousVolume=" + previousVolume
                        + ", movingAverages=" + movingAverages
                        + ", fibonacciLevels=" + java.util.Arrays.toString(fibonacciLevels)
                        + "}");

            } catch (Exception e) {
                System.err.println("Failed to update data for " + coin.name() + ": " + e.getMessage());
            }
        }

        System.out.println("Finished updateCoinData.");
    }
}
