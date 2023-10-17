package com.eric.appointment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eric.appointment.security.UserDetail;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserDetail, Integer>{
    
    Optional<UserDetail> findByUserName(String username);
}
