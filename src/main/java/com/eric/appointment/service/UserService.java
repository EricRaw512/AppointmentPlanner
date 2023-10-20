package com.eric.appointment.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.eric.appointment.dao.user.UserRepository;
import com.eric.appointment.entity.user.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));}
}
