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

import com.eric.appointment.model.ChangePasswordForm;
import com.eric.appointment.model.UserForm;
import com.eric.appointment.security.UserDetail;
import com.eric.appointment.service.AppointmentService;
import com.eric.appointment.service.UserService;
import com.eric.appointment.validator.groups.CreateUser;
import com.eric.appointment.validator.groups.UpdateUser;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
@RequestMapping("customers")
public class CustomerController {

    private final UserService userService;
    private final AppointmentService appointmentService;

    @GetMapping("/all")
    public String showAllCustomers(Model model) {
        model.addAttribute("customers", userService.getAllCustomers());
        return "users/listCustomers";
    }
    
    @GetMapping("/new/customer")
    public String registrationForm(Model model, @AuthenticationPrincipal UserDetail userDetail) {
        if (userDetail != null) {
            return "redirect:/";
        }

        model.addAttribute("account_type", "customer");
        model.addAttribute("registerAction", "/customers/new/customer");
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
    public String showCustomerDetail(@PathVariable("id") int id, Model model, @AuthenticationPrincipal UserDetail userDetail, @RequestParam(value = "activeTab", required = false) String activeTab) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserForm(userService.getUserById(id)));
        }
        
        if (!model.containsAttribute("changePassword")) {
            model.addAttribute("changePassword", new ChangePasswordForm(id));
        }

        model.addAttribute("account_type", "customer");
        model.addAttribute("formActionProfile", "/customers/update/profile");
        model.addAttribute("formActionPassword", "/customers/update/password");
        model.addAttribute("numberOfScheduledAppointments", appointmentService.getNumberOfScheduledAppointments(id));
        model.addAttribute("numberOfCanceledAppointments", appointmentService.getNumberOfCanceledAppointments(id));
        return "user/updateForm";
    }

    @PostMapping("/update/password")
    public String updateUserPassword(@Valid @ModelAttribute("changePassword") ChangePasswordForm changePasswordForm, BindingResult bindingResult, @AuthenticationPrincipal UserDetail userDetail, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.changePassword", bindingResult);
            redirectAttributes.addFlashAttribute("changePassword", changePasswordForm);
            return "redirect:/customers/" + userDetail.getId() + "?activeTab=changingPassword";
        }
        
        userService.updateUserPassword(changePasswordForm);
        return "redirect:/customers/" + userDetail.getId() + "?activeTab=changingPassword";
    }

    @PostMapping("/update/profile")
    public String updateUserProfile(@Validated(UpdateUser.class) @ModelAttribute("user") UserForm userForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            redirectAttributes.addFlashAttribute("user", userForm);
            return "redirect:/customers/" + userForm.getId();
        }

        userService.updateUserProfile(userForm);
        return "redirect:/customers/" + userForm.getId();
    }

    @PostMapping("/delete")
    public String deleteCustomer(@RequestParam("customerId") int id) {
        userService.deleteUserById(id);
        return "redirect:/customers/all";
    }
}
