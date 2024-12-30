package tradingbot.rules;

import tradingbot.model.CoinData;

public class RSIWithVolumeIncreaseRule implements Rule {

    @Override
    public boolean check(CoinData coinData) {
        double rsi = coinData.getRSI();
        double volume = coinData.getVolume();
        return rsi >= 30 && rsi <= 40 && volume > coinData.getPreviousVolume();
    }
}
