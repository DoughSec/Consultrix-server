package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.Organization;
import com.consultrix.consultrixserver.model.dto.organizationDTO.OrganizationRequestDto;
import com.consultrix.consultrixserver.model.dto.organizationDTO.OrganizationResponseDto;
import com.consultrix.consultrixserver.service.OrganizationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultrix/organizations")
public class OrganizationController {
    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    //create Organization record
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrganizationResponseDto create(@RequestBody OrganizationRequestDto request) {
        return organizationService.create(
                request.getName(),
                request.getType(),
                request.getContactEmail()
        );
    }

    //get all Organization records
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Organization> getAll() {
        return organizationService.getAll();
    }

    //get Organization by id
    @GetMapping("/{organizationId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Organization getOrganizationById(@PathVariable("organizationId") Integer organizationId) {
        return organizationService.getById(organizationId);
    }

    //update Organization record
    @PutMapping("/{organizationId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrganizationResponseDto updateOrganization(@PathVariable("organizationId") Integer organizationId, @RequestBody OrganizationRequestDto organization) {
        return organizationService.update(organizationId, organization);
    }

    //delete Organization record
    @DeleteMapping("/{organizationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteOrganization(@PathVariable("organizationId") Integer organizationId) {
        organizationService.delete(organizationId);
    }

}
