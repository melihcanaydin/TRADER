package tradingbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tradingbot.model.BuySuggestion;

@Repository
public interface BuySuggestionRepository extends JpaRepository<BuySuggestion, Long> {
}
