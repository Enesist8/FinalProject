package com.example.finalproject.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Instruments")
public class Instrument {
    public Long getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(Long instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long instrumentId;




    private String name;
    private String description;
    private String brand;
    private String type;
    private double price; //Use double for simplicity, consider BigDecimal for large-scale apps

    // Getters and setters for all fields
    // ... (omitted for brevity) ...

}