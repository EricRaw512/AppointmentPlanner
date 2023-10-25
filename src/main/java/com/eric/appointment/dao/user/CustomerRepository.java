package com.eric.appointment.dao.user;

import org.springframework.stereotype.Repository;

import com.eric.appointment.entity.user.Customers;

@Repository
public interface CustomerRepository extends CommonUserRepository<Customers>{
    
}
