package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.interfaces.Searchable;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Doctor in MediTrack.
 * Demonstrates: inheritance (extends Person), interface implementation (Searchable),
 * polymorphism (overloaded search, overriding getSummary), enums.
 * JPA: @Entity with TABLE_PER_CLASS inheritance from Person/MedicalEntity.
 */
@Entity
@Table(name = "doctors")
public class Doctor extends Person implements Searchable {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Specialization specialization;

    @Column(name = "consultation_fee")
    private double consultationFee;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "doctor_slots", joinColumns = @JoinColumn(name = "doctor_id"))
    @Column(name = "slot")
    private List<String> availableSlots;

    @Column(name = "years_of_experience")
    private int yearsOfExperience;

    // No-arg constructor
    public Doctor() {
        super();
        this.availableSlots = new ArrayList<>();
    }

    // Constructor chaining with super()
    public Doctor(String id, String name, int age, String gender, String phone, String email,
                  Specialization specialization, double consultationFee, int yearsOfExperience) {
        super(id, name, age, gender, phone, email);
        this.specialization = specialization;
        this.consultationFee = consultationFee;
        this.yearsOfExperience = yearsOfExperience;
        this.availableSlots = new ArrayList<>();
    }

    // --- Overriding abstract methods from MedicalEntity ---

    @Override
    public String getEntityType() {
        return "Doctor";
    }

    @Override
    public String getSummary() {
        return "Dr. " + getName() + " — " + specialization.getDisplayName()
                + " (Fee: $" + String.format("%.2f", consultationFee) + ")";
    }

    // --- Searchable interface implementation ---

    @Override
    public boolean matchesSearchCriteria(String keyword) {
        return containsIgnoreCase(getName(), keyword)
                || containsIgnoreCase(specialization.name(), keyword)
                || containsIgnoreCase(specialization.getDisplayName(), keyword)
                || containsIgnoreCase(getId(), keyword);
    }

    @Override
    public String getSearchableSummary() {
        return getSummary();
    }

    // --- Polymorphism: overloaded search methods ---

    /**
     * Search by ID.
     */
    public boolean matchesById(String id) {
        return getId() != null && getId().equalsIgnoreCase(id);
    }

    /**
     * Search by name (overloaded).
     */
    public boolean matchesByName(String name) {
        return getName() != null && getName().toLowerCase().contains(name.toLowerCase());
    }

    /**
     * Search by specialization (overloaded).
     */
    public boolean matchesBySpecialization(Specialization spec) {
        return this.specialization == spec;
    }

    // --- Getters and setters ---

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public List<String> getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(List<String> availableSlots) {
        this.availableSlots = availableSlots;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    @Override
    public String toString() {
        return "Doctor{id='" + getId() + "', name='" + getName()
                + "', specialization=" + specialization
                + ", fee=" + consultationFee + "}";
    }
}
