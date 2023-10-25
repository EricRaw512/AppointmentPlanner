package com.eric.appointment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eric.appointment.entity.Work;

@Repository
public interface WoksRepository extends JpaRepository<Work, Integer>{
    
}
