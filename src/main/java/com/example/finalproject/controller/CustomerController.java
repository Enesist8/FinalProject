package com.example.finalproject.controller;

import com.example.finalproject.model.Customer;
import com.example.finalproject.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String showCustomers(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customers"; //Template name
    }

    @GetMapping("/{id}")
    public String showCustomer(@PathVariable Long id, Model model) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        customer.ifPresent(model::addAttribute);
        return "customerDetails"; //Template name
    }

    @GetMapping("/create")
    public String showCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "createCustomer"; //Template name
    }

    @PostMapping("/create")
    public String createCustomer(@ModelAttribute Customer customer, Model model) {
        customerService.createCustomer(customer);
        return "redirect:/customers";
    }

    @GetMapping("/{id}/edit")
    public String showCustomerEditForm(@PathVariable Long id, Model model) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        customer.ifPresent(model::addAttribute);
        return "editCustomer"; //Template name
    }

    @PostMapping("/{id}/edit")
    public String updateCustomer(@PathVariable Long id, @ModelAttribute Customer customer, Model model) {
        try{
            customerService.updateCustomer(id, customer);
            return "redirect:/customers";
        } catch (RuntimeException e){
            model.addAttribute("error", e.getMessage());
            model.addAttribute("customer", customer); // Add the customer object back to the model
            return "editCustomer";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }
}