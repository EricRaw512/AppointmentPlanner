package com.eric.appointment.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Setter;

@Setter
@MappedSuperclass
public class BaseEntity {
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    public Integer getId() {
        return id;
    }
}
