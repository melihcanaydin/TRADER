package tradingbot.service.trading;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tradingbot.model.ExecutionOrder;
import tradingbot.service.order.OrderService;

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

        double currentPrice = binanceService.getCurrentPrice(symbol).getPrice();

        for (double target : levels) {
            String tradeKey = symbol + "-" + target;

            if (currentPrice <= target && !processedTrades.contains(tradeKey)) {
                processedTrades.add(tradeKey);

                try {
                    ExecutionOrder executionOrder = new ExecutionOrder();
                    executionOrder.setSymbol(symbol);
                    executionOrder.setPrice(target);
                    executionOrder.setOrderType("BUY");
                    orderService.saveOrder(executionOrder);
                    System.out.println("Buy signal at: " + target);
                } catch (IllegalArgumentException e) {
                    processedTrades.remove(tradeKey);
                    System.err.println("Failed to save order: " + e.getMessage());
                }
            }
        }
    }
}
