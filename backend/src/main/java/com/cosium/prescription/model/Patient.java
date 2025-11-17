package com.cosium.prescription.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class Patient {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false, unique = true)
    private String socialSecurityNumber;
    
    @Column(nullable = false)
    private LocalDate birthDate;
    
    @Column(nullable = false)
    private Boolean active;
    
    public Patient() {}
    
    public Patient(String firstName, String lastName, String socialSecurityNumber, 
                   LocalDate birthDate, Boolean active) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.socialSecurityNumber = socialSecurityNumber;
        this.birthDate = birthDate;
        this.active = active;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getSocialSecurityNumber() { return socialSecurityNumber; }
    public void setSocialSecurityNumber(String socialSecurityNumber) { 
        this.socialSecurityNumber = socialSecurityNumber; }
    
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
