package tradingbot.model;

import java.util.Map;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import tradingbot.util.JsonMapConverter;

@Entity
@Table(name = "coin_data")
public class CoinData {

    @Id
    private String coinName;

    private double price;
    private double[] fibonacciLevels;
    private double rsi;
    private double volume;
    private double previousVolume;

    @Convert(converter = JsonMapConverter.class)
    private Map<Integer, Double> movingAverages;

    @Convert(converter = JsonMapConverter.class)
    private Map<Integer, Double> previousMovingAverages;

    private double macdLine;
    private double signalLine;
    private double upperBollinger;
    private double lowerBollinger;
    private double atr;
    private double obv;

    public CoinData() {}

    public CoinData(String coinName, double price, double[] fibonacciLevels, double rsi,
            double volume, double previousVolume, Map<Integer, Double> movingAverages,
            Map<Integer, Double> previousMovingAverages, double macdLine, double signalLine,
            double upperBollinger, double lowerBollinger, double atr, double obv) {
        this.coinName = coinName;
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

    public Double getMovingAverage(int period) {
        return movingAverages != null ? movingAverages.getOrDefault(period, 0.0) : 0.0;
    }

    public Double getPreviousMovingAverage(int period) {
        return previousMovingAverages != null ? previousMovingAverages.getOrDefault(period, 0.0)
                : 0.0;
    }

    public String getCoinName() {
        return coinName;
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
}
