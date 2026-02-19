package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Organization;
import com.consultrix.consultrixserver.repository.OrganizationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    //create
    public Organization create(Organization org) {
        if(organizationRepository.findByNameIgnoreCase(org.getName())) {
            throw new IllegalArgumentException("Organization already exists");
        }
        return organizationRepository.save(org);
    }
    //getById
    public Organization getById(Integer id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found")); //lamba function for exception
    }

    //getAll
    public List<Organization> getAll() {
        return organizationRepository.findAll();
    }
    //update
    public Organization update(Integer id, Organization updated) {
        //get the already existing org
        Organization existingOrg = getById(id);

        //set the things we want to update
        existingOrg.setName(updated.getName());
        existingOrg.setType(updated.getType());
        existingOrg.setContactEmail(updated.getContactEmail());

        return organizationRepository.save(existingOrg);
    }

    //delete
    public void delete(Integer id) {
        organizationRepository.deleteById(id);
    }
}
