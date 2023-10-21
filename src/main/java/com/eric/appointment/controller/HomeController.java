package com.eric.appointment.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.eric.appointment.security.UserDetail;
import com.eric.appointment.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetail userDetail) {
        model.addAttribute("user", userService.getUserById(userDetail.getId()));
        return "home";
    }

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal UserDetail userDetail) {
        if (userDetail != null) {
            return "redirect:/";
        }

        return "user/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }    
}
