package com.eric.appointment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{workId}")
    public String showWorkForm(@PathVariable("workId") int workId, Model model) {
        model.addAttribute("work", workService.getWorkById(workId));
        return "work/workForm";
    }

    @GetMapping("/new")
    public String addNewWork(Model model) {
        model.addAttribute("work", new Work());
        return "work/workForm";
    }

    @PostMapping("/new")
    public String saveNewWork(@ModelAttribute("work") Work work) {
        if (workService.getWorkById(work.getId()) != null) {
            workService.updateWork(work);
        } else {
            workService.createNewWork(work);
        }

        return "redirect:/works/all";
    }


    @PostMapping("/delete")
    public String deleteWork(@RequestParam("workId") int id) {
        workService.deleteWorkById(id);
        return "redirect:/works/all";
    }
}
