package tradingbot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.binance.api.client.domain.market.CandlestickInterval;

import tradingbot.service.trading.BinanceService;

@RestController
public class BinanceController {

    private final BinanceService binanceService;

    public BinanceController(BinanceService tradingService) {
        this.binanceService = tradingService;
    }

    @GetMapping("/api/price")
    public double getCurrentPrice(@RequestParam String symbol) {
        return binanceService.getCurrentPrice(symbol);
    }

    @GetMapping("/api/candlesticks")
    public String getCandlesticks(@RequestParam String symbol, @RequestParam String interval, @RequestParam int limit) {
        return binanceService.getHistoricalCandlesticks(symbol, CandlestickInterval.valueOf(interval), limit).toString();
    }

    @GetMapping("/api/ping")
    public String pingServer() {
        binanceService.pingServer();
        return "Ping Successful!";
    }
}
