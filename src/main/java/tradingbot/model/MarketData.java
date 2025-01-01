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
