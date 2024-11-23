package com.example.finalproject.repository;

import com.example.finalproject.model.InstrumentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrumentCategoryRepository extends JpaRepository<InstrumentCategory, Long> {
}