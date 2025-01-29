package tradingbot.service.risk;

public class PositionSizingService {

    public Double calculatePositionSize(Double accountBalance) {
        return accountBalance * 0.20;
    }
}
