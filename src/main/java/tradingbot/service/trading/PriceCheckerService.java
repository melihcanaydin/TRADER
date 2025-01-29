package tradingbot.service.trading;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import tradingbot.model.BuySuggestion;
import tradingbot.model.Coin;
import tradingbot.model.CoinData;
import tradingbot.model.TradeSignal;
import tradingbot.repository.BuySuggestionRepository;
import tradingbot.repository.LogicRepository;
import tradingbot.service.marketdata.MarketDataService;
import tradingbot.service.strategy.MultiIndicatorStrategy;

@Service
public class PriceCheckerService {

    private static final Logger logger = LoggerFactory.getLogger(PriceCheckerService.class);
    private final LogicRepository logicRepository;
    private final MarketDataService marketDataService;
    private final BuySuggestionRepository buySuggestionRepository;
    private final MultiIndicatorStrategy tradingStrategy;

    public PriceCheckerService(LogicRepository logicRepository, MarketDataService marketDataService,
            BuySuggestionRepository buySuggestionRepository,
            MultiIndicatorStrategy tradingStrategy) {
        this.logicRepository = logicRepository;
        this.marketDataService = marketDataService;
        this.buySuggestionRepository = buySuggestionRepository;
        this.tradingStrategy = tradingStrategy;
    }

    @Scheduled(fixedRate = 60000)
    public void checkPrices() {
        for (Coin coin : Coin.values()) {
            try {
                // Get all indicators
                double price = marketDataService.getCurrentPrice(coin.name());
                double rsi = marketDataService.getRSI(coin, 14);
                double[] macd = marketDataService.getMACD(coin);
                double[] bb = marketDataService.getBollingerBands(coin);
                double atr = marketDataService.getATR(coin, 14);
                double obv = marketDataService.getOBV(coin);

                // Create validated CoinData object
                CoinData coinData = new CoinData(price, new double[] {}, // Fibonacci levels (TODO)
                        rsi, marketDataService.getVolume(coin), 0.0, // Previous volume (TODO)
                        marketDataService.getMovingAverages(coin, List.of(5, 20, 50, 200)),
                        new HashMap<>(), macd[0], macd[1], bb[0], bb[1], atr, obv);

                logicRepository.updateCoinData(coin, coinData);

                // Generate trade signal
                TradeSignal signal = tradingStrategy.evaluate(coinData);
                if (signal.isBuy()) {
                    // Save buy suggestion
                }
            } catch (Exception e) {
                logger.error("Failed to process {}", coin.name(), e);
            }
        }
    }

    private void processCoinData(Coin coin) {
        logger.info("Processing data for {}", coin.name());

        // Define the required period for indicators
        int rsiPeriod = 14;
        int atrPeriod = 14;

        try {
            // Fetch all required market data with the appropriate periods
            double price = marketDataService.getCurrentPrice(coin.name());
            double rsi = marketDataService.getRSI(coin, rsiPeriod);
            double[] macd = marketDataService.getMACD(coin);
            double[] bb = marketDataService.getBollingerBands(coin);
            double atr = marketDataService.getATR(coin, atrPeriod);
            double obv = marketDataService.getOBV(coin);

            // Validate indicator data
            if (macd == null || bb == null) {
                logger.warn("Skipping {} due to insufficient data for indicators", coin.name());
                return;
            }

            // Create CoinData with full indicator values
            CoinData coinData = new CoinData(price, // price
                    new double[] {}, // fibonacciLevels (empty for now)
                    rsi, // rsi
                    0.0, // volume (not used in this example)
                    0.0, // previousVolume (not used in this example)
                    new HashMap<>(), // movingAverages (empty for now)
                    new HashMap<>(), // previousMovingAverages (empty for now)
                    macd[0], // macdLine
                    macd[1], // signalLine
                    bb[0], // upperBollinger
                    bb[1], // lowerBollinger
                    atr, // atr
                    obv // obv
            );

            // Update coin data in the repository
            logicRepository.updateCoinData(coinData);

            // Evaluate trading strategy
            TradeSignal signal = tradingStrategy.evaluate(coinData);
            if (signal.isBuy()) {
                BuySuggestion suggestion = new BuySuggestion(coin.name(), coinData.getPrice(),
                        coinData.getRsi(), coinData.getMacdLine(), coinData.getUpperBollinger());
                buySuggestionRepository.save(suggestion);
                logger.info("Buy suggestion saved for {}", coin.name());
            }
        } catch (Exception e) {
            logger.error("Error processing data for {}: {}", coin.name(), e.getMessage(), e);
        }
    }
}
