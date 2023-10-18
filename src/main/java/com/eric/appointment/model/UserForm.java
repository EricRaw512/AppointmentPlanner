package com.eric.appointment.model;

import com.eric.appointment.entity.user.User;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserForm {
    
    @NotNull
    @Min(value = 1)
    private long id;

    @Size(min = 5, max = 15, message = "Username should have 5-15 letters")
    @NotBlank
    private String userName;

    @Size(min = 5, max = 15, message = "Password should have 5-15 letters")
    @NotBlank
    private String password;

    @Size(min = 5, max = 15, message = "Password should have 5-15 letters")
    @NotBlank
    @AssertTrue(message = "Passwords must match")
    private String matchingPassword;

    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    private String lastName;

    @Email(message = "Email not valid")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @Pattern(regexp = "^(\\+62|62|0)8[1-9][0-9]{6,9}$", message = "Please enter valid mobile phone")
    @NotBlank(message = "Mobile phone cannot be empty")
    private String mobile;

    @Size(min = 5, max = 30, message = "Street should have 5 - 30 letters")
    @NotBlank(message = "Street cannot be empty")
    private String street;

    @Pattern(regexp = "^([1-9])[0-9]{4}$", message = "Please enter valid postcode")
    @NotBlank(message = "Post code cannot be empty")
    private String postcode;

    @NotBlank( message = "City cannot be empty")
    private String city;


    public boolean arePasswordsMatching(String password, String matchingPassword) {
        return password.equals(matchingPassword);
    }

    /*
    CorporateCustomer only:
    */
    @NotBlank(message = "Company cannot be empty")
    private String companyName;

    @Pattern(regexp = "[0-9]{15}", message = "Please enter valid NPWP")
    @NotBlank(message = "NPWP cannot be empty")
    private String NPWP;

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

    public UserForm(RetailCustomer retailCustomer) {
        this((User) retailCustomer);
    }

    public UserForm(CorporateCustomer corporateCustomer) {
        this((User) corporateCustomer);
        this.setCompanyName(corporateCustomer.getCompanyName());
        this.setVatNumber(corporateCustomer.getVatNumber());
    }
}