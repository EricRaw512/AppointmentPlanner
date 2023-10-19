package com.eric.appointment.entity.user.customer;

import com.eric.appointment.entity.user.Role;
import com.eric.appointment.entity.user.User;
import com.eric.appointment.model.UserForm;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
@PrimaryKeyJoinColumn(name = "id_customer")
public class Customers extends User{

    public Customers(UserForm userForm, String encryptedPassword, Role role) {
        super(userForm, encryptedPassword, role);
    }
}
