package tradingbot.service.analysis;

public class MarketIndicators {
    public final double currentPrice;
    public final Double ma20, ma50, ma200;
    public final Double rsi;
    public final boolean rsiRising;
    public final double[] bollingerBands;
    public final double macdLine, signalLine;
    public final Double obv;
    public final boolean obvRising;

    public MarketIndicators(double currentPrice, Double ma20, Double ma50, Double ma200, Double rsi,
            boolean rsiRising, double[] bollingerBands, double macdLine, double signalLine,
            Double obv, boolean obvRising) {
        this.currentPrice = currentPrice;
        this.ma20 = ma20;
        this.ma50 = ma50;
        this.ma200 = ma200;
        this.rsi = rsi;
        this.rsiRising = rsiRising;
        this.bollingerBands = bollingerBands;
        this.macdLine = macdLine;
        this.signalLine = signalLine;
        this.obv = obv;
        this.obvRising = obvRising;
    }
}
