package com.eric.appointment.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eric.appointment.entity.WorkingPlan;
import com.eric.appointment.model.ChangePasswordForm;
import com.eric.appointment.model.TimePeriod;
import com.eric.appointment.model.UserForm;
import com.eric.appointment.security.UserDetail;
import com.eric.appointment.service.AppointmentService;
import com.eric.appointment.service.UserService;
import com.eric.appointment.service.WorkService;
import com.eric.appointment.service.WorkingPlanService;
import com.eric.appointment.validator.groups.CreateProvider;
import com.eric.appointment.validator.groups.CreateUser;
import com.eric.appointment.validator.groups.UpdateProvider;
import com.eric.appointment.validator.groups.UpdateUser;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("providers")
public class ProviderController {
    
    private final UserService userService;
    private final WorkService workService;
    private final WorkingPlanService workingPlanService;
    private final AppointmentService appointmentService;

    @GetMapping("/all")
    public String showAllProvider(Model model) {
        model.addAttribute("providers", userService.getAllProvider());
        return "user/listProvider";
    }

    @GetMapping("/new")
    public String registrationForm(Model model, @AuthenticationPrincipal UserDetail userDetail) {
        if (!model.containsAttribute("user")) model.addAttribute("user", new UserForm());
        model.addAttribute("account_type", "provider");
        model.addAttribute("registerAction", "/providers/new");
        model.addAttribute("allWorks", workService.getAllWorks());
        return "user/registerForm";
    }

    @PostMapping("/new")
    public String registerCustomer(@Validated({CreateProvider.class, CreateUser.class}) @ModelAttribute("user") UserForm userForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
        redirectAttributes.addFlashAttribute("user", userForm);
        return "redirect:/providers/new";
    }
    userService.saveNewProvider(userForm);
    return "redirect:/providers/all";
    }

    @GetMapping("/{id}")
    public String showCustomerDetail(@PathVariable("id") int id, Model model, @AuthenticationPrincipal UserDetail userDetail, @RequestParam(value = "activeTab", required = false) String activeTab) {
        if (userDetail.getId() != id && !userDetail.hasRole("ADMIN")) {
            throw new org.springframework.security.access.AccessDeniedException("Unauthorized");
        }

        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserForm(userService.getProviderById(id)));
        }
        if (!model.containsAttribute("changePassword")) {
            model.addAttribute("changePassword", new ChangePasswordForm(id));
        }

        model.addAttribute("account_type", "provider");
        model.addAttribute("formActionProfile", "/providers/update/profile");
        model.addAttribute("formActionPassword", "/providers/update/password");
        model.addAttribute("allWorks", workService.getAllWorks());
        model.addAttribute("numberOfScheduledAppointments", appointmentService.getNumberOfScheduledAppointments(id));
        model.addAttribute("numberOfCanceledAppointments", appointmentService.getNumberOfCanceledAppointments(id));
        return "user/updateForm";
    }

    @PostMapping("/update/password")
    public String updateProviderPassword(@Valid @ModelAttribute("changePassword") ChangePasswordForm changePasswordForm, BindingResult bindingResult, @AuthenticationPrincipal UserDetail userDetail, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.changePassword", bindingResult);
            redirectAttributes.addFlashAttribute("changePassword", changePasswordForm);
            return "redirect:/providers/" + userDetail.getId() + "?activeTab=changingPassword";
        }
        userService.updateProviderPassword(changePasswordForm);
        return "redirect:/providers/" + userDetail.getId() + "?activeTab=changingPassword";
    }

    @PostMapping("/update/profile")
    public String updateProviderProfile(@Validated({UpdateProvider.class, UpdateUser.class}) @ModelAttribute("user") UserForm userForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            redirectAttributes.addFlashAttribute("user", userForm);
            return "redirect:/providers/" + userForm.getId();
        }

        userService.updateProviderProfile(userForm);
        return "redirect:/providers/" + userForm.getId();
    }

    @PostMapping("/delete")
    public String deleteProvider(@RequestParam("providerId") int id) {
        userService.deleteUserById(id);
        return "redirect:/providers/all";
    }

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

    @PostMapping("/availability/breaks/delete")
    public String deleteProviderBreaks(@ModelAttribute("breakModel") TimePeriod timePeriod, @RequestParam("planId") int planId, @RequestParam("dayOfWeek") String dayOfWeek) {
        workingPlanService.deleteBreakToWorkingPlan(timePeriod, planId, dayOfWeek);
        return "redirect:/providers/availability";
    }
}
