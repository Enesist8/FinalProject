package com.example.finalproject.controller;

import com.example.finalproject.model.Employee;
import com.example.finalproject.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public String showEmployeesPage() {
        return "employees"; // Replace "employees" with the name of your Thymeleaf template
    }

    @GetMapping
    public String showEmployees(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employees = employeeService.findAll(pageable);
        model.addAttribute("employees", employees);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", employees.getTotalPages());
        return "employees";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "createEmployee";
    }

    @PostMapping("/create")
    public String createEmployee(@Valid @ModelAttribute Employee employee, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "createEmployee";
        }
        try {
            employeeService.save(employee);
            redirectAttributes.addFlashAttribute("successMessage", "Employee created successfully!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating employee: " + e.getMessage());
            return "createEmployee";
        }
        return "redirect:/employees";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Employee> employee = employeeService.findById(id);
        if (employee.isPresent()) {
            model.addAttribute("employee", employee.get());
            return "editEmployee";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Employee not found");
            return "redirect:/employees";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateEmployee(@PathVariable Long id, @Valid @ModelAttribute Employee employee, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("employee", employee);
            return "editEmployee";
        }
        try {
            employeeService.update(id, employee);
            redirectAttributes.addFlashAttribute("successMessage", "Employee updated successfully!");
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("employee", employee);
            return "editEmployee";
        }
        return "redirect:/employees";
    }

    @GetMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/employees";
    }

    @GetMapping("/{id}/details")
    public String showEmployeeDetails(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Employee> employee = employeeService.findById(id);
        if (employee.isPresent()) {
            model.addAttribute("employee", employee.get());
            return "employeeDetails";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Employee not found");
            return "redirect:/employees";
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "registration";
    }


    @PostMapping("/register")
    public String registerEmployee(@Valid @ModelAttribute Employee employee, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        try {
            employeeService.save(employee); //Use Secure Password Hashing Here
            redirectAttributes.addFlashAttribute("successMessage", "Employee registered successfully!");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error registering employee: " + e.getMessage());
            return "registration";
        }
    }



}