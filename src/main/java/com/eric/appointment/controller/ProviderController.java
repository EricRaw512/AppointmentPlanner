package com.eric.appointment.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eric.appointment.entity.WorkingPlan;
import com.eric.appointment.model.TimePeriod;
import com.eric.appointment.security.UserDetail;
import com.eric.appointment.service.WorkingPlanService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ProviderController {
    
    private final WorkingPlanService workingPlanService;

    @GetMapping("/availability")
    public String showProviderAvailability(Model model, @AuthenticationPrincipal UserDetail userDetail) {
        model.addAttribute("plan", workingPlanService.getWorkingPlanByProviderId(userDetail.getId()));
        model.addAttribute("breakModel", new TimePeriod());
        return "user/providerAvailability";
    }

    @PostMapping("/availability")
    public String updateProviderWorkingPlan(@ModelAttribute("plan") WorkingPlan workingPlan) {
        workingPlanService.updateWorkingPlan(workingPlan);
        return "redirect:/providers/availability";
    }

    @PostMapping("/availability/breaks/add")
    public String addProviderBreaks(@ModelAttribute("breakModel") TimePeriod timePeriod, @RequestParam("planId") int planId, @RequestParam("dayOfWeek") String dayOfWeek) {
        workingPlanService.addBreakToWorkingPlan(timePeriod, planId, dayOfWeek);
        return "redirect:/providers/availability";
    }

    @PostMapping("/avaiability/breaks/delete")
    public String deleteProviderBreaks(@ModelAttribute("breakModel") TimePeriod timePeriod, @RequestParam("planId") int planId, @RequestParam("dayOfWeek") String dayOfWeek) {
        workingPlanService.deleteBreakToWorkingPlan(timePeriod, planId, dayOfWeek);
        return "redirect:/providers/availability";
    }
}
