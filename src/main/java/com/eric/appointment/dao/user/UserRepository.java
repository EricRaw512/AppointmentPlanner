package com.eric.appointment.dao.user;

import org.springframework.stereotype.Repository;

import com.eric.appointment.entity.user.User;


@Repository
public interface UserRepository extends CommonUserRepository<User>{
    
}
