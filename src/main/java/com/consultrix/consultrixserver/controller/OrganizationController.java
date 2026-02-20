package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.Organization;
import com.consultrix.consultrixserver.service.OrganizationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultrix/organization")
public class OrganizationController {
    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    //create Organization record
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Organization create(@RequestBody Organization request) {
        return organizationService.create(request);
    }

    //get all Organization records
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Organization> getAll() {
        return organizationService.getAll();
    }

    //get Organization by id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Organization getOrganizationById(@PathVariable("id") Integer id) {
        return organizationService.getById(id);
    }

    //update Organization record
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Organization updateOrganization(@PathVariable("id") Integer id, @RequestBody Organization organization) {
        return organizationService.update(id, organization);
    }

    //delete Organization record
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganization(@PathVariable("id") Integer id) {
        organizationService.delete(id);
    }

}
