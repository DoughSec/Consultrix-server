package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.Cohort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CohortRepository extends JpaRepository<Cohort, Integer> {
    List<Cohort> findByStatus(String status);
    List<Cohort> findByFacilityId(Integer facilityId);
    List<Cohort> findByPrimaryInstructorId(Integer primaryInstructorUserId);
}
