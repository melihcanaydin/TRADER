package tradingbot.repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import tradingbot.model.Coin;
import tradingbot.model.CoinData;
import tradingbot.model.MarketData;

@Repository
public class LogicRepository {

    private final Map<Coin, CoinData> coinDataMap = new HashMap<>();
    private final MarketDataRepository marketDataRepository;

    public LogicRepository(MarketDataRepository marketDataRepository) {
        this.marketDataRepository = marketDataRepository;
    }

    public void updateCoinData(Coin coin, double price, double[] fibonacciLevels, double rsi, double volume, double previousVolume, Map<Integer, Double> movingAverages, Map<Integer, Double> previousMovingAverages) {
        CoinData coinData = new CoinData(price, fibonacciLevels, rsi, volume, previousVolume, movingAverages, previousMovingAverages);
        coinDataMap.put(coin, coinData);

        MarketData marketData = new MarketData();
        marketData.setSymbol(coin.toString());
        marketData.setClosePrice(price);
        marketData.setHighPrice(0.0);
        marketData.setLowPrice(0.0);
        marketData.setVolume(volume);
        marketData.setMovingAverage5(movingAverages.get(5));
        marketData.setMovingAverage8(movingAverages.get(8));
        marketData.setMovingAverage21(movingAverages.get(21));
        marketData.setRsi(rsi);
        marketData.setCreatedAt(LocalDateTime.now());

        marketDataRepository.save(marketData);
    }

    public CoinData getCoinData(Coin coin) {
        return coinDataMap.get(coin);
    }

    public Map<Coin, CoinData> getAllCoinData() {
        return new HashMap<>(coinDataMap);
    }

    public Map<Integer, Double> getMovingAverages(Coin coin, Iterable<Integer> periods) {
        CoinData coinData = getCoinData(coin);

        if (coinData == null) {
            return new HashMap<>();
        }

        Map<Integer, Double> movingAverages = new HashMap<>();
        for (int period : periods) {
            movingAverages.put(period, coinData.getMovingAverage(period));
        }
        return movingAverages;
    }
}
