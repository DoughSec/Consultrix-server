package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Organization;
import com.consultrix.consultrixserver.repository.OrganizationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    //create
//    public Organization createOrganiztion(Organization)
    //getById

    //getAll

    //update

    //delete
}
