package tradingbot.dto;

public class TradeRequestDto {

    private String symbol;
    private double high;
    private double low;

    public TradeRequestDto() {
    }

    public TradeRequestDto(String symbol, double high, double low) {
        this.symbol = symbol;
        this.high = high;
        this.low = low;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    @Override
    public String toString() {
        return "TradeRequestDto{"
                + "symbol='" + symbol + '\''
                + ", high=" + high
                + ", low=" + low
                + '}';
    }
}
