package tradingbot.rules;

import tradingbot.model.CoinData;

public class MA8CrossesMA21Rule implements Rule {

    @Override
    public boolean check(CoinData coinData) {
        double ma8 = coinData.getMovingAverage(8);
        double ma21 = coinData.getMovingAverage(21);
        return ma8 > ma21 && coinData.getPreviousMovingAverage(8) <= coinData.getPreviousMovingAverage(21);
    }
}
