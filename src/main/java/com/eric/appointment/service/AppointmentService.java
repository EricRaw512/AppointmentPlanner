package com.eric.appointment.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.eric.appointment.dao.AppointmentRepository;
import com.eric.appointment.entity.Appointment;
import com.eric.appointment.exception.AppointmentNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    
    private final AppointmentRepository appointmentRepository;

    @PreAuthorize("#customerId == principal.id")
    public List<Appointment> getAppointmentByCustomerId(int id) {
        return appointmentRepository.findByCustomer_Id(id);
    }

    @PreAuthorize("#providerId == principal.id")
    public List<Appointment> getAppointmentByProviderId(int id) {
        return appointmentRepository.findByProvider_Id(id);
    }

    @PreAuthorize(value = "ADMIN")
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @PreAuthorize("returnObject.provider.id == principal.id or returnObject.customer.id == principal.id or hasRole('ADMIN')")
    public Appointment getAppointmentById(int id) {
    return appointmentRepository.findById(id)
            .orElseThrow(AppointmentNotFoundException::new);
    }

    public int getNumberOfScheduledAppointments(int id) {
        return appointmentRepository.findCanceledByUser(id).size();
    }

    public Object getNumberOfCanceledAppointments(int id) {
        return appointmentRepository.findScheduledByUserId(id).size();
    }
}
