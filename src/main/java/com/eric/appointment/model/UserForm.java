package com.eric.appointment.model;

import java.util.List;

import com.eric.appointment.entity.Work;
import com.eric.appointment.entity.user.Customer;
import com.eric.appointment.entity.user.Provider;
import com.eric.appointment.entity.user.User;
import com.eric.appointment.validator.FieldsValueMatch;
import com.eric.appointment.validator.UniqueUsername;
import com.eric.appointment.validator.groups.CreateUser;
import com.eric.appointment.validator.groups.UpdateProvider;
import com.eric.appointment.validator.groups.CreateProvider;
import com.eric.appointment.validator.groups.UpdateUser;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@FieldsValueMatch(groups = {CreateUser.class, UpdateUser.class}, field = "password", fieldMatch = "matchingPassword", message = "Password doesn't match")
public class UserForm {
    
    @NotNull(groups = UpdateUser.class)
    @Min(value = 1, groups = UpdateUser.class)
    private int id;

    @UniqueUsername(groups = CreateUser.class)
    @Size(groups = CreateUser.class, min = 5, max = 15, message = "Username should have 5-15 letters")
    @NotBlank(groups = CreateUser.class)
    private String userName;

    @Size(groups = {CreateUser.class, UpdateUser.class}, min = 5, max = 15, message = "Password should have 5-15 letters")
    @NotBlank(groups = {CreateUser.class, UpdateUser.class})
    private String password;

    @NotBlank(groups = {CreateUser.class, UpdateUser.class})
    private String matchingPassword;

    @NotBlank(message = "First name cannot be empty", groups = {CreateUser.class, UpdateUser.class})
    private String firstName;

    @NotBlank(message = "Last name cannot be empty", groups = {CreateUser.class, UpdateUser.class})
    private String lastName;

    @Email(message = "Email not valid", groups = {CreateUser.class, UpdateUser.class})
    @NotBlank(message = "Email cannot be empty", groups = {CreateUser.class, UpdateUser.class})
    private String email;

    @Pattern(groups = {CreateUser.class, UpdateUser.class}, regexp = "^(\\+62|62|0)8[1-9][0-9]{6,9}$", message = "Please enter valid mobile phone")
    @NotBlank(groups = {CreateUser.class, UpdateUser.class}, message = "Mobile phone cannot be empty")
    private String mobile;

    @Size(groups = {CreateUser.class, UpdateUser.class}, min = 5, max = 30, message = "Street should have 5 - 30 letters")
    @NotBlank(groups = {CreateUser.class, UpdateUser.class}, message = "Street cannot be empty")
    private String street;

    @Pattern(groups = {CreateUser.class, UpdateUser.class}, regexp = "^([1-9])[0-9]{4}$", message = "Please enter valid postcode")
    @NotBlank(groups = {CreateUser.class, UpdateUser.class}, message = "Post code cannot be empty")
    private String postcode;

    @NotBlank(groups = {CreateUser.class, UpdateUser.class},  message = "City cannot be empty")
    private String city;

    /*
     * Provider only;
     */
    @NotNull(groups = {CreateProvider.class, UpdateProvider.class})
    private List<Work> works;

    public UserForm(User user) {
        this.setId(user.getId());
        this.setUserName(user.getUserName());
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setEmail(user.getEmail());
        this.setCity(user.getCity());
        this.setStreet(user.getStreet());
        this.setPostcode(user.getPostcode());
        this.setMobile(user.getMobile());
    }

    public UserForm(Provider provider) {
        this((User) provider);
        this.setWorks(provider.getWorks());
    }

    public UserForm(Customer customer) {
        this((User) customer);
    }

}