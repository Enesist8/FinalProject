package com.example.finalproject.service;

import com.example.finalproject.model.Employee;
import com.example.finalproject.repository.EmployeeRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // ... other methods (findAll, findAll, findById, save, delete) ...

    @Override
    @Transactional
    public Employee update(Long id, Employee updatedEmployee) {
        Optional<Employee> existingEmployee = employeeRepository.findById(id);
        if (existingEmployee.isPresent()) {
            Employee employee = existingEmployee.get();
            //Update other fields
            employee.setFirstName(updatedEmployee.getFirstName());
            employee.setLastName(updatedEmployee.getLastName());
            employee.setEmail(updatedEmployee.getEmail());
            employee.setRole(updatedEmployee.getRole());
            employee.setLogin(updatedEmployee.getLogin());

            //Secure password update - using BCrypt directly
            if (updatedEmployee.getPassword() != null && !updatedEmployee.getPassword().isEmpty()) {
                String hashedPassword = BCrypt.hashpw(updatedEmployee.getPassword(), BCrypt.gensalt());
                employee.setPassword(hashedPassword);
            }
            return employeeRepository.save(employee);
        } else {
            throw new RuntimeException("Employee not found for id: " + id);
        }
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }



    @Override
    public Page<Employee> findAll(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    @Transactional
    public Employee save(Employee employee) {
        String hashedPassword = BCrypt.hashpw(employee.getPassword(), BCrypt.gensalt());
        employee.setPassword(hashedPassword);
        return employeeRepository.save(employee);
    }



    @Override
    @Transactional
    public void delete(Long id) {
        try {
            employeeRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Employee not found for id: " + id, e);
        }
    }




    @Transactional // Wrap the entire migration in a transaction for atomicity
    public void migratePasswords() {
        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {
            // Skip employees without passwords (e.g., newly created and not yet set)
            if (employee.getPassword() != null && !employee.getPassword().isEmpty()) {
                try {
                    String newHashedPassword = rehashPassword(employee.getPassword());
                    employee.setPassword(newHashedPassword);
                    employeeRepository.save(employee);  // Save the updated employee
                    System.out.println("Password updated for employee: " + employee.getLogin());
                } catch (Exception e) {
                    // Log the error; don't halt migration for a single employee
                    System.err.println("Error updating password for employee " + employee.getLogin() + ": " + e.getMessage());
                }
            }
        }
    }
    private String rehashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}