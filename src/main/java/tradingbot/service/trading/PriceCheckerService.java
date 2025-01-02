package tradingbot.service.trading;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;

import tradingbot.model.Coin;
import tradingbot.model.CoinData;
import tradingbot.repository.LogicRepository;
import tradingbot.rules.Rule;
import tradingbot.rules.RuleEngine;
import tradingbot.service.marketdata.MarketDataService;

@Service
public class PriceCheckerService {

    private static final Logger logger = LoggerFactory.getLogger(PriceCheckerService.class);

    private final LogicRepository logicRepository;
    private final MarketDataService marketDataService;
    private final RuleEngine ruleEngine;

    public PriceCheckerService(LogicRepository logicRepository, MarketDataService marketDataService, RuleEngine ruleEngine) {
        this.logicRepository = logicRepository;
        this.marketDataService = marketDataService;
        this.ruleEngine = ruleEngine;
    }

    @Scheduled(fixedRate = 60000)
    public void checkPrices() {
        logger.info("Starting scheduled price check...");

        for (Coin coin : Coin.values()) {
            try {
                processCoinData(coin);
            } catch (Exception e) {
                logger.error("Error processing data for {}: {}", coin.name(), e.getMessage(), e);
            }
        }

        logger.info("Finished scheduled price check.");
    }

    private void processCoinData(Coin coin) {
        logger.info("Processing data for {}", coin.name());

        // Fetch candlesticks and perform calculations
        List<Candlestick> candlesticks = marketDataService.getCandlesticks(coin.name(), CandlestickInterval.DAILY);

        double currentPrice = marketDataService.getCurrentPrice(coin.name());
        double rsi = marketDataService.getRSI(coin.name());
        if (rsi < 0) {
            logger.warn("Skipping {} due to insufficient data for RSI", coin.name());
            return;
        }

        double volume = marketDataService.calculateVolume(candlesticks);
        double previousVolume = marketDataService.calculatePreviousVolume(candlesticks);

        List<Integer> periods = List.of(5, 8, 21);
        Map<Integer, Double> movingAverages = periods.stream()
                .collect(Collectors.toMap(period -> period, period -> marketDataService.calculateMovingAverage(candlesticks, period)));
        Map<Integer, Double> previousMovingAverages = periods.stream()
                .collect(Collectors.toMap(period -> period, period -> marketDataService.calculatePreviousMovingAverage(candlesticks, period)));

        double[] fibonacciLevels = marketDataService.calculateFibonacciLevels(candlesticks);

        logicRepository.updateCoinData(coin, currentPrice, fibonacciLevels, rsi, volume, previousVolume, movingAverages, previousMovingAverages);

        logger.info("Updated data for {} - Price: {}, RSI: {}, Volume: {}, PreviousVolume: {}, MovingAverages: {}, PreviousMovingAverages: {}, FibonacciLevels: {}",
                coin.name(), currentPrice, rsi, volume, previousVolume, movingAverages, previousMovingAverages, java.util.Arrays.toString(fibonacciLevels));

        checkRules(coin);
    }

    private void checkRules(Coin coin) {
        logger.info("Checking rules for {}", coin.name());
        CoinData coinData = logicRepository.getCoinData(coin);

        List<Rule> matchedRules = ruleEngine.getMatchingRules(coinData);

        if (!matchedRules.isEmpty()) {
            matchedRules.forEach(rule -> logger.info("Matched rule: {} for {}", rule.getClass().getSimpleName(), coin.name()));
            logger.info("BUY : " + coin.name() + " Buying Price : " + coinData.getPrice());

        } else {
            logger.info("No rules matched for {}", coin.name());
        }
    }
}
