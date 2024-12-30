package tradingbot.service.trading;

import java.util.List;

import org.springframework.stereotype.Service;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;

@Service
public class FibonacciService {

    private final BinanceApiRestClient binanceClient;

    public FibonacciService(BinanceApiRestClient binanceClient) {
        this.binanceClient = binanceClient;
    }

    /**
     * Calculates Fibonacci levels based on the high and low prices from
     * candlestick data.
     *
     * @param coinName The name of the coin (e.g., "BTCUSDT").
     * @param interval The candlestick interval (e.g., DAILY).
     * @param period The number of candlesticks to consider.
     * @return An array of Fibonacci levels.
     */
    public double[] calculateFibonacciLevels(String coinName, CandlestickInterval interval, int period) {
        List<Candlestick> candlesticks = binanceClient.getCandlestickBars(coinName, interval, period, null, null);

        // Calculate high and low prices
        double high = candlesticks.stream()
                .mapToDouble(c -> Double.parseDouble(c.getHigh()))
                .max()
                .orElse(0.0);

        double low = candlesticks.stream()
                .mapToDouble(c -> Double.parseDouble(c.getLow()))
                .min()
                .orElse(0.0);

        return calculateFibonacciLevels(high, low);
    }

    /**
     * Calculates Fibonacci levels given high and low prices.
     *
     * @param high The high price.
     * @param low The low price.
     * @return An array of Fibonacci levels.
     */
    public double[] calculateFibonacciLevels(double high, double low) {
        double range = high - low;
        return new double[]{
            high - 0.236 * range,
            high - 0.382 * range,
            high - 0.5 * range,
            high - 0.618 * range,
            high - 0.786 * range
        };
    }
}
