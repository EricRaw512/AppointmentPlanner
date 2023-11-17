package com.eric.appointment.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.eric.appointment.dao.AppointmentRepository;
import com.eric.appointment.dao.ExchangeRequestRepository;
import com.eric.appointment.entity.Appointment;
import com.eric.appointment.entity.AppointmentStatus;
import com.eric.appointment.entity.ExchangeRequest;
import com.eric.appointment.entity.ExchangeRequestStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExchangeService {
    
    private final AppointmentService appointmentService;
    private final AppointmentRepository appointmentRepository;
    private final ExchangeRequestRepository exchangeRequestRepository;

    public boolean checkIfAllowedForExchange(int appointmentId) {
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        return appointment.getStart().minusHours(24).isAfter(LocalDateTime.now());
    }

    public List<Appointment> getEligibleAppointmentsForExchange(int appointmentId) {
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        return appointmentRepository.getEligibleAppointmentsForExchange(LocalDateTime.now().plusHours(24), appointment.getCustomer().getId(), appointment.getProvider().getId(), appointment.getWork().getId());
    }

    public boolean checkIfExchangeIsPossible(int oldAppointmentId, int newAppointmentId, int userId) {
        Appointment oldAppointment = appointmentService.getAppointmentById(oldAppointmentId);
        Appointment newAppointment = appointmentService.getAppointmentById(newAppointmentId);
        if (oldAppointment.getCustomer().getId() != userId) {
            throw new org.springframework.security.access.AccessDeniedException("Unauthorized");
        }

        return oldAppointment.getWork().getId().equals(newAppointment.getWork().getId())
                    && oldAppointment.getProvider().getId().equals(newAppointment.getProvider().getId())
                    && oldAppointment.getStart().minusHours(24).isAfter(LocalDateTime.now())
                    && newAppointment.getStart().minusHours(24).isAfter(LocalDateTime.now());
    }

    public boolean requestExchange(int oldAppointmentId, int newAppointmentId, int userId) {
        if (!checkIfExchangeIsPossible(oldAppointmentId, newAppointmentId, userId)) {
            return false;
        }

        Appointment oldAppointment = appointmentService.getAppointmentById(oldAppointmentId);
        Appointment newAppointment = appointmentService.getAppointmentById(newAppointmentId);
        oldAppointment.setStatus(AppointmentStatus.EXCHANGE_REQUESTED);
        appointmentRepository.save(oldAppointment);
        ExchangeRequest exchangeRequest = new ExchangeRequest(oldAppointment, newAppointment, ExchangeRequestStatus.PENDING);
        exchangeRequestRepository.save(exchangeRequest);
        return true;
    }

    public void acceptExchange(int exchangeId, int id) {
    }

    public void rejectExchange(int exchangeId, int id) {
    }
}
