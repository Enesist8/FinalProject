package com.example.finalproject.service;

import com.example.finalproject.model.OrderItem;
import com.example.finalproject.repository.OrderItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {

    private static final Logger log = LoggerFactory.getLogger(OrderItemService.class);

    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public List<OrderItem> getAllOrderItems() {
        List<OrderItem> orderItems = orderItemRepository.findAll();
        for (OrderItem item : orderItems) {
            log.info("OrderItem: ID={}, Quantity={}, UnitPrice={}",
                    item.getOrderItemId(), item.getQuantity(), item.getUnitPrice());
            if (item.getQuantity() == null || item.getUnitPrice() == null) {
                log.error("Null value found in OrderItem: ID={}", item.getOrderItemId());
            }
        }
        return orderItems;
    }

    public Optional<OrderItem> getOrderItemById(Long id) {
        return orderItemRepository.findById(id);
    }

    public OrderItem createOrderItem(OrderItem orderItem) {
        log.info("Creating OrderItem: {}", orderItem);
        return orderItemRepository.save(orderItem);
    }

    public boolean updateOrderItem(Long id, OrderItem updatedOrderItem) {
        Optional<OrderItem> existingOrderItemOptional = orderItemRepository.findById(id);
        if (existingOrderItemOptional.isPresent()) {
            OrderItem existingOrderItem = existingOrderItemOptional.get();
            // Copy relevant fields from updatedOrderItem to existingOrderItem
            existingOrderItem.setQuantity(updatedOrderItem.getQuantity());
            existingOrderItem.setUnitPrice(updatedOrderItem.getUnitPrice());
            existingOrderItem.setOrder(updatedOrderItem.getOrder());
            existingOrderItem.setInstrument(updatedOrderItem.getInstrument());
            orderItemRepository.save(existingOrderItem);
            return true; // Update successful
        } else {
            return false; // OrderItem not found
        }
    }

    public void deleteOrderItem(Long id) {
        orderItemRepository.deleteById(id);
        log.info("Deleting OrderItem with ID: {}", id);
    }
}