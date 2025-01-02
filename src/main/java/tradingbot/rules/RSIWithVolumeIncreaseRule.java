package tradingbot.rules;

import org.springframework.stereotype.Component;

import tradingbot.model.CoinData;

@Component
public class RSIWithVolumeIncreaseRule implements Rule {

    @Override
    public boolean check(CoinData coinData) {
        double rsi = coinData.getRSI();
        double volume = coinData.getVolume();
        return rsi >= 30 && rsi <= 40 && volume > coinData.getPreviousVolume();
    }
}
