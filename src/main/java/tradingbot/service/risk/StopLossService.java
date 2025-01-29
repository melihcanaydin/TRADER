package tradingbot.service.risk;

import java.util.List;

import tradingbot.model.MarketData;
import tradingbot.service.indicator.ATRService;

public class StopLossService {

    private final ATRService atrService;

    public StopLossService(ATRService atrService) {
        this.atrService = atrService;
    }

    public Double calculateTrailingStop(List<MarketData> data) {
        Double atr = atrService.calculateATR(data, 14);
        return 3 * atr;
    }
}
