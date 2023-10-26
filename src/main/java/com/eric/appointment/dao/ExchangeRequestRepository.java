package com.eric.appointment.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eric.appointment.entity.ExchangeRequest;

public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Integer>{
    
}
