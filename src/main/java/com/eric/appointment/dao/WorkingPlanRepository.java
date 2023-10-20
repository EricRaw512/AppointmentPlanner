package com.eric.appointment.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eric.appointment.entity.WorkingPlan;

public interface WorkingPlanRepository extends JpaRepository<WorkingPlan, Integer>{
    
}
