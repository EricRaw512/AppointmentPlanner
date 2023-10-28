package com.eric.appointment.entity;

import java.util.List;

import com.eric.appointment.entity.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name = "works")
public class Work extends BaseEntity{
    
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private long price;

    @Column(name = "duration")
    private int duration;

    @Column(name =  "editable")
    private boolean editable;

    @ManyToMany
    @JoinTable(
        name = "works_providers", 
        joinColumns = @JoinColumn(name = "id_work"), 
        inverseJoinColumns = @JoinColumn(name = "id_user")
    )
    private List<User> providers;
}
