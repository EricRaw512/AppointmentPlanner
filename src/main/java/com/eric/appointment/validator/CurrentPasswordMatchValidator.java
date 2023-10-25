package com.eric.appointment.validator;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.eric.appointment.entity.user.User;
import com.eric.appointment.model.ChangePasswordForm;
import com.eric.appointment.service.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CurrentPasswordMatchValidator implements ConstraintValidator<CurrentPasswordMatch, Object>{

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        ChangePasswordForm form = (ChangePasswordForm) value;
        User user = userService.getUserById(form.getId());
        return passwordEncoder.matches(form.getCurrentPassword(), user.getPassword());
    }

}
