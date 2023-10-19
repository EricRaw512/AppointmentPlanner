package com.eric.appointment.entity.user;

import com.eric.appointment.model.UserForm;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "providers")
public class Provider extends User{
    
    public Provider(UserForm userForm, String encryptedPassword, Role role) {
        super(userForm, encryptedPassword, role);
    }
}
