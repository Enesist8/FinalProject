package com.example.finalproject.controller;


import com.example.finalproject.models.ModelUser;
import com.example.finalproject.model.RoleEnum;
import com.example.finalproject.repository.EmployeeRepository;
import com.example.finalproject.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/orders")
    public String showAdminOrders() {
        return "/orders"; // Assuming you have admin/orders.html
    }
    @GetMapping("/employees")
    public String showEmployees() {

        return "employees";
    }

    @GetMapping("/customers")
    public String showCustomers() {
        return "customers";
    }

    @GetMapping("/instrumentCategories")
    public String showInstrumentCategories() {
        return "instrumentCategories";
    }

    @GetMapping("/instruments")
    public String showInstruments() {
        return "instruments";
    }

    @GetMapping("/inventory")
    public String showInventory() {
        return "inventory";
    }

    @GetMapping("/orderItems")
    public String showOrderItems() {
        return "orderItems";
    }

    @GetMapping("/categories")
    public String showCategories() {
        return "categories";
    }


    @GetMapping("/users")
    public String userView(Model model) {
        model.addAttribute("user_list", userRepository.findAll());
        return "index";
    }

    @GetMapping("/{id}")
    public String detailView(@PathVariable Long id, Model model) {
        ModelUser user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + id));
        model.addAttribute("user_object", user);
        return "info";
    }

    @GetMapping("/{id}/update")
    public String updView(@PathVariable Long id, Model model) {
        ModelUser user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + id));
        model.addAttribute("user_object", user);
        model.addAttribute("roles", RoleEnum.values());
        return "update";
    }

    @PostMapping("/user/create")
    public String createUser(@Valid @ModelAttribute("user") ModelUser user,
                             @RequestParam(name = "roles[]", required = false) String[] selectedRoles,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", RoleEnum.values());
            return "create"; // Return to the create form on error
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            model.addAttribute("roles", RoleEnum.values());
            return "create"; // Return to the create form on error
        }

        try {
            user.setActive(true);
            user.setPassword(passwordEncoder.encode(user.getPassword())); //Encode password

            Set<RoleEnum> roles = new HashSet<>();
            if (selectedRoles != null) {
                for (String role : selectedRoles) {
                    roles.add(RoleEnum.valueOf(role));
                }
            }
            // Default to USER role if none are selected
            if (roles.isEmpty()) {
                roles.add(RoleEnum.USER);
            }
            user.setRoles(roles);
            userRepository.save(user);
            return "redirect:/admin/users"; // Redirect to user list after successful creation
        } catch (Exception e) {
            model.addAttribute("generalError", "Ошибка при создании пользователя. Пожалуйста, попробуйте позже.");
            model.addAttribute("roles", RoleEnum.values());
            return "create"; // Return to create form on error
        }
    }
}