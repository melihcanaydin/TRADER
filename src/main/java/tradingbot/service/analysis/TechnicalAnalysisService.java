package tradingbot.service.analysis;

import java.util.ArrayList;
import java.util.Collections;
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



    // ✅ Calculate Multiple SMAs at Once
    // public Map<Integer, Double> calculateSMAs(List<MarketData> data, List<Integer> periods) {
    // return periods.stream().collect(Collectors.toMap(period -> period,
    // period -> calculateSMA(data.subList(0, Math.min(period, data.size())), period)));
    // }

    public double calculateRSI(List<MarketData> data, int period) {
        if (data == null || data.size() < period + 1) {
            log.warn("Not enough data for RSI calculation.");
            return Double.NaN;
        }

        // ✅ Reverse the list to process from oldest to newest
        List<MarketData> reversedData = new ArrayList<>(data);
        Collections.reverse(reversedData);

        double avgGain = 0;
        double avgLoss = 0;

        // ✅ Step 1: Compute Initial Average Gain & Loss
        for (int i = 1; i <= period; i++) {
            double change =
                    reversedData.get(i).getClosePrice() - reversedData.get(i - 1).getClosePrice();
            if (change > 0) {
                avgGain += change;
            } else {
                avgLoss -= change; // Take absolute value for losses
            }
        }

        avgGain /= period;
        avgLoss /= period;

        // ✅ Step 2: Compute Smoothed RSI Using Wilder's Smoothing
        for (int i = period + 1; i < reversedData.size(); i++) {
            double change =
                    reversedData.get(i).getClosePrice() - reversedData.get(i - 1).getClosePrice();

            double gain = (change > 0) ? change : 0;
            double loss = (change < 0) ? -change : 0;

            avgGain = ((avgGain * (period - 1)) + gain) / period;
            avgLoss = ((avgLoss * (period - 1)) + loss) / period;
        }

        // ✅ Step 3: Compute RSI
        double rs = (avgLoss == 0) ? Double.POSITIVE_INFINITY : avgGain / avgLoss;
        return 100 - (100 / (1 + rs));
    }

    public boolean isRisingRSI(List<MarketData> data, int period) {
        if (data == null || data.size() < period + 1) {
            log.warn("Not enough data to determine RSI trend.");
            return false;
        }

        List<Double> rsiValues = new ArrayList<>();

        // ✅ Calculate RSI for the last `period` candles safely
        for (int i = 0; i <= data.size() - period; i++) { // Ensure valid sublist
            List<MarketData> subList = data.subList(i, i + period); // ✅ This is now safe
            Double rsi = calculateRSI(subList, period);

            if (rsi == null)
                continue; // Ignore null values
            rsiValues.add(rsi);
        }

        if (rsiValues.size() < 2) {
            log.warn("Not enough calculated RSI values to check for trend.");
            return false;
        }

        // ✅ Check if RSI is consistently rising
        for (int i = 1; i < rsiValues.size(); i++) {
            if (rsiValues.get(i) <= rsiValues.get(i - 1)) {
                return false; // RSI is not increasing
            }
        }

        return true; // RSI is consistently rising
    }

    public static double calculateMACD(List<MarketData> data) {
        // Reverse the data to process from oldest to latest (chronological order)
        List<MarketData> reversedData = new ArrayList<>(data);
        Collections.reverse(reversedData);

        // Extract the close prices in chronological order
        List<Double> closes =
                reversedData.stream().map(MarketData::getClosePrice).collect(Collectors.toList());

        // Calculate the 12-period and 26-period EMAs
        List<Double> ema12 = calculateEMA(closes, 12);
        List<Double> ema26 = calculateEMA(closes, 26);

        // Ensure there are enough EMA values to compute MACD
        if (ema12.isEmpty() || ema26.isEmpty()) {
            throw new IllegalArgumentException("Insufficient data to calculate MACD Line");
        }

        // Get the latest EMA values
        double latestEMA12 = ema12.get(ema12.size() - 1);
        double latestEMA26 = ema26.get(ema26.size() - 1);

        // MACD Line is the difference between the two EMAs
        return latestEMA12 - latestEMA26;
    }

    private static List<Double> calculateEMA(List<Double> closes, int period) {
        List<Double> ema = new ArrayList<>();
        if (closes.size() < period) {
            return ema; // Not enough data points
        }

        double multiplier = 2.0 / (period + 1);
        double sum = 0.0;

        // Calculate the initial SMA (period values)
        for (int i = 0; i < period; i++) {
            sum += closes.get(i);
        }
        double sma = sum / period;
        ema.add(sma);

        // Calculate subsequent EMA values
        for (int i = period; i < closes.size(); i++) {
            double prevEMA = ema.get(ema.size() - 1);
            double currentClose = closes.get(i);
            double currentEMA = (currentClose - prevEMA) * multiplier + prevEMA;
            ema.add(currentEMA);
        }

        return ema;
    }

    // ✅ New Helper Method for EMA on List<Double>
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

    public double calculateEMAWithPeriod(List<MarketData> data, int period) {
        if (data.size() < period) {
            log.warn("Not enough data for EMA calculation.");
            return 0.0;
        }

        double multiplier = 2.0 / (period + 1);
        double ema = calculateSMA(data.subList(data.size() - period, data.size()), period); // Start
                                                                                            // with
                                                                                            // SMA

        for (int i = data.size() - period - 1; i >= 0; i--) {
            ema = (data.get(i).getClosePrice() - ema) * multiplier + ema;
        }

        return ema;
    }

    public double calculateSignalLine(List<MarketData> data) {
        if (data.size() < 26) {
            log.warn("Not enough data for MACD calculation.");
            return 0.0;
        }

        // ✅ Calculate MACD values for each candlestick
        List<Double> macdValues = new ArrayList<>();
        for (int i = 0; i <= data.size() - 26; i++) {
            double ema12 = calculateEMAWithPeriod(data.subList(i, i + 12), 12);
            double ema26 = calculateEMAWithPeriod(data.subList(i, i + 26), 26);
            macdValues.add(ema12 - ema26);
        }

        if (macdValues.size() < 9) {
            log.warn("Not enough MACD values to compute the signal line.");
            return 0.0;
        }

        // ✅ Compute the Signal Line (EMA of MACD)
        return calculateEMAForDoubles(macdValues, 9);
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
