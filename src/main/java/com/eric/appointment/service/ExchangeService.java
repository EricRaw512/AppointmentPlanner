package com.eric.appointment.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.eric.appointment.entity.Appointment;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExchangeService {
    
    private final AppointmentService appointmentService;

    public boolean checkIfAllowedForExchange(int appointmentId) {
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        return appointment.getStart().minusHours(24).isAfter(LocalDateTime.now());
    }



}
