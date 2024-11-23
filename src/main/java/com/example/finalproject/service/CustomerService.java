package com.example.finalproject.service;

import com.example.finalproject.model.Customer;
import com.example.finalproject.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers;
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    // Update only the fields that might have changed;  Avoid overwriting other fields:
                    existingCustomer.setFirstName(updatedCustomer.getFirstName());
                    existingCustomer.setLastName(updatedCustomer.getLastName());
                    existingCustomer.setEmail(updatedCustomer.getEmail());
                    existingCustomer.setPhone(updatedCustomer.getPhone());
                    existingCustomer.setAddress(updatedCustomer.getAddress());
                    return customerRepository.save(existingCustomer);
                })
                .orElseThrow(() -> new RuntimeException("Customer not found")); //Improved exception handling

    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
