package com.airtribe.meditrack.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/**
 * Abstract class representing a person in the system.
 * Demonstrates: inheritance (extends MedicalEntity), constructor chaining (super/this),
 * encapsulation (private fields + getters/setters), access modifiers.
 * JPA: @MappedSuperclass — Person columns inherited by Doctor and Patient tables.
 */
@MappedSuperclass
public abstract class Person extends MedicalEntity {

    @Column(nullable = false)
    private String name;

    @Column
    private int age;

    @Column
    private String gender;

    @Column
    private String phone;

    @Column
    private String email;

    // No-arg constructor
    protected Person() {
        super();
    }

    // Constructor with id — chains to MedicalEntity(id)
    protected Person(String id) {
        super(id);
    }

    // Full constructor — constructor chaining with this()
    protected Person(String id, String name, int age, String gender, String phone, String email) {
        this(id);
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
    }

    // Getters and setters — encapsulation with private fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return getEntityType() + "{id='" + getId() + "', name='" + name + "', age=" + age + "}";
    }
}
