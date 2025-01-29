package tradingbot.service.indicator;

import java.util.List;

import org.springframework.stereotype.Service;

import tradingbot.model.MarketData;

@Service
public class OBVService {

    public Double calculateOBV(List<MarketData> data) {
        if (data == null || data.isEmpty()) {
            return null; // No data
        }

        double obv = 0;
        for (int i = 1; i < data.size(); i++) {
            MarketData current = data.get(i);
            MarketData previous = data.get(i - 1);

            if (current.getClosePrice() > previous.getClosePrice()) {
                obv += current.getVolume();
            } else if (current.getClosePrice() < previous.getClosePrice()) {
                obv -= current.getVolume();
            }
        }
        return obv;
    }

    public Boolean isRisingOBV(List<MarketData> data) {
        if (data == null || data.size() < 2) {
            return null; // Not enough data
        }

        double currentOBV = calculateOBV(data);
        double previousOBV = calculateOBV(data.subList(0, data.size() - 1));
        return currentOBV > previousOBV;
    }
}
