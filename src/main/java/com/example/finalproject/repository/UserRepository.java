package com.example.finalproject.repository;

import com.example.finalproject.models.ModelUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<ModelUser, Long> {
    ModelUser findByUsername(String username);
    boolean existsByUsername(String username);
}
