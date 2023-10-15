package com.eric.appointment.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eric.appointment.entity.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    
    Optional<User> findByEmail(String email);
}
