package tradingbot.model;

import java.util.Map;

public class CoinData {

    private final double price;
    private final double[] fibonacciLevels;
    private final double rsi;
    private final double volume;
    private final double previousVolume;
    private final Map<Integer, Double> movingAverages;
    private final Map<Integer, Double> previousMovingAverages;

    // New fields for multi-indicator strategy
    private final double macdLine;          // MACD line (12-day EMA - 26-day EMA)
    private final double signalLine;        // Signal line (9-day EMA of MACD)
    private final double upperBollinger;    // Upper Bollinger Band
    private final double lowerBollinger;    // Lower Bollinger Band
    private final double atr;               // Average True Range (14-period)
    private final double obv;               // On-Balance Volume

    public CoinData(double price, double[] fibonacciLevels, double rsi, double volume, double previousVolume,
            Map<Integer, Double> movingAverages, Map<Integer, Double> previousMovingAverages,
            double macdLine, double signalLine, double upperBollinger, double lowerBollinger,
            double atr, double obv) {
        this.price = price;
        this.fibonacciLevels = fibonacciLevels;
        this.rsi = rsi;
        this.volume = volume;
        this.previousVolume = previousVolume;
        this.movingAverages = movingAverages;
        this.previousMovingAverages = previousMovingAverages;
        this.macdLine = macdLine;
        this.signalLine = signalLine;
        this.upperBollinger = upperBollinger;
        this.lowerBollinger = lowerBollinger;
        this.atr = atr;
        this.obv = obv;
    }

    // Getters for existing fields
    public double getPrice() {
        return price;
    }

    public double[] getFibonacciLevels() {
        return fibonacciLevels;
    }

    public double getRSI() {
        return rsi;
    }

    public double getVolume() {
        return volume;
    }

    public double getPreviousVolume() {
        return previousVolume;
    }

    public Map<Integer, Double> getMovingAverages() {
        return movingAverages;
    }

    public Map<Integer, Double> getPreviousMovingAverages() {
        return previousMovingAverages;
    }

    // Getters for new fields
    public double getMacdLine() {
        return macdLine;
    }

    public double getSignalLine() {
        return signalLine;
    }

    public double getUpperBollinger() {
        return upperBollinger;
    }

    public double getLowerBollinger() {
        return lowerBollinger;
    }

    public double getAtr() {
        return atr;
    }

    public double getObv() {
        return obv;
    }

    // Helper methods for moving averages
    public double getMovingAverage(int period) {
        return movingAverages.getOrDefault(period, 0.0);
    }

    public double getPreviousMovingAverage(int period) {
        return previousMovingAverages.getOrDefault(period, 0.0);
    }
}
