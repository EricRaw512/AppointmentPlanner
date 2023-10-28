package com.eric.appointment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eric.appointment.entity.Work;
import com.eric.appointment.service.WorkService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("works")
public class WorkControler {
    
    private final WorkService workService;

    @GetMapping("/all")
    public String showAllWorks(Model model) {
        model.addAttribute("works", workService.getAllWorks());
        return "work/workList";
    }

    @GetMapping("/new")
    public String addNewWork(Model model) {
        model.addAttribute("work", new Work());
        return "work/workForm";
    }

    @PostMapping("/delete")
    public String deleteWork(@RequestParam("workId") int id) {
        workService.deleteWorkById(id);
        return "redirect:/works/all";
    }
}
