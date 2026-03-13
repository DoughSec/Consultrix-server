package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.*;
import com.consultrix.consultrixserver.model.Cohort;
import com.consultrix.consultrixserver.model.dto.cohortDTO.CohortRequestDto;
import com.consultrix.consultrixserver.model.dto.cohortDTO.CohortResponseDto;
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
    public CohortResponseDto create(
            Integer facilityId, Integer primaryInstructorUserId, String name,
            LocalDate startDate, LocalDate endDate, Integer capacity,
            String status
            ) {
        if (facilityId == null || primaryInstructorUserId == null) {
            throw new IllegalArgumentException("facilityId and instructorId are required");
        }

        // Prevent duplicates for same instructor
        List<Cohort> existing = cohortRepository.findByPrimaryInstructorId(primaryInstructorUserId);
//        if (existing != null) {
//            throw new IllegalArgumentException("Instructor already exists for this cohort");
//        }

        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new IllegalArgumentException("Facility not found: " + facilityId));

        Instructor primaryInstructor = instructorRepository.findById(primaryInstructorUserId)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found: " + primaryInstructorUserId));

        Cohort cohort = new Cohort();
        cohort.setFacility(facility);
        cohort.setPrimaryInstructor(primaryInstructor);
        cohort.setName(name);
        cohort.setStartDate(startDate);
        cohort.setEndDate(endDate);
        cohort.setCapacity(capacity);
        cohort.setStatus(status);

        cohortRepository.save(cohort);

        CohortResponseDto cohortResponseDto = new CohortResponseDto();
        cohortResponseDto.setCohortId(cohort.getId());
        cohortResponseDto.setFacilityId(cohort.getFacility().getId());
        cohortResponseDto.setPrimaryInstructorUserId(cohort.getPrimaryInstructor().getId());
        cohortResponseDto.setName(cohort.getName());
        cohortResponseDto.setStartDate(cohort.getStartDate());
        cohortResponseDto.setEndDate(cohort.getEndDate());
        cohortResponseDto.setCapacity(cohort.getCapacity());
        cohortResponseDto.setStatus(cohort.getStatus());

        return cohortResponseDto;
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
    public CohortResponseDto update(Integer cohortId, CohortRequestDto updated) {
        Cohort existing = getById(cohortId);

        existing.setName(updated.getName());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        existing.setCapacity(updated.getCapacity());
        existing.setStatus(updated.getStatus());

        cohortRepository.save(existing);

        CohortResponseDto cohortResponseDto = new CohortResponseDto();
        cohortResponseDto.setCohortId(existing.getId());
        cohortResponseDto.setFacilityId(existing.getFacility().getId());
        cohortResponseDto.setName(existing.getName());
        cohortResponseDto.setPrimaryInstructorUserId(existing.getPrimaryInstructor().getId());
        cohortResponseDto.setName(existing.getName());
        cohortResponseDto.setStartDate(existing.getStartDate());
        cohortResponseDto.setEndDate(existing.getEndDate());
        cohortResponseDto.setCapacity(existing.getCapacity());
        cohortResponseDto.setStatus(existing.getStatus());

        return cohortResponseDto;
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