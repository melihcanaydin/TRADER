package tradingbot.rules;

import org.springframework.stereotype.Component;

import tradingbot.model.CoinData;

@Component
public class MA8CrossesMA21Rule implements Rule {

    @Override
    public boolean check(CoinData coinData) {
        double previousMa8 = coinData.getPreviousMovingAverage(8);
        double previousMa21 = coinData.getPreviousMovingAverage(21);

        if (previousMa8 == 0 || previousMa21 == 0) {
            return false;
        }

        double ma8 = coinData.getMovingAverage(8);
        double ma21 = coinData.getMovingAverage(21);

        return ma8 > ma21 && previousMa8 <= previousMa21;
    }
}
