package main.java.tradingbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;

@Configuration
public class BinanceConfig {
    @Bean
    public BinanceApiRestClient binanceApiRestClient() {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(
                "YOUR_API_KEY", "YOUR_SECRET_KEY");
        return factory.newRestClient();
    }
}
