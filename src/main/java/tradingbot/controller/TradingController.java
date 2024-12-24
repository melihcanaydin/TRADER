package tradingbot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tradingbot.dto.TradeRequestDto;
import tradingbot.dto.TradeResponseDto;
import tradingbot.service.trading.TradeService;

@RestController
@RequestMapping("/api/trade")
public class TradingController {

    private static final Logger logger = LoggerFactory.getLogger(TradingController.class);
    private final TradeService tradeService;

    public TradingController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @PostMapping("/check")
    public ResponseEntity<TradeResponseDto> checkTrade(@RequestBody TradeRequestDto request) {
        try {
            tradeService.performTrade(request.getSymbol(), request.getHigh(), request.getLow());
            return ResponseEntity.ok(new TradeResponseDto("Trade check completed!"));
        } catch (Exception e) {
            logger.warn("Error occurred while performing trade: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(new TradeResponseDto("An error occurred while performing the trade."));
        }
    }
}
