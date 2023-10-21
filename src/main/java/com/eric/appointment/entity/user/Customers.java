package com.eric.appointment.entity.user;

import com.eric.appointment.model.UserForm;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "customers")
@PrimaryKeyJoinColumn(name = "id_customer")
public class Customers extends User{

    public Customers(UserForm userForm, String encryptedPassword, Role role) {
        super(userForm, encryptedPassword, role);
    }
}
