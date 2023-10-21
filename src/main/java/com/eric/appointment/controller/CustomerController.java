package com.eric.appointment.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eric.appointment.model.UserForm;
import com.eric.appointment.security.UserDetail;
import com.eric.appointment.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
@RequestMapping("customers")
public class CustomerController {

    private final UserService userService;
    
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
    public String registerCustomer(@Valid @ModelAttribute("user") UserForm userForm, BindingResult bindingResult, Model model) {
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
}
