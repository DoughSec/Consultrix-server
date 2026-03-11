package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Organization;
import com.consultrix.consultrixserver.model.dto.organizationDTO.OrganizationRequestDto;
import com.consultrix.consultrixserver.model.dto.organizationDTO.OrganizationResponseDto;
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
    public OrganizationResponseDto create(String name, String type, String contactEmail) {

        Organization existing = organizationRepository.findByNameIgnoreCase(name);

        if(existing != null) {
            throw new IllegalArgumentException("Organization already exists");
        }

        Organization org = new Organization();
        org.setName(name);
        org.setType(type);
        org.setContactEmail(contactEmail);

        organizationRepository.save(org);

        OrganizationResponseDto responseDto = new OrganizationResponseDto();
        responseDto.setOrganizationId(org.getId());
        responseDto.setName(org.getName());
        responseDto.setType(org.getType());
        responseDto.setContactEmail(org.getContactEmail());

        return responseDto;
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
    public OrganizationResponseDto update(Integer id, OrganizationRequestDto updated) {
        //get the already existing org
        Organization existingOrg = getById(id);

        //set the things we want to update
        existingOrg.setName(updated.getName());
        existingOrg.setType(updated.getType());
        existingOrg.setContactEmail(updated.getContactEmail());

        organizationRepository.save(existingOrg);

        OrganizationResponseDto responseDto = new OrganizationResponseDto();
        responseDto.setOrganizationId(existingOrg.getId());
        responseDto.setName(existingOrg.getName());
        responseDto.setType(existingOrg.getType());
        responseDto.setContactEmail(existingOrg.getContactEmail());

        return responseDto;
    }

    //delete
    public void delete(Integer id) {
        organizationRepository.deleteById(id);
    }
}
