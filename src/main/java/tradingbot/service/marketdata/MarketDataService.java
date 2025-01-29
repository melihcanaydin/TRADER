package tradingbot.service.marketdata;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;

import tradingbot.model.Coin;
import tradingbot.model.MarketData;
import tradingbot.repository.MarketDataRepository;
import tradingbot.service.indicator.ATRService;
import tradingbot.service.indicator.BollingerBandsService;
import tradingbot.service.indicator.MACDService;
import tradingbot.service.indicator.MovingAverageService;
import tradingbot.service.indicator.OBVService;
import tradingbot.service.indicator.RSIService;

@Service
public class MarketDataService {

    private static final Logger logger = LoggerFactory.getLogger(MarketDataService.class);

    private final BinanceApiRestClient binanceApiClient;
    private final RSIService rsiService;
    private final MACDService macdService;
    private final BollingerBandsService bbService;
    private final MovingAverageService maService;
    private final ATRService atrService;
    private final OBVService obvService;


    private final MarketDataRepository marketDataRepository;

    public MarketDataService(BinanceApiRestClient binanceApiClient, RSIService rsiService,
            MACDService macdService, BollingerBandsService bbService,
            MovingAverageService maService, ATRService atrService, OBVService obvService,
            MarketDataRepository marketDataRepository) {
        this.binanceApiClient = binanceApiClient;
        this.rsiService = rsiService;
        this.macdService = macdService;
        this.bbService = bbService;
        this.maService = maService;
        this.atrService = atrService;
        this.obvService = obvService;
        this.marketDataRepository = marketDataRepository;
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
    
    private List<MarketData> parseToMarketData(List<Candlestick> candlesticks, String symbol) {
        return candlesticks.stream()
                .map(candle -> new MarketData(symbol, candle.getOpenTime(),
                        Double.parseDouble(candle.getOpen()), Double.parseDouble(candle.getHigh()),
                        Double.parseDouble(candle.getLow()), Double.parseDouble(candle.getClose()),
                        Double.parseDouble(candle.getVolume()), candle.getCloseTime()))
                .collect(Collectors.toList());
    }

    public double getRSI(Coin coin, int period) {
        List<Candlestick> candlesticks = getCandlesticks(coin.name(), CandlestickInterval.DAILY);
        List<MarketData> marketData = parseToMarketData(candlesticks, coin.name());
        return rsiService.calculateRSI(marketData, period); // Pass both marketData and period
    }
    
    public double[] getMACD(Coin coin) {
        List<Candlestick> candlesticks = getCandlesticks(coin.name(), CandlestickInterval.DAILY);
        List<MarketData> marketData = parseToMarketData(candlesticks, coin.name());
        return macdService.calculateMACD(marketData);
    }
    
    public double[] getBollingerBands(Coin coin) {
        List<Candlestick> candlesticks = getCandlesticks(coin.name(), CandlestickInterval.DAILY);
        List<MarketData> marketData = parseToMarketData(candlesticks, coin.name());
        return bbService.calculateBollingerBands(marketData);
    }

    public double getOBV(Coin coin) {
        List<Candlestick> candlesticks = getCandlesticks(coin.name(), CandlestickInterval.DAILY);
        if (candlesticks.size() < 2) {
            throw new IllegalArgumentException("Not enough candlestick data for OBV calculation");
        }

        double obv = 0.0;
        double previousClose = Double.parseDouble(candlesticks.get(0).getClose());

        for (int i = 1; i < candlesticks.size(); i++) {
            double currentClose = Double.parseDouble(candlesticks.get(i).getClose());
            double currentVolume = Double.parseDouble(candlesticks.get(i).getVolume());

            if (currentClose > previousClose) {
                obv += currentVolume;
            } else if (currentClose < previousClose) {
                obv -= currentVolume;
            }

            previousClose = currentClose;
        }

        return obv;
    }
    
    public double getATR(Coin coin, int period) {
        List<Candlestick> candlesticks = getCandlesticks(coin.name(), CandlestickInterval.DAILY);
        List<MarketData> marketData = parseToMarketData(candlesticks, coin.name());
        return atrService.calculateATR(marketData, period); // Pass both marketData and period
    }

    public void saveMarketData(List<MarketData> marketDataList) {
        marketDataRepository.saveAll(marketDataList);
    }
}
