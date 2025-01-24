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
    private LocalDateTime suggestionDate;

    public BuySuggestion() {
        this.suggestionDate = LocalDateTime.now();
    }

    public BuySuggestion(String coinName, double buyPrice) {
        this.coinName = coinName;
        this.buyPrice = buyPrice;
        this.suggestionDate = LocalDateTime.now();
    }

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

    public LocalDateTime getSuggestionDate() {
        return suggestionDate;
    }

    public void setSuggestionDate(LocalDateTime suggestionDate) {
        this.suggestionDate = suggestionDate;
    }
}
