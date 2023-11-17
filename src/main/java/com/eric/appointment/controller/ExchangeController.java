package com.eric.appointment.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eric.appointment.entity.Appointment;
import com.eric.appointment.security.UserDetail;
import com.eric.appointment.service.AppointmentService;
import com.eric.appointment.service.ExchangeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/exchange")
public class ExchangeController {
    
    private final ExchangeService exchangeService;
    private final AppointmentService appointmentService;

    @GetMapping("/{appointmentId}")
    public String setExchange(@PathVariable("appointmentId") int appointmentId , Model model) {
        List<Appointment> eligibleAppointments = exchangeService.getEligibleAppointmentsForExchange(appointmentId);
        model.addAttribute("appointmentId", appointmentId);
        model.addAttribute("numberOfEligibleAppointments", eligibleAppointments.size());
        model.addAttribute("eligibleAppointments", eligibleAppointments);
        return "exchange/listProposals";
    }

    @GetMapping("/{oldAppointmentId}/{newAppointmentId}")
    public String showExchangeSummaryScreen(@PathVariable("oldAppointmentId") int oldAppointmentId, @PathVariable("newAppointmentId") int newAppointmentId, Model model, @AuthenticationPrincipal UserDetail userDetail) {
        if (!exchangeService.checkIfExchangeIsPossible(oldAppointmentId, newAppointmentId, userDetail.getId())) {
            return "redirect:/appointments/all";
        }

        model.addAttribute("oldAppointment", appointmentService.getAppointmentById(oldAppointmentId));
        model.addAttribute("newAppointment", appointmentService.getAppointmentById(newAppointmentId));
        return "exchange/exchangeSummary";
    }

    @PostMapping
    public String processExchangeRequest(@RequestParam("oldAppointmentId") int oldAppointmentId, @RequestParam("newAppointmentId") int newAppointmentId, Model model, @AuthenticationPrincipal UserDetail userDetail) {
        boolean result = exchangeService.requestExchange(oldAppointmentId, newAppointmentId, userDetail.getId());
        if (result) {
            model.addAttribute("message", "Exchange request successfully sent!");
        } else {
            model.addAttribute("message", "Error! Exchange not sent!");
        }

        return "exchange/requestConfirmation";
    }

    @PostMapping("/accept")
    public String processExchangeAcceptation(@RequestParam("exchangeId") int exchangeId, Model model, @AuthenticationPrincipal UserDetail userDetail) {
        exchangeService.acceptExchange(exchangeId, userDetail.getId());
        return "redirect:/appointments/all";
    }

    @PostMapping("/reject")
    public String processExchangeRejection(@RequestParam("exchangeId") int exchangeId, Model model, @AuthenticationPrincipal UserDetail userDetail) {
        exchangeService.rejectExchange(exchangeId, userDetail.getId());
        return "redirect:/appointments/all";
    }
}
