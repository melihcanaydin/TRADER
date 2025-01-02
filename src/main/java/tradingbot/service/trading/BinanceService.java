package tradingbot.service.trading;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.CandlestickInterval;

import tradingbot.dto.CandlestickDto;
import tradingbot.dto.PriceResponseDto;

@Service
public class BinanceService {

    private static final Logger logger = LoggerFactory.getLogger(BinanceService.class);
    private final BinanceApiRestClient binanceApiRestClient;

    public BinanceService(BinanceApiRestClient binanceApiRestClient) {
        this.binanceApiRestClient = binanceApiRestClient;
    }

    public List<CandlestickDto> getHistoricalCandlesticks(String symbol, CandlestickInterval interval, int limit) {
        return binanceApiRestClient.getCandlestickBars(symbol, interval, limit, null, null)
                .stream()
                .map(c -> new CandlestickDto(
                c.getOpenTime(),
                c.getCloseTime(),
                c.getOpen(),
                c.getClose(),
                c.getHigh(),
                c.getLow(),
                c.getVolume()))
                .collect(Collectors.toList());
    }

    public PriceResponseDto getCurrentPrice(String symbol) {
        double price = Double.parseDouble(binanceApiRestClient.getPrice(symbol).getPrice());
        return new PriceResponseDto(symbol, price);
    }

    public void pingServer() {
        binanceApiRestClient.ping();
        logger.info("Ping successful to Binance API");
    }
}
