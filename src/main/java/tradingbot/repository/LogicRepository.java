package tradingbot.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import tradingbot.model.Coin;
import tradingbot.model.CoinData;

@Repository
public class LogicRepository {

    private final Map<Coin, CoinData> coinDataMap = new HashMap<>();

    public void updateCoinData(Coin coin, double price, double[] fibonacciLevels, double rsi, double volume, double previousVolume, Map<Integer, Double> movingAverages) {
        CoinData coinData = new CoinData(price, fibonacciLevels, rsi, volume, previousVolume, movingAverages);
        coinDataMap.put(coin, coinData);
    }

    public CoinData getCoinData(Coin coin) {
        return coinDataMap.get(coin);
    }

    public Map<Coin, CoinData> getAllCoinData() {
        return new HashMap<>(coinDataMap);
    }
}
