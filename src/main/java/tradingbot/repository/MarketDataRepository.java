package tradingbot.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tradingbot.model.MarketData;

@Repository
public interface MarketDataRepository extends JpaRepository<MarketData, Long> {
    @Query("SELECT m FROM MarketData m WHERE m.symbol = :symbol ORDER BY m.createdAt DESC")
    List<MarketData> findRecentData(@Param("symbol") String symbol, Pageable pageable);

    List<MarketData> findTop201BySymbolOrderByCreatedAtDesc(String symbol);
}
