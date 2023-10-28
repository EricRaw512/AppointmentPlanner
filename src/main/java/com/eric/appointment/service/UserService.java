package com.eric.appointment.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eric.appointment.dao.user.CustomerRepository;
import com.eric.appointment.dao.user.ProviderRepository;
import com.eric.appointment.dao.user.UserRepository;
import com.eric.appointment.entity.WorkingPlan;
import com.eric.appointment.entity.user.Customer;
import com.eric.appointment.entity.user.Provider;
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
    private final ProviderRepository providerRepository;

    public User getUserById(int id) {
        return userRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));}

    public void saveNewCustomer(@Valid UserForm userForm) {
        Customer customer = new Customer(userForm, passwordEncoder.encode(userForm.getPassword()), Role.CUSTOMER);
        customerRepository.save(customer);
    }

    public boolean userExist(String value) {
        return userRepository.findByUserName(value).orElse(null) != null;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    public Customer getCustomerById(int id) {
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

    @PreAuthorize("#userForm.id == principal.id or hasRole('ADMIN')")
    public void updateUserProfile(UserForm userForm) {
        Customer customer = customerRepository.findById(userForm.getId()).orElse(null);
        customer.update(userForm);
        customerRepository.save(customer);
    }

    public void saveNewProvider(UserForm userForm) {
        WorkingPlan workingPlan = WorkingPlan.generateDefaultWorkingPlan();
        Provider provider = new Provider(userForm, passwordEncoder.encode(userForm.getPassword()), Role.PROVIDER, workingPlan);
        providerRepository.save(provider);
    }

    public List<Provider> getAllProvider() {
        return providerRepository.findAll();
    }

    public Provider getProviderById(int id) {
        return providerRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }

    @PreAuthorize("#changePasswordForm.id == principal.id")
    public void updateProviderPassword(@Valid ChangePasswordForm changePasswordForm) {
        User user = userRepository.findById(changePasswordForm.getId()).orElse(null);
        user.setPassword(passwordEncoder.encode(changePasswordForm.getNewPassword()));
        userRepository.save(user);
    }

    @PreAuthorize("#userForm.id == principal.id or hasRole('ADMIN')")
    public void updateProviderProfile(UserForm userForm) {
        Provider provider = providerRepository.findById(userForm.getId()).orElse(null);
        provider.update(userForm);
        providerRepository.save(provider);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }
}
