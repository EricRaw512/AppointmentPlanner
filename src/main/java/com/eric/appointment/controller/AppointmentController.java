package com.eric.appointment.controller;

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
import com.eric.appointment.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("appointments")
public class AppointmentController {
    
    private final UserService userService;
    private final AppointmentService appointmentService;

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
        return "appointments/appointmentDetail";
    }
}
