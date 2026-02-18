package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    List<Facility> findByStatus(String status);
    List<Facility> findByOrganizationId(int organizationId);
}
