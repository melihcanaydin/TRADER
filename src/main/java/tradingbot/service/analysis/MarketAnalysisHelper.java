package tradingbot.service.analysis;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import tradingbot.model.MarketData;
import tradingbot.service.notification.NotificationService;

@Component
public class MarketAnalysisHelper {

        private static final Logger log = LoggerFactory.getLogger(MarketAnalysisHelper.class);

        private final TechnicalAnalysisService analysisService;
        private final NotificationService telegramService; // ✅ Add Telegram service

        public MarketAnalysisHelper(TechnicalAnalysisService analysisService,
                        NotificationService telegramService) {
                this.analysisService = analysisService;
                this.telegramService = telegramService;
        }

        public MarketIndicators calculateIndicators(List<MarketData> data) {
                if (data == null || data.isEmpty()) {
                        throw new IllegalArgumentException("Market data cannot be null or empty.");
                }

                // ✅ Get latest market data point
                MarketData latestData = data.get(0);
                double currentPrice = latestData.getClosePrice();

                Double ma20 = analysisService.calculateSMA(data, 20);
                Double ma50 = analysisService.calculateSMA(data, 50);
                Double ma200 = analysisService.calculateSMA(data, 200);

                // ✅ Calculate RSI
                Double rsi = analysisService
                                .calculateRSI(data.subList(0, Math.min(15, data.size())), 14);
                boolean rsiRising = analysisService
                                .isRisingRSI(data.subList(0, Math.min(15, data.size())), 14);

                // ✅ Calculate Bollinger Bands
                double[] bollingerBands = analysisService.calculateBollingerBands(data, 20);

                // ✅ Calculate MACD & Signal Line

                double macdLine = analysisService.calculateMACD(data);
                double signalLine = analysisService.calculateSignalLine(data); // ✅ Corresponding
                                                                               // Signal Line

                // ✅ Calculate OBV
                Double obv = analysisService.calculateOBV(data);
                boolean obvRising = (obv != null) && obv > 0;

                String message = String.format("<b>📊 Technical Indicators Update! 📊</b>\n\n"
                                + "<b>🔹 Coin:</b> %s\n"
                                + "<b>💰 Current Price:</b> <code>%.2f</code>\n\n" +

                                "<b>📈 Moving Averages:</b>\n" + "   - 20-day: <b>%.2f</b> 📊\n"
                                + "   - 50-day: <b>%.2f</b> 📊\n"
                                + "   - 200-day: <b>%.2f</b> 📊\n\n" +

                                "<b>📊 RSI (14):</b> <code>%.2f</code> (%s)\n\n" +

                                "<b>💎 Bollinger Bands:</b>\n" + "   🔼 Upper: <b>%.2f</b>\n"
                                + "   ➖ Middle: <b>%.2f</b>\n" + "   🔽 Lower: <b>%.2f</b>\n\n" +

                                "<b>📊 MACD Line:</b> <code>%.2f</code>\n"
                                + "<b>📊 Signal Line:</b> <code>%.2f</code>\n\n" +

                                "<b>📊 On-Balance Volume (OBV):</b> <code>%.2f</code> (%s)\n\n" +

                                "🚀 <i>Trade wisely!</i> 🔥",

                                latestData.getSymbol(), currentPrice, ma20, ma50, ma200, rsi,
                                rsiRising ? "📈 Rising ✅" : "📉 Falling ❌", bollingerBands[0],
                                bollingerBands[1], bollingerBands[2], macdLine, signalLine, obv,
                                obvRising ? "📈 Rising ✅" : "📉 Falling ❌");

                telegramService.sendMessage(message);
                log.info(message);
                log.info("📊 Sent detailed indicator analysis to Telegram for {}",
                                latestData.getSymbol());

                return new MarketIndicators(currentPrice, ma20, ma50, ma200, rsi, rsiRising,
                                bollingerBands, macdLine, signalLine, obv, obvRising);
        }
}
