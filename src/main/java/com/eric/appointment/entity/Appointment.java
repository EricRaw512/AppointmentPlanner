package com.eric.appointment.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.eric.appointment.entity.user.Customer;
import com.eric.appointment.entity.user.Provider;
import com.eric.appointment.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @ManyToOne
    @JoinColumn(name = "id_canceler")
    @JsonIgnore
    private User canceler;

    @ManyToOne
    @JoinColumn(name = "id_customer")
    @JsonIgnore
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "id_provider")
    @JsonIgnore
    private Provider provider;

    @ManyToOne
    @JoinColumn(name = "id_work")
    @JsonIgnore
    private Work work;

    @OneToMany(mappedBy = "appointment")
    @JsonIgnore
    private List<ChatMessage> chatMessages;

    @Override
    public int compareTo(Appointment o) {
        return this.getStart().compareTo(o.getStart());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
