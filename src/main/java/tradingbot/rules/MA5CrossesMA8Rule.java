package tradingbot.rules;

import org.springframework.stereotype.Component;

import tradingbot.model.CoinData;

@Component
public class MA5CrossesMA8Rule implements Rule {

    @Override
    public boolean check(CoinData coinData) {
        double previousMa5 = coinData.getPreviousMovingAverage(5);
        double previousMa8 = coinData.getPreviousMovingAverage(8);

        if (previousMa5 == 0 || previousMa8 == 0) {
            return false;
        }

        double ma5 = coinData.getMovingAverage(5);
        double ma8 = coinData.getMovingAverage(8);

        return ma5 < ma8 && previousMa5 >= previousMa8;
    }
}
