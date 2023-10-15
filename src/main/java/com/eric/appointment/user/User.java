package com.eric.appointment.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    
    @Id
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
