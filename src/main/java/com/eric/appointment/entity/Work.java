package com.eric.appointment.entity;

import java.util.List;

import com.eric.appointment.entity.user.Provider;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @ManyToMany(mappedBy = "works")
    private List<Provider> providers;

    @Override
    public String toString() {
        return super.toString();
    }
}
