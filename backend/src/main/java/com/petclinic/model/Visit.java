package com.petclinic.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Visit {
    private Integer id;
    private LocalDate visitDate;
    private String description;
    private Integer petId;
    private Integer vetId;
    private LocalDateTime createdAt;

    public Visit() {}

    public Visit(Integer id, LocalDate visitDate, String description, Integer petId, Integer vetId, LocalDateTime createdAt) {
        this.id = id;
        this.visitDate = visitDate;
        this.description = description;
        this.petId = petId;
        this.vetId = vetId;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public Integer getVetId() {
        return vetId;
    }

    public void setVetId(Integer vetId) {
        this.vetId = vetId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
