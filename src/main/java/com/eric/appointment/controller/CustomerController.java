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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eric.appointment.model.ChangePasswordForm;
import com.eric.appointment.model.UserForm;
import com.eric.appointment.security.UserDetail;
import com.eric.appointment.service.AppointmentService;
import com.eric.appointment.service.UserService;
import com.eric.appointment.validator.groups.CreateUser;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
@RequestMapping("customers")
public class CustomerController {

    private final UserService userService;
    private final AppointmentService appointmentService;
    
    @GetMapping("/new/{customer_type}")
    public String registrationForm(@PathVariable("customer_type") String customerType, Model model, @AuthenticationPrincipal UserDetail userDetail) {
        if (userDetail != null) {
            return "redirect:/";
        }
        
        if (customerType.equals("customer")) {
            model.addAttribute("account_type", "customer");
            model.addAttribute("registerAction", "/customers/new/customer");
        } else {
            throw new RuntimeException();
        }
 
        model.addAttribute("user", new UserForm());
        return "user/registerForm";
    }

    @PostMapping("/new/customer")
    public String registerCustomer(@Validated(CreateUser.class) @ModelAttribute("user") UserForm userForm, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
        model.addAttribute("user", userForm);
        model.addAttribute("account_type", "customer");
        model.addAttribute("registerAction", "/customers/new/customer");
        model.addAttribute("registrationError", null);
        return "user/registerForm";
    }
    userService.saveNewCustomer(userForm);
    model.addAttribute("createdUserName", userForm.getUserName());
    return "user/login";
    }

    @GetMapping("/{id}")
    public String showCustomerDetail(@PathVariable("id") int id, Model model, @AuthenticationPrincipal UserDetail userDetail) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserForm(userService.getUserById(id)));
            model.addAttribute("account_type", "customer");
            model.addAttribute("formActionProfile", "/customers/update/profile");
        }
        if (!model.containsAttribute("changePassword")) {
            model.addAttribute("changePassword", new ChangePasswordForm(id));
        }

        model.addAttribute("formActionPassword", "/customers/update/password");
        model.addAttribute("numberOfScheduledAppointments", appointmentService.getNumberOfScheduledAppointments(id));
        model.addAttribute("numberOfCanceledAppointments", appointmentService.getNumberOfCanceledAppointments(id));
        return "user/updateForm";
    }

    @PostMapping("/update/password")
    public String updateCustomer(@Valid @ModelAttribute("passwordChange") ChangePasswordForm changePasswordForm, BindingResult bindingResult, @AuthenticationPrincipal UserDetail userDetail, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passwordChange", bindingResult);
            redirectAttributes.addFlashAttribute("changePassword", changePasswordForm);
            return "redirect:/customers/" + userDetail.getId();
        }
        userService.updateUserPassword(changePasswordForm);
        return "redirect:/customers/" + userDetail.getId();
    }
}
