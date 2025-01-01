package tradingbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tradingbot.model.MarketData;

@Repository
public interface MarketDataRepository extends JpaRepository<MarketData, Long> {
}
