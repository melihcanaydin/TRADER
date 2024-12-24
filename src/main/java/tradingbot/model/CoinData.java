package tradingbot.model;

public class CoinData {

    private final double price;
    private final double[] fibonacciLevels;

    public CoinData(double price, double[] fibonacciLevels) {
        this.price = price;
        this.fibonacciLevels = fibonacciLevels;
    }

    public double getPrice() {
        return price;
    }

    public double[] getFibonacciLevels() {
        return fibonacciLevels;
    }
}
