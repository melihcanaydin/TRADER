package tradingbot.service.trading;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.binance.api.client.domain.market.CandlestickInterval;

import tradingbot.model.Coin;
import tradingbot.repository.LogicRepository;
import tradingbot.rules.Rule;
import tradingbot.rules.RuleEngine;
import tradingbot.service.marketdata.MarketDataService;

@Service
public class PriceCheckerService {

    private final LogicRepository logicRepository;
    private final MarketDataService marketDataService;
    private final FibonacciService fibonacciService;
    private final RuleEngine ruleEngine;

    public PriceCheckerService(LogicRepository logicRepository, MarketDataService marketDataService, FibonacciService fibonacciService, RuleEngine ruleEngine) {
        this.logicRepository = logicRepository;
        this.marketDataService = marketDataService;
        this.fibonacciService = fibonacciService;
        this.ruleEngine = ruleEngine;
    }

    @Scheduled(fixedRate = 60000)
    public void checkPrices() {
        updateCoinData();

        logicRepository.getAllCoinData().forEach((coin, data) -> {
            System.out.println("Checking rules for: " + coin);

            // Use the RuleEngine to evaluate rules for the current CoinData
            List<Rule> matchedRules = ruleEngine.getMatchingRules(data);

            if (!matchedRules.isEmpty()) {
                System.out.println("Matched rules for " + coin + ":");
                matchedRules.forEach(rule -> System.out.println("- " + rule.getClass().getSimpleName()));
            } else {
                System.out.println("No rules matched for " + coin);
            }

            //TODO:MCA Implement and decide how to decide performing trades based on Rules matched
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

                List<Integer> periods = List.of(5, 8, 21);
                Map<Integer, Double> existingMovingAverages = logicRepository.getMovingAverages(coin, periods);
                Map<Integer, Double> previousMovingAverages = periods.stream()
                        .collect(Collectors.toMap(
                                period -> period,
                                period -> existingMovingAverages != null && existingMovingAverages.containsKey(period)
                                ? existingMovingAverages.get(period)
                                : 0.0
                        ));

                Map<Integer, Double> movingAverages = Map.of(
                        5, marketDataService.getMovingAverage(coin.name(), 5),
                        8, marketDataService.getMovingAverage(coin.name(), 8),
                        21, marketDataService.getMovingAverage(coin.name(), 21)
                );

                double[] fibonacciLevels = fibonacciService.calculateFibonacciLevels(coin.name(), CandlestickInterval.DAILY, 14);

                logicRepository.updateCoinData(coin, currentPrice, fibonacciLevels, rsi, volume, previousVolume, movingAverages, previousMovingAverages);

                System.out.println("Updated data for " + coin.name() + ": {"
                        + "currentPrice=" + currentPrice
                        + ", rsi=" + rsi
                        + ", volume=" + volume
                        + ", previousVolume=" + previousVolume
                        + ", movingAverages=" + movingAverages
                        + ", previousMovingAverages=" + previousMovingAverages
                        + ", fibonacciLevels=" + java.util.Arrays.toString(fibonacciLevels)
                        + "}");

            } catch (Exception e) {
                System.err.println("Failed to update data for " + coin.name() + ": " + e.getMessage());
            }
        }

        System.out.println("Finished updateCoinData.");
    }
}
