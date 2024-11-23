package com.example.finalproject.controller;

import com.example.finalproject.models.ModelUser;
import com.example.finalproject.model.RoleEnum;
import com.example.finalproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String showUserEmployees() {
        return "employees";
    }
    @GetMapping("/me")
    public String myDataView(Model model, Authentication authentication) {
        String username = authentication.getName();
        ModelUser user = userRepository.findByUsername(username); //get user directly

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        model.addAttribute("user_object", user);
        return "user/info";
    }

    @GetMapping("/me/update")
    public String myDataUpdateView(Model model, Authentication authentication) {
        String username = authentication.getName();
        ModelUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        model.addAttribute("user_object", user);
        model.addAttribute("roles", RoleEnum.values());
        return "user/update";
    }

    @PostMapping("/me/update")
    public String myDataUpdate(@RequestParam String username, @RequestParam(name = "roles[]", required = false) String[] roles, Authentication authentication) {
        String currentUsername = authentication.getName();
        ModelUser user = userRepository.findByUsername(currentUsername);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + currentUsername);
        }
        user.setUsername(username);

        Set<RoleEnum> updatedRoles = new HashSet<>();
        if (roles != null) {
            updatedRoles = Arrays.stream(roles).map(RoleEnum::valueOf).collect(Collectors.toSet());
        }
        user.setRoles(updatedRoles);

        userRepository.save(user);
        return "redirect:/user/me";
    }
}
