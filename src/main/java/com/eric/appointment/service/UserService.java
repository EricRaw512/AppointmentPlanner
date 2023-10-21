package com.eric.appointment.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eric.appointment.dao.user.UserRepository;
import com.eric.appointment.entity.user.User;
import com.eric.appointment.model.UserForm;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;

    public User getUserById(int id) {
        return userRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));}

    public void saveNewCustomer(@Valid UserForm userForm) {
    }
}
