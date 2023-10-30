package com.eric.appointment.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eric.appointment.entity.Work;

@Repository
public interface WorkRepository extends JpaRepository<Work, Integer>{

    @Query("SELECT w FROM Work w inner join w.providers p WHERE p.id = :providerId")
    List<Work> findByProviderId(@Param("providerId") int id);
}
