package tradingbot.model;

public class TradeSignal {

    public static final TradeSignal NO_SIGNAL = new TradeSignal("HOLD");

    private String action;
    private Double positionSize;
    private Integer leverage;
    private Double trailingStop;

    public TradeSignal(String action) {
        this.action = action;
    }

    public TradeSignal(String action, Double positionSize, Integer leverage, Double trailingStop) {
        this.action = action;
        this.positionSize = positionSize;
        this.leverage = leverage;
        this.trailingStop = trailingStop;
    }

    public static TradeSignal getNoSignal() {
        return NO_SIGNAL;
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
