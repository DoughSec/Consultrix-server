package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.Cohort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CohortRepository extends JpaRepository<Cohort, Long> {
    List<Cohort> findByStatus(String status);
    List<Cohort> findByFacilityId(int facilityId);
    List<Cohort> findByPrimaryInstructorId(int primaryInstructorUserId);
}
