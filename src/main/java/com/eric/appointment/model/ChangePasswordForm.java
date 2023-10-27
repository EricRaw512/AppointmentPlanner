package com.eric.appointment.model;

import com.eric.appointment.validator.CurrentPasswordMatch;
import com.eric.appointment.validator.FieldsValueMatch;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@FieldsValueMatch(field = "newPassword", fieldMatch = "matchingPassword", message = "Password doesn't match")
@CurrentPasswordMatch(message = "Current Password is Wrong", field = "currentPassword")
public class ChangePasswordForm {
 
    @NotNull
    private int id;

    @NotBlank()
    private String currentPassword;

    @Size(min = 5, max = 10, message = "Password should have 5-15 letters")
    @NotBlank()
    private String newPassword;

    @Size(min = 5, max = 10, message = "Password should have 5-15 letters")
    @NotBlank()
    private String matchingPassword;

    public ChangePasswordForm(int id) {
        this.setId(id);
    }
}
