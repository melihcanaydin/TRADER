package tradingbot.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "market_data")
public class MarketData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private String interval;

    @Column(name = "close_price")
    private double closePrice;

    @Column(name = "high_price")
    private double highPrice;

    @Column(name = "low_price")
    private double lowPrice;

    private double volume;

    @Column(name = "moving_average_5")
    private Double movingAverage5;

    @Column(name = "moving_average_8")
    private Double movingAverage8;

    @Column(name = "moving_average_21")
    private Double movingAverage21;

    private double rsi;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public MarketData(String symbol, Long openTime, double open, double high, double low, double close, double volume, Long closeTime) {
        this.symbol = symbol;
        this.createdAt = LocalDateTime.now(); // Automatically set createdAt to the current time
        this.interval = "N/A"; // Default interval if not provided

        // Set price and volume data
        this.closePrice = close;
        this.highPrice = high;
        this.lowPrice = low;
        this.volume = volume;

        // Moving averages and RSI default to null or 0
        this.movingAverage5 = null;
        this.movingAverage8 = null;
        this.movingAverage21 = null;
        this.rsi = 0;
    }

    // Parameterized constructor
    public MarketData(String symbol, String interval, double closePrice, double highPrice, double lowPrice, double volume, Double movingAverage5, Double movingAverage8, Double movingAverage21, double rsi, LocalDateTime createdAt) {
        this.symbol = symbol;
        this.interval = interval;
        this.closePrice = closePrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.volume = volume;
        this.movingAverage5 = movingAverage5;
        this.movingAverage8 = movingAverage8;
        this.movingAverage21 = movingAverage21;
        this.rsi = rsi;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public Double getMovingAverage5() {
        return movingAverage5;
    }

    public void setMovingAverage5(Double movingAverage5) {
        this.movingAverage5 = movingAverage5;
    }

    public Double getMovingAverage8() {
        return movingAverage8;
    }

    public void setMovingAverage8(Double movingAverage8) {
        this.movingAverage8 = movingAverage8;
    }

    public Double getMovingAverage21() {
        return movingAverage21;
    }

    public void setMovingAverage21(Double movingAverage21) {
        this.movingAverage21 = movingAverage21;
    }

    public double getRsi() {
        return rsi;
    }

    public void setRsi(double rsi) {
        this.rsi = rsi;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
