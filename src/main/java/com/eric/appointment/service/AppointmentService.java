package com.eric.appointment.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.eric.appointment.dao.AppointmentRepository;
import com.eric.appointment.dao.ChatMessageRepository;
import com.eric.appointment.entity.Appointment;
import com.eric.appointment.entity.AppointmentStatus;
import com.eric.appointment.entity.ChatMessage;
import com.eric.appointment.entity.Work;
import com.eric.appointment.entity.WorkingPlan;
import com.eric.appointment.entity.user.Provider;
import com.eric.appointment.entity.user.Role;
import com.eric.appointment.entity.user.User;
import com.eric.appointment.exception.AppointmentNotFoundException;
import com.eric.appointment.model.DayPlan;
import com.eric.appointment.model.TimePeriod;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    
    private final int NUMBER_OF_ALLOWED_CANCELLATIONS_PER_MONTH = 2;
    private final UserService userService;
    private final AppointmentRepository appointmentRepository;
    private final WorkService workService;
    private final ChatMessageRepository chatMessageRepository;

    @PreAuthorize("#id == principal.id")
    public List<Appointment> getAppointmentByCustomerId(int id) {
        return appointmentRepository.findByCustomer_Id(id);
    }

    @PreAuthorize("#id == principal.id")
    public List<Appointment> getAppointmentByProviderId(int id) {
        return appointmentRepository.findByProvider_Id(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @PostAuthorize("returnObject.provider.id == principal.id or returnObject.customer.id == principal.id or hasRole('ADMIN')")
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
        User user = userService.getCustomerById(userId);
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        
        return appointment.getCustomer().equals(user) && appointment.getStatus().equals(AppointmentStatus.FINISHED) && !LocalDateTime.now().isAfter(appointment.getEnd().plusDays(1));
    }

    public boolean isProviderAllowedToAcceptRejection(int userId, int appointmentId) {
        User user = userService.getProviderById(userId);
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        
        return appointment.getProvider().equals(user) && appointment.getStatus().equals(AppointmentStatus.REJECTION_REQUESTED);
    }

    public String getCancelNotAllowedReason(int userId, int appointmentId) {
        User user = userService.getCustomerById(userId);
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
        return appointmentRepository.findByCustomerIdCanceledAfterDate(userId, LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay());
    }

    public boolean workAvailable(int providerId, int workId, int customerId, LocalDateTime start) {
        Work work = workService.getWorkById(workId);
        TimePeriod timePeriod = new TimePeriod(start.toLocalTime(), start.toLocalTime().plusMinutes(work.getDuration()));
        return getAvailableHours(providerId, workId, customerId, start.toLocalDate()).contains(timePeriod);
    }

    public List<TimePeriod> getAvailableHours(int providerId, int workId, int customerId, LocalDate localDate) {
        Provider provider = userService.getProviderById(providerId);
        WorkingPlan workingPlan = provider.getWorkingPlan();
        DayPlan selectedDay = workingPlan.getDay(localDate.getDayOfWeek().toString().toLowerCase());
        List<Appointment> providerAppointments = getAppointmentByProviderIdAtDay(providerId, localDate);
        List<Appointment> customerAppointments = getAppointmentByCustomerIdAtDay(customerId, localDate);
        List<TimePeriod> availablePeriods = selectedDay.timePeriodWithBreakExcluded();
        availablePeriods = excludeAppointmentFromTimePeriod(availablePeriods, providerAppointments);
        availablePeriods = excludeAppointmentFromTimePeriod(availablePeriods, customerAppointments);
        return calculateAvailableHours(availablePeriods, workService.getWorkById(workId));
    }

    private List<TimePeriod> calculateAvailableHours(List<TimePeriod> availablePeriods, Work work) {
        List<TimePeriod> availableHours = new ArrayList<>();
        for (TimePeriod period : availablePeriods) {
            TimePeriod workPeriod = new TimePeriod(period.getStart(), period.getStart().plusMinutes(work.getDuration()));
            if (workPeriod.getStart().isAfter(period.getStart()) || workPeriod.getStart().equals(period.getStart())) {
                while (workPeriod.getEnd().isBefore(period.getEnd()) || workPeriod.getEnd().equals(period.getEnd())) {
                    availableHours.add(new TimePeriod(workPeriod.getStart(), workPeriod.getStart().plusMinutes(work.getDuration())));
                    workPeriod.setStart(workPeriod.getStart().plusMinutes(work.getDuration()));
                    workPeriod.setEnd(workPeriod.getEnd().plusMinutes(work.getDuration()));
                }
            }
        }
        return availableHours;
    }

    private List<TimePeriod> excludeAppointmentFromTimePeriod(List<TimePeriod> availablePeriods,
        List<Appointment> appointments) {
        List<TimePeriod> toAdd = new ArrayList<>();
        Collections.sort(appointments);
        for (Appointment appointment : appointments) {
            for (TimePeriod period : availablePeriods) {
                if ((appointment.getStart().toLocalTime().isBefore(period.getStart()) || appointment.getStart().toLocalTime().equals(period.getStart())) && appointment.getEnd().toLocalTime().isAfter(period.getStart()) && appointment.getEnd().toLocalTime().isBefore(period.getEnd())) {
                    period.setStart(appointment.getEnd().toLocalTime());
                }
                if (appointment.getStart().toLocalTime().isAfter(period.getStart()) && appointment.getStart().toLocalTime().isBefore(period.getEnd()) && appointment.getEnd().toLocalTime().isAfter(period.getEnd()) || appointment.getEnd().toLocalTime().equals(period.getEnd())) {
                    period.setEnd(appointment.getStart().toLocalTime());
                }
                if (appointment.getStart().toLocalTime().isAfter(period.getStart()) && appointment.getEnd().toLocalTime().isBefore(period.getEnd())) {
                    toAdd.add(new TimePeriod(period.getStart(), appointment.getStart().toLocalTime()));
                    period.setStart(appointment.getEnd().toLocalTime());
                }
            }
        }
        availablePeriods.addAll(toAdd);
        Collections.sort(availablePeriods);
        return availablePeriods;
    }

    private List<Appointment> getAppointmentByCustomerIdAtDay(int customerId, LocalDate localDate) {
        return appointmentRepository.findByCustomerIdWithStartInPeriod(customerId, localDate.atStartOfDay(), localDate.atStartOfDay().plusDays(1));
    }

    private List<Appointment> getAppointmentByProviderIdAtDay(int providerId, LocalDate localDate) {
        return appointmentRepository.findByProviderIdWithStartInPeriod(providerId, localDate.atStartOfDay(), localDate.atStartOfDay().plusDays(1));
    }

    public void createNewAppointment(int workId, int providerId, int customerId, LocalDateTime start) {
        if (!workAvailable(providerId, workId, customerId, start)) {
            throw new RuntimeException();
        }

        Appointment appointment = new Appointment();
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setCustomer(userService.getCustomerById(customerId));
        appointment.setProvider(userService.getProviderById(providerId));
        Work work = workService.getWorkById(workId);
        appointment.setWork(work);
        appointment.setStart(start);
        appointment.setEnd(start.plusMinutes(work.getDuration()));
        appointmentRepository.save(appointment);
    }

    public void addMessageToAppointmentChat(int appointmentId, int userId, ChatMessage chatMessage) {
        Appointment appointment = getAppointmentById(appointmentId);
        if (appointment.getProvider().getId() != userId && appointment.getCustomer().getId() != userId) {
            throw new org.springframework.security.access.AccessDeniedException("Unauthorized");
        }
        
        chatMessage.setSender(userService.getUserById(userId));
        chatMessage.setAppointment(appointment);
        chatMessage.setCreatedAt(LocalDateTime.now());
        chatMessageRepository.save(chatMessage);
    } 
}
