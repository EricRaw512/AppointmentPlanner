package com.eric.appointment.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eric.appointment.entity.Appointment;
import com.eric.appointment.model.AppointmentRegisterForm;
import com.eric.appointment.security.UserDetail;
import com.eric.appointment.service.AppointmentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AjaxController {
    
    private final AppointmentService appointmentService;

    @GetMapping("/availableHours/{providerId}/{workId}/{date}")
    public List<AppointmentRegisterForm> getAvailableHours(@PathVariable("providerId") int providerId, @PathVariable("workId") int workId, @PathVariable("date") String date, @AuthenticationPrincipal UserDetail userDetail) {
        LocalDate localDate = LocalDate.parse(date);
        return appointmentService.getAvailableHours(providerId, workId, userDetail.getId() ,localDate)
                .stream()
                .map(timePeriod -> new AppointmentRegisterForm(workId, providerId, timePeriod.getStart().atDate(localDate), timePeriod.getEnd().atDate(localDate)))
                .collect(Collectors.toList());
    }

    @GetMapping("/users/{userId}/appointments")
    public List<Appointment> getAppointments(@PathVariable("userId") int userId, @AuthenticationPrincipal UserDetail userDetail) {
        if (userDetail.hasRole("CUSTOMER")) {
            return appointmentService.getAppointmentByCustomerId(userId);
        } else if (userDetail.hasRole("PROVIDER")) {
            return appointmentService.getAppointmentByProviderId(userId);
        } else if (userDetail.hasRole("ADMIN")) {
            return appointmentService.getAllAppointments();
        } else {
            return new ArrayList<>();
        }
    }
}
