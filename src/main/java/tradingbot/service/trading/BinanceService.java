package main.java.tradingbot.service.trading;

import org.springframework.stereotype.Service;

import com.binance.api.client.BinanceApiRestClient;

@Service
public class BinanceService {

    private final BinanceApiRestClient client;

    public BinanceService(BinanceApiRestClient client) {
        this.client = client;
    }

    public double getCurrentPrice(String symbol) {
        return Double.parseDouble(client.getPrice(symbol).getPrice());
    }
}
