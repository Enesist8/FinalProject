package com.example.finalproject.service;

import com.example.finalproject.model.InstrumentCategory;
import com.example.finalproject.model.Instrument;
import com.example.finalproject.model.Category;
import com.example.finalproject.repository.InstrumentCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class InstrumentCategoryService {

    private static final Logger log = LoggerFactory.getLogger(InstrumentCategoryService.class);

    @Autowired
    private InstrumentCategoryRepository instrumentCategoryRepository;
    @Autowired
    private InstrumentService instrumentService; // Inject InstrumentService
    @Autowired
    private CategoryService categoryService;     // Inject CategoryService


    @Transactional(readOnly = true)
    public List<InstrumentCategory> findAll() {
        return instrumentCategoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<InstrumentCategory> findById(Long id) {
        log.info("Retrieving InstrumentCategory with ID: {}", id);
        return instrumentCategoryRepository.findById(id).stream()
                .filter(category -> Objects.nonNull(category.getInstrument()) && Objects.nonNull(category.getCategory()))
                .findFirst(); // Ensures instrument and category aren't null
    }

    @Transactional
    public InstrumentCategory create(Long instrumentId, Long categoryId) {
        Optional<Instrument> instrumentOptional = instrumentService.findById(instrumentId);
        Optional<Category> categoryOptional = categoryService.findById(categoryId);

        if (instrumentOptional.isPresent() && categoryOptional.isPresent()) {
            InstrumentCategory instrumentCategory = new InstrumentCategory();
            instrumentCategory.setInstrument(instrumentOptional.get());
            instrumentCategory.setCategory(categoryOptional.get());
            try {
                return instrumentCategoryRepository.save(instrumentCategory);
            } catch (DataIntegrityViolationException e) {
                log.error("Error creating InstrumentCategory: Data integrity violation", e);
                throw new RuntimeException("Error creating InstrumentCategory: Duplicate entry or constraint violation");
            } catch (Exception e) {
                log.error("Error creating InstrumentCategory: ", e);
                throw new RuntimeException("Error creating InstrumentCategory. Please try again.");
            }
        } else {
            throw new RuntimeException("Instrument or Category not found.");
        }
    }


    @Transactional
    public void update(InstrumentCategory instrumentCategory) {
        // Retrieve the existing InstrumentCategory from the database
        InstrumentCategory existingCategory = instrumentCategoryRepository.findById(instrumentCategory.getInstrumentCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Instrument Category not found"));

        // Update the properties of the existing category with the values from the provided instrumentCategory.
        existingCategory.setInstrument(instrumentCategory.getInstrument());
        existingCategory.setCategory(instrumentCategory.getCategory());
        // ... update other properties as needed ...


        // Save the updated InstrumentCategory
        instrumentCategoryRepository.save(existingCategory);
    }


    @Transactional
    public void delete(Long id) {
        try {
            instrumentCategoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Attempt to delete non-existent InstrumentCategory with ID: {}", id);
            throw new RuntimeException("InstrumentCategory not found.");
        } catch (Exception e) {
            log.error("Error deleting InstrumentCategory: ", e);
            throw new RuntimeException("Error deleting InstrumentCategory. Please try again.");
        }
    }
    @Transactional
    public InstrumentCategory save(InstrumentCategory instrumentCategory) {
        return instrumentCategoryRepository.save(instrumentCategory);
    }
}