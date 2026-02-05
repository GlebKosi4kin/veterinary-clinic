package com.petclinic.model;

import java.time.LocalDateTime;

public class PatientCard {
    private Integer id;
    private Integer petId;
    private String notes;
    private LocalDateTime createdAt;

    public PatientCard() {}

    public PatientCard(Integer id, Integer petId, String notes, LocalDateTime createdAt) {
        this.id = id;
        this.petId = petId;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
