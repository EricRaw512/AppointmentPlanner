package com.eric.appointment.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.eric.appointment.dao.AppointmentRepository;
import com.eric.appointment.dao.user.UserRepository;
import com.eric.appointment.entity.Appointment;
import com.eric.appointment.entity.AppointmentStatus;
import com.eric.appointment.entity.user.Role;
import com.eric.appointment.entity.user.User;
import com.eric.appointment.exception.AppointmentNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    
    private final int NUMBER_OF_ALLOWED_CANCELLATIONS_PER_MONTH = 1;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    @PreAuthorize("#Id == principal.id")
    public List<Appointment> getAppointmentByCustomerId(int id) {
        return appointmentRepository.findByCustomer_Id(id);
    }

    @PreAuthorize("#id == principal.id")
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
        return appointmentRepository.findScheduledByUserId(id).size();
    }

    public int getNumberOfCanceledAppointments(int id) {
        return appointmentRepository.findCanceledByUser(id).size();
    }

    public boolean isCustomerAllowedToRejectAppointment(int userId, int appointmentId) {
        User user = userRepository.findById(userId).orElse(null);
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        
        return appointment.getCustomer().equals(user) && appointment.getStatus().equals(AppointmentStatus.FINISHED) && !LocalDateTime.now().isAfter(appointment.getEnd().plusDays(1));
    }

    public boolean isProviderAllowedToAcceptRejection(int userId, int appointmentId) {
        User user = userRepository.findById(userId).orElse(null);
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        
        return appointment.getProvider().equals(user) && appointment.getStatus().equals(AppointmentStatus.REJECTION_REQUESTED);
    }

    public String getCancelNotAllowedReason(int userId, int appointmentId) {
        User user = userRepository.findById(userId).orElse(null);
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        
        if (user.getRole() == Role.ADMIN) {
            return "Only provider or customer can cancel appointment";
        }

        if (appointment.getProvider().equals(user)) {
            if (appointment.getStatus().equals(AppointmentStatus.SCHEDULED)) {
                return null;
            }

            return "Only appointment with scheduled status can be cancelled";
        }

        if (appointment.getCustomer().equals(user)) {
            if (!appointment.getStatus().equals(AppointmentStatus.SCHEDULED)) {
                return "Only appointments with scheduled status can be cancelled.";
            } else if (LocalDateTime.now().plusDays(1).isAfter(appointment.getStart())) {
                return "Appointments which will be in less than 24 hours cannot be canceled.";
            } else if (!appointment.getWork().isEditable()) {
                return "This type of appointment can be canceled only by Provider.";
            } else if (getCanceledAppointmentsByCustomerIdForCurrentMonth(userId).size() >= NUMBER_OF_ALLOWED_CANCELLATIONS_PER_MONTH) {
                return "You can't cancel this appointment because you exceeded maximum number of cancellations in this month.";
            } else {
                return null;
            }
        }

        return null;
    }

    private List<Appointment> getCanceledAppointmentsByCustomerIdForCurrentMonth(int userId) {
        return appointmentRepository.findByCustomerIdCancceledAfterDate(userId, LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay());
    }
}
