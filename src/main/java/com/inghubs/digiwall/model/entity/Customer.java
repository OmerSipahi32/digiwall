package com.inghubs.digiwall.model.entity;

import com.inghubs.digiwall.model.base.ExtendedModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer extends ExtendedModel {

    private String name;
    private String surname;

    @Column(unique = true)
    private String tckn;

    @Column(unique = true)
    private String username;

    private String password;

    private String role; // EMPLOYEE or CUSTOMER
}