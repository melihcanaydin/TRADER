package main.java.tradingbot.service.trading;

import org.springframework.stereotype.Service;

@Service
public class TradeService {
    private final FibonacciService fibonacciService;
    private final BinanceService binanceService;

    public TradeService(FibonacciService fibonacciService, BinanceService binanceService) {
        this.fibonacciService = fibonacciService;
        this.binanceService = binanceService;
    }

    public void performTrade(String symbol, double high, double low) {
        double[] levels = fibonacciService.calculateFibonacciLevels(high, low);
        double currentPrice = binanceService.getCurrentPrice(symbol);

        for (double target : levels) {
            if (currentPrice <= target) {
                System.out.println("Buy signal at: " + target);
                // Add logic to call OrderService and save orders
            }
        }
    }
}