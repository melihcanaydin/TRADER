package tradingbot.service.rule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import tradingbot.service.analysis.MarketIndicators;
import tradingbot.service.notification.NotificationService;

@Service
public class EntryRuleService {

        private static final Logger log = LoggerFactory.getLogger(EntryRuleService.class);

        private final NotificationService telegramService;

        public EntryRuleService(NotificationService telegramService) {
                this.telegramService = telegramService;
        }

        public boolean checkEntryRules(MarketIndicators indicators) {
                if (indicators == null) {
                        throw new IllegalArgumentException("Market indicators cannot be null.");
                }

                boolean trendValid = (indicators.ma20 != null && indicators.ma50 != null
                                && indicators.ma200 != null) && (indicators.ma20 > indicators.ma50)
                                && (indicators.currentPrice > indicators.ma200);

                boolean rsiValid = (indicators.rsi != null) && (indicators.rsi < 35)
                                && indicators.rsiRising;

                boolean volatilityValid = (indicators.bollingerBands != null)
                                && (indicators.currentPrice <= indicators.bollingerBands[2]); // Lower
                                                                                              // Bollinger
                                                                                              // Band

                boolean macdBullish = indicators.macdLine > indicators.signalLine;
                boolean confirmationValid = macdBullish && indicators.obvRising;

                boolean entrySignal =
                                trendValid && rsiValid && volatilityValid && confirmationValid;

                log.info("📈 Entry Signal: {}", entrySignal);
                return entrySignal;
        }
}
