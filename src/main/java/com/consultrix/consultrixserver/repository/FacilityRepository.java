package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
    List<Facility> findByStatus(String status);
    List<Facility> findByOrganizationId(int organizationId);
}
