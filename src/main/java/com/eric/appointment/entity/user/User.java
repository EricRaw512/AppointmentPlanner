package com.eric.appointment.entity.user;

import com.eric.appointment.entity.BaseEntity;
import com.eric.appointment.model.UserForm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseEntity{

    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "postcode")
    private String postcode;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User(UserForm userForm, String encryptedPassword, Role role) {
        this.userName = userForm.getUserName();
        this.firstName = userForm.getFirstName();
        this.lastName = userForm.getLastName();
        this.email = userForm.getEmail();
        this.mobile = userForm.getMobile();
        this.street = userForm.getStreet();
        this.city = userForm.getCity();
        this.postcode = userForm.getPostcode();
        this.password = encryptedPassword;
        this.role = role;
    }  
    
    public void update(UserForm userForm) {
        this.setFirstName(userForm.getFirstName());
        this.setLastName(userForm.getLastName());
        this.setEmail(userForm.getEmail());
        this.setMobile(userForm.getMobile());
        this.setStreet(userForm.getStreet());
        this.setCity(userForm.getCity());
        this.setPostcode(userForm.getPostcode());
    }
}
