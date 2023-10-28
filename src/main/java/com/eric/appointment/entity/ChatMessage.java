package com.eric.appointment.entity;

import java.time.LocalDate;

import com.eric.appointment.entity.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "messsages")
public class ChatMessage extends BaseEntity implements Comparable<ChatMessage>{
    
    @Column(name = "create_at")
    private LocalDate createAt;

    @Column(name = "message")
    private String message;

    @ManyToOne
    @JoinColumn(name = "id_sender")
    private User Sender;

    @ManyToOne
    @JoinColumn(name = "id_appointment")
    private Appointment appointment;

    @Override
    public int compareTo(ChatMessage o) {
        return this.getCreateAt().compareTo(o.getCreateAt());
    }
}
