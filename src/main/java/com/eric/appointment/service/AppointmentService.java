package com.eric.appointment.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.eric.appointment.dao.AppointmentRepository;
import com.eric.appointment.entity.Appointment;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    
    private final AppointmentRepository appointmentRepository;

    public List<Appointment> getAppointmentByCustomerId(int id) {
        return appointmentRepository.findByCustomer_Id(id);
    }

    public List<Appointment> getAppointmentByProviderId(int id) {
        return appointmentRepository.findByProvider_Id(id);
    }

    @PreAuthorize(value = "ADMIN")
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
}
