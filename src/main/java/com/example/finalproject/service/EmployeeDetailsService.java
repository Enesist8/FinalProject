package com.example.finalproject.service;

import com.example.finalproject.model.Employee;
import com.example.finalproject.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class EmployeeDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    //Crucially, for this to work, your Employee class must have this
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); //You must have this or something similar for encoding/decoding


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Employee> employee = employeeRepository.findByLogin(username);
        return employee.map(employee1 -> new org.springframework.security.core.userdetails.User(
                employee1.getLogin(),
                employee1.getPassword(),
                getAuthorities(employee1.getRole())
        )).orElseThrow(() -> new UsernameNotFoundException("Employee not found with login: " + username));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }
}