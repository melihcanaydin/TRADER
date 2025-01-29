package tradingbot.service.indicator;

import java.util.List;

import org.springframework.stereotype.Service;

import tradingbot.model.MarketData;

@Service
public class ATRService {

    public Double calculateATR(List<MarketData> data, int period) {
        if (data == null || data.size() < period + 1) {
            return null; // Not enough data
        }

        double[] trueRanges = new double[data.size() - 1];
        for (int i = 1; i < data.size(); i++) {
            MarketData current = data.get(i);
            MarketData previous = data.get(i - 1);

            double tr = Math.max(
                    current.getHighPrice() - current.getLowPrice(),
                    Math.max(
                            Math.abs(current.getHighPrice() - previous.getClosePrice()),
                            Math.abs(current.getLowPrice() - previous.getClosePrice())
                    )
            );
            trueRanges[i - 1] = tr;
        }

        // Calculate initial ATR as SMA of true ranges
        double atr = 0;
        for (int i = 0; i < period; i++) {
            atr += trueRanges[i];
        }
        atr /= period;

        // Calculate subsequent ATR values
        for (int i = period; i < trueRanges.length; i++) {
            atr = (atr * (period - 1) + trueRanges[i]) / period;
        }

        return atr;
    }
}
