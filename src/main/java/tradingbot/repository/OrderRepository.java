package tradingbot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tradingbot.model.ExecutionOrder;

@Repository
public interface OrderRepository extends JpaRepository<ExecutionOrder, Long> {

    Optional<ExecutionOrder> findBySymbolAndPriceAndOrderType(String symbol, double price, String orderType);
}
