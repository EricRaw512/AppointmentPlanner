package com.eric.appointment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eric.appointment.dao.WorkRepository;
import com.eric.appointment.entity.Work;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WorkService {
    
    private final WorkRepository WorkRepository;

    public List<Work> getAllWorks() {
        return WorkRepository.findAll();
    }

}
