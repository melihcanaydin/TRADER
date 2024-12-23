package main.java.tradingbot.service.trading;

import java.util.Arrays;

import org.springframework.stereotype.Service;

@Service
public class FibonacciService {

    public double[] calculateFibonacciLevels(double high, double low) {
        double[] levels = {0.236, 0.382, 0.5, 0.618, 0.786};
        return Arrays.stream(levels)
                .map(level -> high - (high - low) * level)
                .toArray();
    }
}
