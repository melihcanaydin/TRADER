package main.java.tradingbot.service.trading;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.java.tradingbot.model.Order;
import main.java.tradingbot.service.order.OrderService;

@Service
public class TradeService {

    private final FibonacciService fibonacciService;
    private final BinanceService binanceService;
    private final OrderService orderService;
    private final Set<String> processedTrades;

    public TradeService(FibonacciService fibonacciService, BinanceService binanceService, OrderService orderService) {
        this.fibonacciService = fibonacciService;
        this.binanceService = binanceService;
        this.orderService = orderService;
        this.processedTrades = new HashSet<>();
    }

    @Transactional
    public void performTrade(String symbol, double high, double low) {
        double[] levels = fibonacciService.calculateFibonacciLevels(high, low);
        double currentPrice = binanceService.getCurrentPrice(symbol);

        for (double target : levels) {
            String tradeKey = symbol + "-" + target;

            if (currentPrice <= target && !processedTrades.contains(tradeKey)) {
                processedTrades.add(tradeKey);

                try {
                    Order order = new Order();
                    order.setSymbol(symbol);
                    order.setPrice(target);
                    order.setOrderType("BUY");
                    orderService.saveOrder(order);
                    System.out.println("Buy signal at: " + target);
                } catch (IllegalArgumentException e) {
                    processedTrades.remove(tradeKey);
                    System.err.println("Failed to save order: " + e.getMessage());
                }
            }
        }
    }
}
