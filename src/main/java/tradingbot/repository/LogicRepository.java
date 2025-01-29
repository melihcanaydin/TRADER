package tradingbot.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import tradingbot.model.MarketData;
import tradingbot.service.indicator.ATRService;
import tradingbot.service.indicator.BollingerBandsService;
import tradingbot.service.indicator.MACDService;
import tradingbot.service.indicator.OBVService;

@Repository
public class LogicRepository {

    private static final Logger log = LoggerFactory.getLogger(LogicRepository.class);

    private final MarketDataRepository marketDataRepository;
    private final MACDService macdService;
    private final BollingerBandsService bbService;
    private final ATRService atrService;
    private final OBVService obvService;

    public LogicRepository(MarketDataRepository marketDataRepository, MACDService macdService,
            BollingerBandsService bbService, ATRService atrService, OBVService obvService) {
        this.marketDataRepository = marketDataRepository;
        this.macdService = macdService;
        this.bbService = bbService;
        this.atrService = atrService;
        this.obvService = obvService;
    }

    public void analyzeMarketData(MarketData marketData) {
        List<MarketData> historicalData =
                marketDataRepository.findRecentData(marketData.getSymbol(), PageRequest.of(0, 30));

        if (historicalData.size() < 26) {
            log.warn("Not enough historical data for indicator calculations.");
            return;
        }

        double macd = macdService.calculateMACD(historicalData);
        double atr = atrService.calculateATR(historicalData, 14); // Corrected period
        double obv = obvService.calculateOBV(historicalData);
        double[] bb = bbService.calculateBollingerBands(historicalData);

        log.info("Computed MACD: {}, ATR: {}, OBV: {}", macd, atr, obv);
        log.info("Bollinger Bands - Upper: {}, Lower: {}", bb[0], bb[1]);
    }
}
