package com.eric.appointment.entity.user.customer;

import com.eric.appointment.entity.user.Role;
import com.eric.appointment.model.UserForm;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Table(name ="retail_customer")
@Entity
@PrimaryKeyJoinColumn(name = "id_customer")
public class RetailCustomer extends Customers {
    
    public RetailCustomer(UserForm userForm, String encryptedPassword, Role role) {
        super(userForm, encryptedPassword, role);
    }
}
