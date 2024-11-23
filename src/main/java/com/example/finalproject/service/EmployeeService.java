package com.example.finalproject.service;

import com.example.finalproject.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface EmployeeService {
    List<Employee> findAll();
    Page<Employee> findAll(Pageable pageable);
    Optional<Employee> findById(Long id);
    Employee save(Employee employee); //Возвращает Employee
    Employee update(Long id, Employee updatedEmployee);
    void delete(Long id);
}