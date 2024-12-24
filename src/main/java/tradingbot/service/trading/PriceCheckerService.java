package tradingbot.service.trading;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import tradingbot.repository.LogicRepository;

@Service
public class PriceCheckerService {

    private final LogicRepository logicRepository;

    public PriceCheckerService(LogicRepository logicRepository) {
        this.logicRepository = logicRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void checkPrices() {
        logicRepository.getAllCoinData().forEach((coin, data) -> {
            double currentPrice = data.getPrice();
            double[] fibonacciLevels = data.getFibonacciLevels();
            for (double level : fibonacciLevels) {
                if (currentPrice <= level) {
                    System.out.println("Buy signal for " + coin + " at price: " + currentPrice);
                }
            }
        });
    }
}
