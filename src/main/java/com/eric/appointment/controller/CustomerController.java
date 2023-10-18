package com.eric.appointment.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eric.appointment.security.UserDetail;


@Controller
@RequestMapping("customers")
public class CustomerController {
    
    @GetMapping("/new/{customer_type}")
    public String registrationForm(@PathVariable("customer_type") String customerType, Model model, @AuthenticationPrincipal UserDetail userDetail) {
        if (userDetail != null) {
            return "redirect:/";
        }

        if (customerType.equals("corporate")) {
            model.addAttribute("account_type", "cus_corporate");
            model.addAttribute("registerAction", "/customers/new/corporate");
        } else if (customerType.equals("retail")) {
            model.addAttribute("account_type", "cus_retail");
            model.addAttribute("registerAction", "/customers/new/retail");
        } else {
            throw new RuntimeException();
        }

        // model.addAttribute("user", new UserForm());
        return "user/registerForm";
    }   
}
