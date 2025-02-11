package tradingbot.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BuySuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String coinName;
    private double buyPrice;
    private double rsi;
    private double macdLine;
    private double upperBollinger;
    private LocalDateTime suggestionDate = LocalDateTime.now();

    public BuySuggestion(String coinName, double buyPrice, double rsi, double macdLine,
            double upperBollinger) {
        this.coinName = coinName;
        this.buyPrice = buyPrice;
        this.rsi = rsi;
        this.macdLine = macdLine;
        this.upperBollinger = upperBollinger;
        this.suggestionDate = LocalDateTime.now();
    }

    public BuySuggestion() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getRsi() {
        return rsi;
    }

    public void setRsi(double rsi) {
        this.rsi = rsi;
    }

    public double getMacdLine() {
        return macdLine;
    }

    public void setMacdLine(double macdLine) {
        this.macdLine = macdLine;
    }

    public double getUpperBollinger() {
        return upperBollinger;
    }

    public void setUpperBollinger(double upperBollinger) {
        this.upperBollinger = upperBollinger;
    }

    public LocalDateTime getSuggestionDate() {
        return suggestionDate;
    }

    public void setSuggestionDate(LocalDateTime suggestionDate) {
        this.suggestionDate = suggestionDate;
    }
}
