package com.eric.appointment.dao.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eric.appointment.entity.user.User;

@Repository
public interface CommonUserRepository<T extends User> extends JpaRepository<T, Integer> {

    Optional<T> findByUserName(String username);

}