package tradingbot.service.order;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tradingbot.model.ExecutionOrder;
import tradingbot.repository.OrderRepository;
import tradingbot.service.exception.DuplicateOrderException;
import tradingbot.service.exception.OrderNotFoundException;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public ExecutionOrder saveOrder(ExecutionOrder order) {
        logger.info("Attempting to save order: {}", order);

        checkForDuplicateOrder(order);

        ExecutionOrder savedOrder = orderRepository.save(order);
        logger.info("Order saved successfully: {}", savedOrder);
        return savedOrder;
    }

    public Optional<ExecutionOrder> getOrderById(Long id) {
        Optional<ExecutionOrder> order = orderRepository.findById(id);
        if (order.isEmpty()) {
            throw new OrderNotFoundException("Order with ID " + id + " does not exist.");
        }
        return order;
    }

    public List<ExecutionOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public void deleteOrderById(Long id) {
        logger.info("Attempting to delete order with ID: {}", id);

        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Order with ID " + id + " does not exist.");
        }

        orderRepository.deleteById(id);
        logger.info("Order with ID {} deleted successfully", id);
    }

    private void checkForDuplicateOrder(ExecutionOrder order) {
        Optional<ExecutionOrder> existingOrder = orderRepository.findBySymbolAndPriceAndOrderType(
                order.getSymbol(), order.getPrice(), order.getOrderType());

        if (existingOrder.isPresent()) {
            throw new DuplicateOrderException("Duplicate order: An identical order already exists.");
        }
    }
}
