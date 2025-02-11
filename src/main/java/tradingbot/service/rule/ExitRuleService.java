package tradingbot.service.rule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import tradingbot.service.analysis.MarketIndicators;
import tradingbot.service.notification.NotificationService;

@Service
public class ExitRuleService {

        private static final Logger log = LoggerFactory.getLogger(ExitRuleService.class);

        private final NotificationService telegramService;

        public ExitRuleService(NotificationService telegramService) {
                this.telegramService = telegramService;
        }

        public boolean checkExitRules(MarketIndicators indicators) {
                if (indicators == null) {
                        throw new IllegalArgumentException("Market indicators cannot be null.");
                }

                boolean trendReversal = (indicators.ma20 != null && indicators.ma50 != null
                                && indicators.ma200 != null)
                                && (indicators.ma20 < indicators.ma50
                                                || indicators.currentPrice < indicators.ma200);

                boolean rsiExhausted = (indicators.rsi != null) && (indicators.rsi > 65)
                                && !indicators.rsiRising;

                boolean volatilitySpike = (indicators.bollingerBands != null)
                                && (indicators.currentPrice >= indicators.bollingerBands[0]); // Upper
                                                                                              // Bollinger
                                                                                              // Band

                boolean macdBearish = indicators.macdLine < indicators.signalLine;
                boolean confirmationValid = macdBearish && !indicators.obvRising;

                boolean exitSignal = trendReversal || rsiExhausted || volatilitySpike
                                || confirmationValid;

                log.info("📉 Exit Signal: {}", exitSignal);
                return exitSignal;
        }
}
