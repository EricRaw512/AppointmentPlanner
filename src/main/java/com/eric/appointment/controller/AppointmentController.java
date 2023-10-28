package com.eric.appointment.controller;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eric.appointment.entity.Appointment;
import com.eric.appointment.entity.ChatMessage;
import com.eric.appointment.security.UserDetail;
import com.eric.appointment.service.AppointmentService;
import com.eric.appointment.service.ExchangeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("appointments")
public class AppointmentController {
    
    private final AppointmentService appointmentService;
    private final ExchangeService exchangeService;

    @GetMapping("/all")
    public String showAllAppointment(Model model, @AuthenticationPrincipal UserDetail userDetail) {
        if (userDetail.hasRole("ROLE_CUSTOMER")) {
            model.addAttribute("appointments", appointmentService.getAppointmentByCustomerId(userDetail.getId()));
        } else if (userDetail.hasRole("ROLE_PROVIDER")) {
            model.addAttribute("appointments", appointmentService.getAppointmentByProviderId(userDetail.getId()));
        } else if (userDetail.hasRole("ROLE_ADMIN")) {
            model.addAttribute("appointments", appointmentService.getAllAppointments());
        }

        return "appointments/listAppointments";
    }

    @GetMapping("/{id}")
    public String showAppointmentDetail(@PathVariable("id") int id, Model model, @AuthenticationPrincipal UserDetail userDetail) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        model.addAttribute("appointment", appointment);
        model.addAttribute("chatMessage", new ChatMessage());
        boolean allowedToRequestRejection = appointmentService.isCustomerAllowedToRejectAppointment(userDetail.getId(), id);
        boolean allowedToAcceptRejection = appointmentService.isProviderAllowedToAcceptRejection(userDetail.getId(), id);
        boolean allowedToExchange = exchangeService.checkIfAllowedForExchange(userDetail.getId(), id);
        model.addAttribute("allowedToRequestRejection", allowedToRequestRejection);
        model.addAttribute("allowedToAcceptRejection", allowedToAcceptRejection);
        model.addAttribute("allowedToExchange", allowedToExchange);
        if (allowedToRequestRejection) {
            model.addAttribute("remainingTime", FormatDuration(Duration.between(LocalDateTime.now(), appointment.getEnd().plusDays(1))));
        }
        String cancelNotAllowedReason = appointmentService.getCancelNotAllowedReason(userDetail.getId(), id);
        model.addAttribute("allowedToCancel", cancelNotAllowedReason == null);
        model.addAttribute("cancelNotAllowedReason", cancelNotAllowedReason);
        return "appointments/appointmentDetail";
    }

    @GetMapping("/new")
    public String newAppointment(Model model, @AuthenticationPrincipal UserDetail userDetail) {
        // model.addAttribute("providers", userService.find)
        return "appointments/newAppointment";
    }

    private String FormatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        return String.format("%02d:%02d", hours, minutes);
    }
}
