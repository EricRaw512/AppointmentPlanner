package com.eric.appointment.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eric.appointment.entity.Work;

public interface WoksRepository extends JpaRepository<Work, Integer>{
    
}
