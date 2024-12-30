package tradingbot.rules;

import tradingbot.model.CoinData;

public class MA5CrossesMA8Rule implements Rule {

    @Override
    public boolean check(CoinData coinData) {
        double ma5 = coinData.getMovingAverage(5);
        double ma8 = coinData.getMovingAverage(8);
        return ma5 < ma8 && coinData.getPreviousMovingAverage(5) >= coinData.getPreviousMovingAverage(8);
    }
}
