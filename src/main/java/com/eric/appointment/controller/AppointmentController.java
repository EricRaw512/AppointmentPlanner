package com.eric.appointment.controller;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eric.appointment.entity.Appointment;
import com.eric.appointment.entity.ChatMessage;
import com.eric.appointment.security.UserDetail;
import com.eric.appointment.service.AppointmentService;
import com.eric.appointment.service.ExchangeService;
import com.eric.appointment.service.UserService;
import com.eric.appointment.service.WorkService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("appointments")
public class AppointmentController {
    
    private final UserService userService;
    private final AppointmentService appointmentService;
    private final ExchangeService exchangeService;
    private final WorkService workService;

    @GetMapping("/all")
    public String showAllAppointment(Model model, @AuthenticationPrincipal UserDetail userDetail) {
        if (userDetail.hasRole("CUSTOMER")) {
            model.addAttribute("appointments", appointmentService.getAppointmentByCustomerId(userDetail.getId()));
        } else if (userDetail.hasRole("PROVIDER")) {
            model.addAttribute("appointments", appointmentService.getAppointmentByProviderId(userDetail.getId()));
        } else if (userDetail.hasRole("ADMIN")) {
            model.addAttribute("appointments", appointmentService.getAllAppointments());
        }

        return "appointments/listAppointments";
    }

    @GetMapping("/{id}")
    public String showAppointmentDetail(@PathVariable("id") int id, Model model, @AuthenticationPrincipal UserDetail userDetail) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        model.addAttribute("appointment", appointment);
        model.addAttribute("chatMessage", new ChatMessage());
        boolean allowedToRequestRejection = appointmentService.isCustomerAllowedToRejectAppointment(appointment.getCustomer().getId(), id);
        boolean allowedToAcceptRejection = appointmentService.isProviderAllowedToAcceptRejection(appointment.getProvider().getId(), id);
        boolean allowedToExchange = exchangeService.checkIfAllowedForExchange(id);
        model.addAttribute("allowedToRequestRejection", allowedToRequestRejection);
        model.addAttribute("allowedToAcceptRejection", allowedToAcceptRejection);
        model.addAttribute("allowedToExchange", allowedToExchange);
        if (allowedToRequestRejection) {
            model.addAttribute("remainingTime", FormatDuration(Duration.between(LocalDateTime.now(), appointment.getEnd().plusDays(1))));
        }
        String cancelNotAllowedReason = appointmentService.getCancelNotAllowedReason(appointment.getCustomer().getId(), id);
        model.addAttribute("allowedToCancel", cancelNotAllowedReason == null);
        model.addAttribute("cancelNotAllowedReason", cancelNotAllowedReason);
        return "appointments/appointmentDetail";
    }

    @GetMapping("/new")
    public String selectProvider(Model model) {
        model.addAttribute("providers", userService.getAllProvider());
        return "appointments/selectProvider";
    }

    @GetMapping("/new/{providerId}")
    public String selectService(@PathVariable("providerId") int id, Model model) {
        model.addAttribute("works", workService.getWorksByProviderId(id));
        model.addAttribute(id);
        return "appointments/selectService";
    }

    @GetMapping("/new/{providerId}/{workId}")
    public String selectDate(@PathVariable("workId") int workId, @PathVariable("providerId") int providerId, Model model) {
        model.addAttribute("providerId", providerId);
        model.addAttribute("workId", workId);
        return "appointments/selectDate";
    }

    @GetMapping("/new/{providerId}/{workId}/{date}")
    public String newAppointmentSummary(@PathVariable("providerId") int providerId, @PathVariable("workId") int workId, @PathVariable("date") String start, Model model, @AuthenticationPrincipal UserDetail userDetail) {
        if (!appointmentService.workAvailable(providerId, workId, userDetail.getId(), LocalDateTime.parse(start))) {
            return "redirect:/appointments/new";
        }

        model.addAttribute("work", workService.getWorkById(workId));
        model.addAttribute("provider", userService.getProviderById(providerId));
        model.addAttribute(providerId);
        model.addAttribute("start", LocalDateTime.parse(start));
        model.addAttribute("end", LocalDateTime.parse(start).plusMinutes(workService.getWorkById(workId).getDuration()));
        return "appointments/appointmentSummary";
    }

    @PostMapping("/new")
    public String saveAppointment(@RequestParam("workId") int workId, @RequestParam("providerId") int providerId, @RequestParam("start") String start, @AuthenticationPrincipal UserDetail userDetail) {
        appointmentService.createNewAppointment(workId, providerId, userDetail.getId(), LocalDateTime.parse(start));
        return "redirect:/appointments/all";
    }

    @PostMapping("/messages/new")
      public String addNewChatMessage(@ModelAttribute("chatMessage") ChatMessage chatMessage, @RequestParam("appointmentId") int appointmentId, @AuthenticationPrincipal UserDetail userDetail) {
        appointmentService.addMessageToAppointmentChat(appointmentId, userDetail.getId(), chatMessage);
        return "redirect:/appointments/" + appointmentId;
    }

    private String FormatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        return String.format("%02d:%02d", hours, minutes);
    }
}
