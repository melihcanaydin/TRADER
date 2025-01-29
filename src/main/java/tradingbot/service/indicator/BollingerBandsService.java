package tradingbot.service.indicator;

import java.util.List;

import org.springframework.stereotype.Service;

import tradingbot.model.MarketData;

@Service
public class BollingerBandsService {

    private final MovingAverageService maService;

    public BollingerBandsService(MovingAverageService maService) {
        this.maService = maService;
    }

    public double[] calculateBollingerBands(List<MarketData> data) {
        if (data == null || data.size() < 20) {
            return null; // Not enough data
        }

        double sma = maService.calculateSMA(data, 20);

        double sumSquaredDeviations = 0;
        for (int i = data.size() - 20; i < data.size(); i++) {
            double deviation = data.get(i).getClosePrice() - sma;
            sumSquaredDeviations += deviation * deviation;
        }

        double standardDeviation = Math.sqrt(sumSquaredDeviations / 20);
        double upperBand = sma + (2 * standardDeviation);
        double lowerBand = sma - (2 * standardDeviation);

        return new double[]{upperBand, sma, lowerBand};
    }
}
