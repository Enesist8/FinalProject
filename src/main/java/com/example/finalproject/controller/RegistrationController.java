package com.example.finalproject.controller;

import com.example.finalproject.models.ModelUser;
import com.example.finalproject.model.RoleEnum;
import com.example.finalproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/registration")
    public String regView(Model model) {
        model.addAttribute("user", new ModelUser());
        model.addAttribute("roles", RoleEnum.values());
        return "regis";
    }

    @PostMapping("/registration")
    public String reg(@Valid @ModelAttribute("user") ModelUser user,
                      @RequestParam(name = "roles[]", required = false) String[] selectedRoles,
                      BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", RoleEnum.values());
            return "regis";
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            model.addAttribute("roles", RoleEnum.values());
            return "regis";
        }

        try {
            user.setActive(true);
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            Set<RoleEnum> roles = new HashSet<>();
            if(selectedRoles != null){
                for(String role : selectedRoles){
                    try{
                        roles.add(RoleEnum.valueOf(role));
                    } catch(IllegalArgumentException ex){
                        model.addAttribute("roleError", "Неверная роль: " + role);
                        return "regis";
                    }
                }
            }
            if (roles.isEmpty()) {
                roles.add(RoleEnum.USER); // Default role if none selected
            }
            user.setRoles(roles);
            userRepository.save(user);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("generalError", "Ошибка при регистрации. Пожалуйста, попробуйте позже.");
            model.addAttribute("roles", RoleEnum.values());
            return "regis";
        }
    }
}