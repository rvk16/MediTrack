package com.airtribe.meditrack.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Abstract base class for all medical entities in MediTrack.
 * Demonstrates: abstraction, static blocks, encapsulation, equals/hashCode.
 * JPA: @MappedSuperclass — shared columns inherited by all entity subclasses.
 */
@MappedSuperclass
public abstract class MedicalEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    // Static counter — initialized via static block
    private static final Logger log = LoggerFactory.getLogger(MedicalEntity.class);

    private static int totalEntitiesCreated;

    static {
        totalEntitiesCreated = 0;
        log.info("[Static Block] MedicalEntity class loaded.");
    }

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // No-arg constructor
    protected MedicalEntity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        totalEntitiesCreated++;
    }

    // Parameterized constructor — constructor chaining with this()
    protected MedicalEntity(String id) {
        this();
        this.id = id;
    }

    // Abstract methods — subclasses must implement
    public abstract String getEntityType();

    public abstract String getSummary();

    // Getters and setters — encapsulation
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static int getTotalEntitiesCreated() {
        return totalEntitiesCreated;
    }

    // equals and hashCode based on id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalEntity that = (MedicalEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getEntityType() + "{id='" + id + "'}";
    }
}
