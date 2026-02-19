package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.*;
import com.consultrix.consultrixserver.model.Cohort;
import com.consultrix.consultrixserver.repository.CohortRepository;
import com.consultrix.consultrixserver.repository.FacilityRepository;
import com.consultrix.consultrixserver.repository.InstructorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class CohortService {

    private final CohortRepository cohortRepository;
    private final FacilityRepository facilityRepository;
    private final InstructorRepository instructorRepository;

    public CohortService(
            CohortRepository cohortRepository,
            FacilityRepository facilityRepository,
            InstructorRepository instructorRepository
    ) {
        this.cohortRepository = cohortRepository;
        this.facilityRepository = facilityRepository;
        this.instructorRepository = instructorRepository;
    }

    // create Cohort
    public Cohort create(
            Integer facilityId, Integer primaryInstructorUserId, String name,
            LocalDate startDate, LocalDate endDate, Integer capacity,
            String status
            ) {
        if (facilityId == null || primaryInstructorUserId == null) {
            throw new IllegalArgumentException("facilityId and instructorId are required");
        }

        // Prevent duplicates for same instructor
        List<Cohort> existing = cohortRepository.findByPrimaryInstructorId(primaryInstructorUserId);
        if (existing != null) {
            throw new IllegalArgumentException("Instructor already exists for this cohort");
        }

        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new IllegalArgumentException("Facility not found: " + facilityId));

        Instructor primaryInstructor = instructorRepository.findById(primaryInstructorUserId)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found: " + primaryInstructorUserId));

        Cohort Cohort = new Cohort();
        Cohort.setFacility(facility);
        Cohort.setPrimaryInstructor(primaryInstructor);
        Cohort.setName(name);
        Cohort.setStartDate(startDate);
        Cohort.setEndDate(endDate);
        Cohort.setCapacity(capacity);
        Cohort.setStatus(status);

        return cohortRepository.save(Cohort);
    }

    // getAll
    @Transactional(readOnly = true)
    public List<Cohort> listAll() {
        return cohortRepository.findAll();
    }

    // getByFacility
    @Transactional(readOnly = true)
    public List<Cohort> listByFacility(Integer facilityId) {
        if (facilityId == null) {
            throw new IllegalArgumentException("facilityId is required");
        }
        return cohortRepository.findByFacilityId(facilityId);
    }

    // getByStatus
    @Transactional(readOnly = true)
    public List<Cohort> listByStatus(String status) {
        if (status == null) {
            throw new IllegalArgumentException("status is required");
        }
        return cohortRepository.findByStatus(status);
    }

    // getById
    @Transactional(readOnly = true)
    public Cohort getById(Integer cohortId) {
        if (cohortId == null) {
            throw new IllegalArgumentException("CohortId is required");
        }
        return cohortRepository.findById(cohortId)
                .orElseThrow(() -> new IllegalArgumentException("Cohort not found: " + cohortId));
    }

    // update Cohort
    public Cohort update(Integer cohortId, Cohort updated) {
        Cohort existing = getById(cohortId);

        existing.setName(updated.getName());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        existing.setCapacity(updated.getCapacity());
        existing.setStatus(updated.getStatus());

        return cohortRepository.save(existing);
    }

    // delete Cohort
    public void delete(Integer cohortId) {
        if (cohortId == null) {
            throw new IllegalArgumentException("CohortId is required");
        }
        if (!cohortRepository.existsById(cohortId)) {
            throw new IllegalArgumentException("Cohort not found: " + cohortId);
        }
        cohortRepository.deleteById(cohortId);
    }
}