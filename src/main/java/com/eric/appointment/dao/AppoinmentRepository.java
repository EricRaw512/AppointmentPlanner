package com.eric.appointment.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eric.appointment.entity.Appointment;

public interface AppoinmentRepository extends JpaRepository<Appointment, Integer>{
    
}
