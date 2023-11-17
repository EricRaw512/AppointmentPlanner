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
   
    @Query("SELECT a FROM Appointment a WHERE a.canceler.id = :userId")
    List<Appointment> findCanceledByUser(@Param("userId") int id);
    
    @Query("SELECT a FROM Appointment a WHERE a.status = 'SCHEDULED' AND (a.customer.id = :userId or a.provider.id = :userId)")
    List<Appointment> findScheduledByUserId(@Param("userId") int id);

    @Query("SELECT a FROM Appointment a WHERE a.customer.id = :userId AND a.canceler.id = :userId AND a.canceledAt >= :date")
    List<Appointment> findByCustomerIdCanceledAfterDate(@Param("userId") int userId, @Param("date") LocalDateTime atStartOfDay);

    @Query("SELECT a FROM Appointment a WHERE a.provider.id = :providerId AND a.start >= :dayStart AND a.start <= :dayEnd")
    List<Appointment> findByProviderIdWithStartInPeriod(@Param("providerId") int providerId, @Param("dayStart") LocalDateTime atStartOfDay,
            @Param("dayEnd") LocalDateTime plusDays);

    @Query("SELECT a FROM Appointment a WHERE a.customer.id = :customerId AND a.start >= :dayStart AND a.start <= :dayEnd")
    List<Appointment> findByCustomerIdWithStartInPeriod(@Param("customerId") int customerId, @Param("dayStart") LocalDateTime atStartOfDay,
            @Param("dayEnd") LocalDateTime plusDays);
            
    @Query("select a from Appointment a inner join a.work w where a.status = 'SCHEDULED' and a.customer.id <> :customerId and a.provider.id= :providerId and a.start >= :start and w.id = :workId")
    List<Appointment> getEligibleAppointmentsForExchange(@Param("start") LocalDateTime start, @Param("customerId") Integer customerId, 
            @Param("providerId") Integer providerId, @Param("workId") Integer workId);

}
