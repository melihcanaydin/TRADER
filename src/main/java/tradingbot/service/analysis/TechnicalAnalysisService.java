package tradingbot.service.analysis;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import tradingbot.model.MarketData;

@Service
public class TechnicalAnalysisService {
    private static final Logger log = LoggerFactory.getLogger(TechnicalAnalysisService.class);

    // ✅ Simple Moving Average (SMA)
    public double calculateSMA(List<MarketData> data, int period) {
        if (data.size() < period) {
            log.warn("Not enough data for SMA calculation.");
            return 0.0;
        }
        return data.subList(0, period).stream().mapToDouble(MarketData::getClosePrice).sum()
                / period;
    }

    // ✅ Exponential Moving Average (EMA)
    public double calculateEMA(List<MarketData> data, int period) {
        if (data.size() < period) {
            log.warn("Not enough data for EMA calculation.");
            return 0.0;
        }
        double multiplier = 2.0 / (period + 1);
        double ema = calculateSMA(data, period);
        for (int i = period; i < data.size(); i++) {
            ema = (data.get(i).getClosePrice() - ema) * multiplier + ema;
        }
        return ema;
    }

    // ✅ Calculate Multiple SMAs at Once
    public Map<Integer, Double> calculateSMAs(List<MarketData> data, List<Integer> periods) {
        return periods.stream().collect(Collectors.toMap(period -> period,
                period -> calculateSMA(data.subList(0, Math.min(period, data.size())), period)));
    }

    public Double calculateRSI(List<MarketData> data, int period) {
        if (data == null || data.size() < period + 1) {
            log.warn("Not enough data for RSI calculation.");
            return null;
        }

        double[] gains = new double[period];
        double[] losses = new double[period];

        // ✅ Looping from the latest (index 0) backward
        for (int i = 0; i < period; i++) {
            double change = data.get(i).getClosePrice() - data.get(i + 1).getClosePrice();
            gains[i] = Math.max(change, 0);
            losses[i] = Math.max(-change, 0);
        }

        double avgGain = average(gains, period);
        double avgLoss = average(losses, period);

        if (avgLoss == 0)
            return 100.0;

        double rs = avgGain / avgLoss;
        return 100 - (100 / (1 + rs));
    }

    public boolean isRisingRSI(List<MarketData> data, int period) {
        if (data == null || data.size() < period + 1) {
            log.warn("Not enough data to determine RSI trend.");
            return false;
        }

        // ✅ Ensure RSI is rising consistently over the period
        for (int i = period; i > 1; i--) {
            Double rsiCurrent = calculateRSI(data.subList(0, i), period);
            Double rsiNext = calculateRSI(data.subList(0, i - 1), period);

            // ✅ Handle null values safely
            if (rsiCurrent == null || rsiNext == null) {
                log.warn("RSI calculation returned null at index {}", i);
                return false;
            }

            if (rsiNext <= rsiCurrent) {
                return false; // RSI is not consistently increasing
            }
        }
        return true; // RSI is rising
    }

    private double average(double[] values, int period) {
        double sum = 0;
        for (int i = 0; i < period; i++) {
            sum += values[i];
        }
        return sum / period;
    }

    // ✅ MACD Calculation (Difference Between 12 EMA and 26 EMA)
    public double calculateMACD(List<MarketData> data) {
        if (data.size() < 26) {
            log.warn("Not enough data for MACD calculation.");
            return 0.0;
        }
        double ema12 = calculateEMA(data, 12);
        double ema26 = calculateEMA(data, 26);
        return ema12 - ema26;
    }

    public double calculateSignalLine(List<MarketData> data) {
        if (data.size() < 35) {
            log.warn("Not enough data for Signal Line calculation.");
            return 0.0;
        }

        // ✅ Extract latest 26 MACD values correctly by creating sublists
        List<Double> macdValues = data.subList(0, 26).stream()
                .map(d -> calculateMACD(data.subList(0, 26))).collect(Collectors.toList());

        // ✅ Compute EMA for last 9 MACD values
        return calculateEMAForDoubles(macdValues, 9);
    }

    // ✅ New Helper Method: EMA for List<Double>
    private double calculateEMAForDoubles(List<Double> values, int period) {
        if (values.size() < period) {
            log.warn("Not enough data for EMA calculation on MACD.");
            return 0.0;
        }

        double multiplier = 2.0 / (period + 1);
        double ema = values.stream().limit(period).mapToDouble(Double::doubleValue).sum() / period;

        for (int i = period; i < values.size(); i++) {
            ema = (values.get(i) - ema) * multiplier + ema;
        }
        return ema;
    }

    // ✅ Bollinger Bands (20-period SMA + Standard Deviation)
    public double[] calculateBollingerBands(List<MarketData> data, int period) {
        if (data.size() < period) {
            log.warn("Not enough data for Bollinger Bands calculation.");
            return new double[] {0.0, 0.0, 0.0};
        }

        double sma = calculateSMA(data, period);
        double sumSquaredDiffs = data.subList(0, period).stream()
                .mapToDouble(d -> Math.pow(d.getClosePrice() - sma, 2)).sum();
        double standardDeviation = Math.sqrt(sumSquaredDiffs / period);

        double upperBand = sma + (2 * standardDeviation);
        double lowerBand = sma - (2 * standardDeviation);
        return new double[] {upperBand, sma, lowerBand};
    }

    // ✅ On-Balance Volume (OBV)
    public double calculateOBV(List<MarketData> data) {
        if (data.size() < 2) {
            log.warn("Not enough data for OBV calculation.");
            return 0.0;
        }
        double obv = 0.0;
        for (int i = 1; i < data.size(); i++) {
            double closePricePrev = data.get(i - 1).getClosePrice();
            double closePriceCurr = data.get(i).getClosePrice();
            double volume = data.get(i).getVolume();
            if (closePriceCurr > closePricePrev)
                obv += volume;
            else if (closePriceCurr < closePricePrev)
                obv -= volume;
        }
        return obv;
    }

    // ✅ Average True Range (ATR)
    public double calculateATR(List<MarketData> data, int period) {
        if (data.size() < period) {
            log.warn("Not enough data for ATR calculation.");
            return 0.0;
        }
        double atr = 0.0;
        for (int i = 1; i < period; i++) {
            double high = data.get(i).getHighPrice();
            double low = data.get(i).getLowPrice();
            double prevClose = data.get(i - 1).getClosePrice();
            double trueRange = Math.max(high - low,
                    Math.max(Math.abs(high - prevClose), Math.abs(low - prevClose)));
            atr += trueRange;
        }
        return atr / period;
    }

    // ✅ Calculate Multiple Moving Averages at Once
    public Map<Integer, Double> calculateMovingAverages(List<MarketData> data,
            List<Integer> periods) {
        return periods.stream()
                .collect(Collectors.toMap(period -> period, period -> calculateSMA(data, period)));
    }
}
