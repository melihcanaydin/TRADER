package tradingbot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.binance.api.client.domain.market.CandlestickInterval;

import tradingbot.service.trading.BinanceService;

@RestController
@RequestMapping("/api/moving-averages")
public class MovingAverageController {

    private final BinanceService binanceService;

    public MovingAverageController(BinanceService binanceService) {
        this.binanceService = binanceService;
    }

    @GetMapping
    public String calculateMovingAverages(@RequestParam(defaultValue = "DAILY") String interval) {
        binanceService.calculateMovingAverages(
                CandlestickInterval.valueOf(interval),
                5, // MA5
                8 // MA8
        );
        return "Moving averages calculated!";
    }
}
