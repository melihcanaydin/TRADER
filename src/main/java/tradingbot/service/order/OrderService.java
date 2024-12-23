package main.java.tradingbot.service.order;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.java.tradingbot.model.Order;
import main.java.tradingbot.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order saveOrder(Order order) {
        // Check for existing order with the same symbol, price, and order type
        Optional<Order> existingOrder = orderRepository.findBySymbolAndPriceAndOrderType(
                order.getSymbol(), order.getPrice(), order.getOrderType());

        if (existingOrder.isPresent()) {
            throw new IllegalArgumentException("Duplicate order: An identical order already exists.");
        }

        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void deleteOrderById(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Order with ID " + id + " does not exist.");
        }
    }
}
