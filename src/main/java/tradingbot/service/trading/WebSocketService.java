package tradingbot.service.trading;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.CandlestickInterval;

import tradingbot.model.Coin;
import tradingbot.repository.LogicRepository;

@Service
public class WebSocketService {

    private final BinanceApiWebSocketClient webSocketClient;
    private final LogicRepository logicRepository;
    private final FibonacciService fibonacciService;

    public WebSocketService(BinanceApiWebSocketClient webSocketClient, LogicRepository logicRepository, FibonacciService fibonacciService) {
        this.webSocketClient = webSocketClient;
        this.logicRepository = logicRepository;
        this.fibonacciService = fibonacciService;
    }

    @PostConstruct
    public void initializeWebSocket() {
        Arrays.stream(Coin.values()).forEach(coin -> {
            webSocketClient.onCandlestickEvent(
                    coin.name().toLowerCase(),
                    CandlestickInterval.FOUR_HOURLY,
                    new BinanceApiCallback<CandlestickEvent>() {
                @Override
                public void onResponse(CandlestickEvent event) {
                    double high = Double.parseDouble(event.getHigh());
                    double low = Double.parseDouble(event.getLow());
                    double currentPrice = Double.parseDouble(event.getClose());
                    double[] fibonacciLevels = fibonacciService.calculateFibonacciLevels(high, low);
                    logicRepository.updateCoinData(coin, currentPrice, fibonacciLevels);
                    System.out.println("Updated data for " + coin + ": price=" + currentPrice);
                }

                @Override
                public void onFailure(Throwable cause) {
                    System.err.println("Error in WebSocket connection for " + coin + ": " + cause.getMessage());
                }
            }
            );
        });
    }
}
