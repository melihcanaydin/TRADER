package main.java.tradingbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import main.java.tradingbot.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
