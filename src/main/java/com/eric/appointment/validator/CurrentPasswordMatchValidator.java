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
    private String field;

    @Override
    public void initialize(CurrentPasswordMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        ChangePasswordForm form = (ChangePasswordForm) value;
        User user = userService.getUserById(form.getId());
        boolean isValid = passwordEncoder.matches(form.getCurrentPassword(), user.getPassword());
        
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()).addPropertyNode(field).addConstraintViolation();
        }
        
        return isValid;
    }

}
