package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.Facility;
import com.consultrix.consultrixserver.model.dto.facilityDTO.FacilityRequestDto;
import com.consultrix.consultrixserver.model.dto.facilityDTO.FacilityResponseDto;
import com.consultrix.consultrixserver.service.FacilityService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/consultrix/facility")
public class FacilityController {
    private final FacilityService facilityService;

    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    //create Facility record
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public FacilityResponseDto create(@RequestBody FacilityRequestDto request) {
        return facilityService.create(
                request.getOrganizationId(),
                request.getName(),
                request.getAddress_line1(),
                request.getCity(),
                request.getState(),
                request.getCountry(),
                request.getCapacity(),
                request.getLeaseStartDate(),
                request.getLeaseEndDate()
        );
    }

    //get all Facility records
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Facility> getAll() {
        return facilityService.listAll();
    }

    //get Facility by id
    @GetMapping("/{facilityId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Facility getFacilityById(@PathVariable("facilityId") Integer facilityId) {
        return facilityService.getById(facilityId);
    }

    //get Facility records by status
    @GetMapping("/status/{status}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Facility> getFacilityByStatus(@PathVariable("status") String status) {
        return facilityService.listByStatus(status);
    }

    //update Facility record
    @PutMapping("/{facilityId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public FacilityResponseDto updateFacility(@PathVariable("facilityId") Integer facilityId, @RequestBody FacilityRequestDto facility) {
        return facilityService.update(facilityId, facility);
    }

    //delete Facility record
    @DeleteMapping("/{facilityId")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteFacility(@PathVariable("facilityId") Integer facilityId) {
        facilityService.delete(facilityId);
    }

}
