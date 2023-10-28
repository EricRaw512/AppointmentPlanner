package com.eric.appointment.dao.user;

import org.springframework.stereotype.Repository;

import com.eric.appointment.entity.user.Customer;

@Repository
public interface CustomerRepository extends CommonUserRepository<Customer>{
    
}
