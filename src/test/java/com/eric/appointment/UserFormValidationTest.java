package com.eric.appointment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.eric.appointment.model.UserForm;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@SpringBootTest
public class UserFormValidationTest {
    private final Validator validator = new LocalValidatorFactoryBean();

    @Test
    public void testValidUserForm() {
        UserForm userForm = new UserForm();
        userForm.setId(1);
        userForm.setUserName("ValidUser");
        userForm.setPassword("ValidPass");
        userForm.setMatchingPassword("ValidPass");
        userForm.setFirstName("John");
        userForm.setLastName("Doe");
        userForm.setEmail("john@example.com");
        userForm.setMobile("081234567890");
        userForm.setStreet("123 Main St");
        userForm.setPostcode("12345");
        userForm.setCity("Example City");

        Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm);
        assertEquals(0, violations.size());
    }

    @Test
    public void testInvalidUserForm() {
         UserForm userForm = new UserForm();

        Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm);
        assertEquals(9, violations.size());
    }
}
