package tradingbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;

@Configuration
public class BinanceConfig {

    @Value("${binance.api.key}")
    private String apiKey;

    @Value("${binance.api.secret}")
    private String apiSecret;

    @Bean
    public BinanceApiRestClient binanceApiRestClient() {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apiKey, apiSecret);
        return factory.newRestClient();
    }
}
