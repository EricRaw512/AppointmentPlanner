package com.eric.appointment.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eric.appointment.entity.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer>{

    List<Appointment> findByCustomer_Id(int customerId);

    List<Appointment> findByProvider_Id(int providerId);
}
