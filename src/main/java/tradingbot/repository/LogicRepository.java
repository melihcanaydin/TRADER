package tradingbot.repository;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import tradingbot.model.Coin;
import tradingbot.model.CoinData;
import tradingbot.model.MarketData;
import tradingbot.service.analysis.TechnicalAnalysisService;

@Repository
public class LogicRepository {

    private static final Logger log = LoggerFactory.getLogger(LogicRepository.class);

    private final MarketDataRepository marketDataRepository;
    private final TechnicalAnalysisService analysisService;

    private final Map<Coin, CoinData> coinDataMap = new HashMap<>();

    public LogicRepository(MarketDataRepository marketDataRepository,
            TechnicalAnalysisService analysisService) {
        this.marketDataRepository = marketDataRepository;
        this.analysisService = analysisService;
    }

    public void analyzeMarketData(MarketData marketData) {
        // List<MarketData> historicalData =
        // marketDataRepository.findRecentData(marketData.getSymbol(), PageRequest.of(0, 30));

        // if (historicalData.size() < 26) {
        // log.warn("Not enough historical data for indicator calculations.");
        // return;
        // }

        // double macd = analysisService.calculateMACD(historicalData);
        // double signalLine = analysisService.calculateSignalLine(historicalData);
        // double atr = analysisService.calculateATR(historicalData, 14);
        // double obv = analysisService.calculateOBV(historicalData);
        // double[] bb = analysisService.calculateBollingerBands(historicalData, 20);

        // log.info("Computed MACD: {}, Signal Line: {}, ATR: {}, OBV: {}", macd, signalLine, atr,
        // obv);
        // log.info("Bollinger Bands - Upper: {}, Middle: {}, Lower: {}", bb[0], bb[1], bb[2]);
    }

    /**
     * Updates CoinData for a given coin.
     */
    public void updateCoinData(Coin coin, CoinData coinData) {
        coinDataMap.put(coin, coinData);
        log.info("Updated CoinData for {}", coin);
    }
}
