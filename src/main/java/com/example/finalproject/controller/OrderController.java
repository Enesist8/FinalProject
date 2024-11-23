package com.example.finalproject.controller;

import com.example.finalproject.model.Customer;
import com.example.finalproject.model.Order;
import com.example.finalproject.service.CustomerService;
import com.example.finalproject.service.OrderService;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final CustomerService customerService;


    @Autowired
    public OrderController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;


    }

    @GetMapping
    public String showOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();

        // Use DateTimeFormatter directly
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        orders.forEach(order -> {
            try {
                order.setOrderDateStr(order.getOrderDate().format(formatter));
            } catch (DateTimeParseException e) {
                // Handle potential DateTimeParseException
                System.err.println("Error formatting date: " + e.getMessage());
                order.setOrderDateStr("N/A"); // Or log the error, etc.
            }
        });

        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/{id}")
    public String showOrder(@PathVariable Long id, Model model) {
        Optional<Order> order = orderService.getOrderById(id);
        order.ifPresent(model::addAttribute);
        return "orderDetails"; // Replace with your template name
    }

    @GetMapping("/create")
    public String displayCreateOrderForm(Model model) {
        List<Customer> customers = customerService.getAllCustomers(); // Crucial!
        model.addAttribute("customers", customers); // Add customers to the model
        model.addAttribute("order", new Order());
        return "createOrder";
    }

    @PostMapping("/create")
    public String createOrder(@ModelAttribute("order") Order order, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "createOrder";
        }
        try {
            orderService.createOrder(order);
            return "redirect:/orders";
        } catch (Exception e) {
            log.error("Error creating order: ", e);
            model.addAttribute("error", "Error creating order: " + e.getMessage());
            return "createOrder";
        }
    }

    @GetMapping("/{id}/edit")
    public String showOrderEditForm(@PathVariable Long id, Model model) {
        Optional<Order> orderOptional = orderService.getOrderById(id);
        List<Customer> customers = customerService.getAllCustomers();

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();

            // CRUCIAL: Initialize the Customer object to avoid lazy loading issues

            // Null checks for robustness
            if (order.getCustomer() == null) {
                log.warn("Customer associated with order ID {} is null!", id);
            }

            model.addAttribute("order", order);
            model.addAttribute("customers", customers);
            return "editOrder";
        } else {
            return "redirect:/orders"; // Or handle 404 appropriately
        }
    }

    @PostMapping("/{id}/edit")
    public String updateOrder(@PathVariable Long id, @ModelAttribute("order") Order order, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "editOrder";
        }
        try {
            Optional<Order> updatedOrder = orderService.updateOrder(id, order);
            if (updatedOrder.isPresent()) {
                return "redirect:/orders";
            } else {
                model.addAttribute("error", "Order not found");
                return "editOrder";
            }
        } catch (Exception e) {
            log.error("Error updating order: ", e);
            model.addAttribute("error", "Error updating order: " + e.getMessage());
            model.addAttribute("order", order);
            return "editOrder";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }


}