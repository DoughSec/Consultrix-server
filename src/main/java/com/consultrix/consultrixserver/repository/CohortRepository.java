package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.Cohort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CohortRepository extends JpaRepository<Cohort, Long> {
    List<Cohort> findByStatus(String status);
    List<Cohort> findByFacilityId(int facilityId);
    List<Cohort> findByPrimaryInstructorId(int primaryInstructorUserId);


}
