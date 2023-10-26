package com.eric.appointment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ExchangeRequest extends BaseEntity{
    
    @Enumerated(EnumType.STRING)
    @Column(name = "exchange_status")
    private ExchangeRequestStatus exchangeStatus;

    @OneToOne
    @Column(name = "requestor")
    private Appointment requestor;

    @OneToOne
    @Column(name = "requested")
    private Appointment requested;
}
