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

    public CoinData(double price, double[] fibonacciLevels, double rsi, double volume, double previousVolume, Map<Integer, Double> movingAverages, Map<Integer, Double> previousMovingAverages) {
        this.price = price;
        this.fibonacciLevels = fibonacciLevels;
        this.rsi = rsi;
        this.volume = volume;
        this.previousVolume = previousVolume;
        this.movingAverages = movingAverages;
        this.previousMovingAverages = previousMovingAverages;
    }

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

    public double getMovingAverage(int period) {
        return movingAverages.getOrDefault(period, 0.0);
    }

    public double getRsi() {
        return rsi;
    }

    public Map<Integer, Double> getMovingAverages() {
        return movingAverages;
    }

    public Map<Integer, Double> getPreviousMovingAverages() {
        return previousMovingAverages;
    }

    public Double getPreviousMovingAverage(int period) {
        return previousMovingAverages.getOrDefault(period, 0.0);
    }
}
