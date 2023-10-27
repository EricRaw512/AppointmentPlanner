package com.eric.appointment.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eric.appointment.dao.user.CustomerRepository;
import com.eric.appointment.dao.user.UserRepository;
import com.eric.appointment.entity.user.Customers;
import com.eric.appointment.entity.user.Role;
import com.eric.appointment.entity.user.User;
import com.eric.appointment.model.ChangePasswordForm;
import com.eric.appointment.model.UserForm;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserById(int id) {
        return userRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));}

    public void saveNewCustomer(@Valid UserForm userForm) {
        Customers customer = new Customers(userForm, passwordEncoder.encode(userForm.getPassword()), Role.CUSTOMER);
        customerRepository.save(customer);
    }

    public boolean userExist(String value) {
        return userRepository.findByUserName(value).orElse(null) != null;
    }

    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    public Customers getCustomerById(int id) {
        return customerRepository.findById(id).orElseThrow
            (() -> new UsernameNotFoundException("Customer Not Found"));
    }

    @PreAuthorize("#changePasswordForm.id == principal.id")
    public void updateUserPassword(@Valid ChangePasswordForm changePasswordForm) {
        User user = userRepository.findById(changePasswordForm.getId())
                .orElse(null);
        user.setPassword(passwordEncoder.encode(changePasswordForm.getNewPassword()));
        userRepository.save(user);
    }
}
