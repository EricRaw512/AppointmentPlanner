package com.eric.appointment.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.eric.appointment.dao.AppointmentRepository;
import com.eric.appointment.entity.Appointment;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExchangeService {
    
    private final AppointmentRepository appointmentRepository;

    public boolean checkIfAllowedForExchange(int userId, int appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        return appointment.getStart().minusHours(24).isAfter(LocalDateTime.now());
    }



}
