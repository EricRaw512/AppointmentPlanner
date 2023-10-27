package com.eric.appointment.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eric.appointment.entity.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer>{

    List<Appointment> findByCustomer_Id(int customerId);

    List<Appointment> findByProvider_Id(int providerId);

    @Query("SELECT * FROM Appointments WHERE status='CANCELED' AND (id_customer = :userId or id_provider = :userId)")
    List<Appointment> findCanceledByUser(@Param("userId") int id);

    @Query("SELECT * FROM Appointments WHERE id_canceler = :userId")
    List<Appointment> findScheduledByUserId(@Param("userId") int id);

    @Query("SELECT * FROM Appointments WHERE id_customer = :userId AND id_canceler = :userId AND canceled_at >= :date")
    List<Appointment> findByCustomerIdCancceledAfterDate(@Param("userId") int userId, @Param("date") LocalDateTime atStartOfDay);
}
