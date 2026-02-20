package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.interfaces.Searchable;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Patient in MediTrack.
 * Demonstrates: inheritance, Cloneable (deep vs shallow copy), interface implementation,
 * polymorphism (overloaded search methods).
 * JPA: @Entity persisted to 'patients' table.
 */
@Entity
@Table(name = "patients")
public class Patient extends Person implements Searchable, Cloneable {

    @Column(name = "blood_group")
    private String bloodGroup;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "patient_allergies", joinColumns = @JoinColumn(name = "patient_id"))
    @Column(name = "allergy")
    private List<String> allergies;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "patient_medical_history", joinColumns = @JoinColumn(name = "patient_id"))
    @Column(name = "history_entry")
    private List<String> medicalHistory;

    // No-arg constructor
    public Patient() {
        super();
        this.allergies = new ArrayList<>();
        this.medicalHistory = new ArrayList<>();
    }

    // Full constructor — uses super() for constructor chaining
    public Patient(String id, String name, int age, String gender, String phone, String email,
                   String bloodGroup) {
        super(id, name, age, gender, phone, email);
        this.bloodGroup = bloodGroup;
        this.allergies = new ArrayList<>();
        this.medicalHistory = new ArrayList<>();
    }

    // --- Deep Clone Implementation ---
    // Demonstrates: deep copy vs shallow copy
    @Override
    public Patient clone() {
        try {
            Patient cloned = (Patient) super.clone(); // shallow copy first
            // Deep copy mutable fields
            cloned.allergies = new ArrayList<>(this.allergies);
            cloned.medicalHistory = new ArrayList<>(this.medicalHistory);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported for Patient", e);
        }
    }

    // --- Overriding abstract methods from MedicalEntity ---

    @Override
    public String getEntityType() {
        return "Patient";
    }

    @Override
    public String getSummary() {
        return getName() + " (Age: " + getAge() + ", Blood Group: " + bloodGroup + ")";
    }

    // --- Searchable interface implementation ---

    @Override
    public boolean matchesSearchCriteria(String keyword) {
        return containsIgnoreCase(getName(), keyword)
                || containsIgnoreCase(getId(), keyword)
                || containsIgnoreCase(bloodGroup, keyword);
    }

    // --- Polymorphism: overloaded search methods ---

    /**
     * Search patient by ID.
     */
    public boolean matchesById(String id) {
        return getId() != null && getId().equalsIgnoreCase(id);
    }

    /**
     * Search patient by name (overloaded).
     */
    public boolean matchesByName(String name) {
        return getName() != null && getName().toLowerCase().contains(name.toLowerCase());
    }

    /**
     * Search patient by age (overloaded).
     */
    public boolean matchesByAge(int age) {
        return getAge() == age;
    }

    // --- Getters and setters ---

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public List<String> getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(List<String> medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    @Override
    public String toString() {
        return "Patient{id='" + getId() + "', name='" + getName()
                + "', age=" + getAge() + ", bloodGroup='" + bloodGroup + "'}";
    }
}
