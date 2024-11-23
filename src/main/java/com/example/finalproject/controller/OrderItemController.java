package com.example.finalproject.controller;

import com.example.finalproject.model.OrderItem;
import com.example.finalproject.service.InstrumentService;
import com.example.finalproject.service.OrderItemService;
import com.example.finalproject.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/orderItems")
public class OrderItemController {

    private static final Logger log = LoggerFactory.getLogger(OrderItemController.class);

    private final OrderItemService orderItemService;
    private final OrderService orderService;
    private final InstrumentService instrumentService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService, OrderService orderService, InstrumentService instrumentService) {
        this.orderItemService = orderItemService;
        this.orderService = orderService;
        this.instrumentService = instrumentService;
    }

    @GetMapping
    public String showOrderItems(Model model) {
        model.addAttribute("orderItems", orderItemService.getAllOrderItems());
        return "orderItems";
    }

    @GetMapping("/create")
    public String createOrderItemForm(Model model) {
        model.addAttribute("orderItem", new OrderItem());
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("instruments", instrumentService.getAllInstruments());
        return "createOrderItem";
    }

    @GetMapping("/{id}")
    public String orderItemDetails(@PathVariable Long id, Model model) {
        Optional<OrderItem> orderItemOptional = orderItemService.getOrderItemById(id);
        if (orderItemOptional.isPresent()) {
            model.addAttribute("orderItem", orderItemOptional.get());
            return "orderItemDetails";
        } else {
            return "redirect:/orderItems"; // Handle case where order item not found
        }
    }

    @PostMapping("/create")
    public String createOrderItem(@Valid @ModelAttribute("orderItem") OrderItem orderItem, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("orders", orderService.getAllOrders());
            model.addAttribute("instruments", instrumentService.getAllInstruments());
            return "createOrderItem";
        }
        try {
            orderItemService.createOrderItem(orderItem);
            return "redirect:/orderItems";
        } catch (Exception e) {
            log.error("Error creating order item: ", e);
            model.addAttribute("errorMessage", "Error creating order item. Please try again.");
            model.addAttribute("orders", orderService.getAllOrders());
            model.addAttribute("instruments", instrumentService.getAllInstruments());
            return "createOrderItem";
        }
    }

    @GetMapping("/{id}/edit") //The correct mapping
    public String editOrderItemForm(@PathVariable Long id, Model model) {
        Optional<OrderItem> orderItemOptional = orderItemService.getOrderItemById(id);
        if (orderItemOptional.isPresent()) {
            model.addAttribute("orderItem", orderItemOptional.get());
            model.addAttribute("orders", orderService.getAllOrders());
            model.addAttribute("instruments", instrumentService.getAllInstruments());
            return "editOrderItem";
        } else {
            return "redirect:/orderItems"; // Redirect if OrderItem not found
        }
    }

    @PostMapping("/{id}/update")
    public String updateOrderItem(@PathVariable Long id, @Valid @ModelAttribute("orderItem") OrderItem updatedOrderItem, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("orders", orderService.getAllOrders());
            model.addAttribute("instruments", instrumentService.getAllInstruments());
            return "editOrderItem";
        }
        try {
            boolean success = orderItemService.updateOrderItem(id, updatedOrderItem);
            if (success) {
                return "redirect:/orderItems";
            } else {
                model.addAttribute("errorMessage", "Error updating order item. Order item not found.");
                model.addAttribute("orders", orderService.getAllOrders());
                model.addAttribute("instruments", instrumentService.getAllInstruments());
                return "editOrderItem";
            }
        } catch (Exception e) {
            log.error("Error updating order item: ", e);
            model.addAttribute("errorMessage", "Error updating order item. Please try again.");
            model.addAttribute("orders", orderService.getAllOrders());
            model.addAttribute("instruments", instrumentService.getAllInstruments());
            return "editOrderItem"; //Added return statement here
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteOrderItem(@PathVariable Long id) {
        try{
            orderItemService.deleteOrderItem(id);
            return "redirect:/orderItems";
        } catch (Exception e){
            log.error("Error deleting order item: ",e);
            return "redirect:/orderItems"; // Or handle the error more gracefully.
        }
    }
}