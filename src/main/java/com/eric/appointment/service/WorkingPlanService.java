package com.eric.appointment.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.eric.appointment.dao.WorkingPlanRepository;
import com.eric.appointment.entity.WorkingPlan;
import com.eric.appointment.model.TimePeriod;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkingPlanService {
    
    private final WorkingPlanRepository workingPlanRepository;

    @PreAuthorize("#id == principal.id")
    public WorkingPlan getWorkingPlanByProviderId(int id) {
        return workingPlanRepository.findById(id).orElse(null);
    }

    @PreAuthorize("#updatePlan.provider.id == principal.id")
    public void updateWorkingPlan(WorkingPlan updatePlan) {
        WorkingPlan workingPlan = workingPlanRepository.findById(updatePlan.getId()).orElse(null);
        workingPlan.updateWorkingPlan(workingPlan);
        workingPlanRepository.save(workingPlan);
    }

    public void addBreakToWorkingPlan(TimePeriod timePeriod, int planId, String dayOfWeek) {
    }

    public void deleteBreakToWorkingPlan(TimePeriod timePeriod, int planId, String dayOfWeek) {
    }

}
