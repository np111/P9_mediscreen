package com.mediscreen.patients.persistence.entity;

import com.mediscreen.patients.api.model.Gender;
import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "patients")
@NoArgsConstructor
@Data
public class PatientEntity {
    @Id
    @Column(columnDefinition = "BINARY", length = 16)
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "gender", columnDefinition = "VARCHAR", length = 6)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "state")
    private String state;

    @Column(name = "country_code")
    private String countryCode;
}
