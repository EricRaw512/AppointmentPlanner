package com.eric.appointment.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.eric.appointment.dao.WorkRepository;
import com.eric.appointment.entity.Work;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WorkService {
    
    private final WorkRepository workRepository;

    public List<Work> getAllWorks() {
        return workRepository.findAll();
    }

    public void deleteWorkById(int id) {
        workRepository.deleteById(id);
    }

    public List<Work> getWorksByProviderId(int id) {
        return workRepository.findByProviderId(id);
    }

    public Work getWorkById(int workId) {
        return workRepository.findById(workId).orElse(null);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void updateWork(Work work) {
        Work updatedWork = getWorkById(work.getId());
        updatedWork.setName(work.getName());
        updatedWork.setPrice(work.getPrice());
        updatedWork.setDescription(work.getDescription());
        updatedWork.setDuration(work.getDuration());
        updatedWork.setEditable(work.isEditable());
        workRepository.save(updatedWork);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void createNewWork(Work work) {
        workRepository.save(work);
    }

}
