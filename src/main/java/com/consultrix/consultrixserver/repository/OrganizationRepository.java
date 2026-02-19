package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
    Optional<Organization> findByNameIgnoreCase(String name);
}
