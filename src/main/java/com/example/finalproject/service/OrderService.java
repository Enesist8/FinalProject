package com.example.finalproject.service;

import com.example.finalproject.model.Order;
import com.example.finalproject.repository.OrderRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "orderDate")).stream().map(order ->{
            Hibernate.initialize(order.getCustomer()); // Force initialization
            return order;
        }).toList();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> updateOrder(Long id, Order order) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setOrderDate(order.getOrderDate());
                    existingOrder.setTotalAmount(order.getTotalAmount());
                    existingOrder.setCustomer(order.getCustomer()); //Update customer if needed
                    return orderRepository.save(existingOrder);
                });
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}