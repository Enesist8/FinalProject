package com.example.finalproject.model;

class CreateInstrumentCategoryRequest {
    private Long instrumentId;
    private Long categoryId;

    // Getters and setters
    public Long getInstrumentId() { return instrumentId; }
    public void setInstrumentId(Long instrumentId) { this.instrumentId = instrumentId; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}

