package com.eric.appointment.entity;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.eric.appointment.entity.user.Customers;
import com.eric.appointment.entity.user.Provider;
import com.eric.appointment.entity.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "appointments")
@Entity
public class Appointment extends BaseEntity implements Comparable<Appointment>{
    
    @Column(name = "start")
    @DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm")
    private LocalDateTime start;

    @Column(name = "end")
    @DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm")
    private LocalDateTime end;

    @Column(name = "canceled_at")
    @DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm")
    private LocalDateTime canceledAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @OneToOne
    @JoinColumn(name = "id_canceler")
    private User canceller;

    @ManyToOne
    @JoinColumn(name = "id_customer")
    private Customers customer;

    @ManyToOne
    @JoinColumn(name = "id_provider")
    private Provider provider;

    @ManyToOne
    @JoinColumn(name = "id_work")
    private Work work;

    @Override
    public int compareTo(Appointment o) {
        return this.getStart().compareTo(o.getStart());
    }
}
