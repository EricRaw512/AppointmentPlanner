package com.eric.appointment.entity;

import org.hibernate.annotations.Type;

import com.eric.appointment.entity.user.Provider;
import com.eric.appointment.model.DayPlan;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "working_plan")
public class WorkingPlan {
    
    @Id
    @Column(name = "id_provider")
    private long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id_provider")
    private Provider provider;

    // @Type()
    @Column(name = "monday")
    private DayPlan monday;

}
