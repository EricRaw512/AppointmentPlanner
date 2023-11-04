package com.eric.appointment.entity.user;

import java.util.List;

import com.eric.appointment.entity.Appointment;
import com.eric.appointment.model.UserForm;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "customers")
@PrimaryKeyJoinColumn(name = "id_customer")
public class Customer extends User{

    @OneToMany(mappedBy = "customer")
    List<Appointment> appointments;
    
    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Customer(UserForm userForm, String encryptedPassword, Role role) {
        super(userForm, encryptedPassword, role);
    }
}
