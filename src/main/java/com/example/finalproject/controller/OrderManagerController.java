package com.example.finalproject.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

    @Controller
    @RequestMapping("/orderManager")
    @PreAuthorize("hasAnyAuthority('ORDER_MANAGER')")  // Important
    public class OrderManagerController {
        // ...other methods...
        @GetMapping("/orderItems")
        public String showOrderManagerOrders() {
            return "orderItems"; // Thymeleaf template
        }
}
