package tradingbot.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "trade_signal") // ✅ Ensure correct table name
public class TradeSignal {

    @Id // ✅ Primary Key: Use coinName
    private String coinName;

    public static final TradeSignal NO_SIGNAL = new TradeSignal("NO_COIN", "HOLD");

    private String action;
    private Double positionSize;
    private Integer leverage;
    private Double trailingStop;

    public TradeSignal() {
        // Default constructor required by JPA
    }

    // ✅ Fix: Added constructor with default `coinName`
    public TradeSignal(String coinName, String action) {
        this.coinName = coinName;
        this.action = action;
    }

    public TradeSignal(String coinName, String action, Double positionSize, Integer leverage,
            Double trailingStop) {
        this.coinName = coinName;
        this.action = action;
        this.positionSize = positionSize;
        this.leverage = leverage;
        this.trailingStop = trailingStop;
    }

    public static TradeSignal getNoSignal() {
        return NO_SIGNAL;
    }

    // ✅ Getter & Setter for coinName
    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Double getPositionSize() {
        return positionSize;
    }

    public void setPositionSize(Double positionSize) {
        this.positionSize = positionSize;
    }

    public Integer getLeverage() {
        return leverage;
    }

    public void setLeverage(Integer leverage) {
        this.leverage = leverage;
    }

    public Double getTrailingStop() {
        return trailingStop;
    }

    public void setTrailingStop(Double trailingStop) {
        this.trailingStop = trailingStop;
    }

    public boolean isBuy() {
        return "BUY".equalsIgnoreCase(this.action);
    }
}
