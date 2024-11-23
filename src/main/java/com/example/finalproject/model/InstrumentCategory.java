package com.example.finalproject.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Entity
@Data // Lombok annotation to generate getters, setters, etc.
@AllArgsConstructor
@Table(name = "instrument_categories")
public class InstrumentCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long instrumentCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrument_id")
    private Instrument instrument;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;

    // Getters and setters
    // ... (Add getters and setters for all fields) ...

    public InstrumentCategory() {
    } // JPA constructor

    public Long getInstrumentCategoryId() {
        return instrumentCategoryId;
    }

    public void setInstrumentCategoryId(Long instrumentCategoryId) {
        this.instrumentCategoryId = instrumentCategoryId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }
}
