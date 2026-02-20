package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.Facility;
import com.consultrix.consultrixserver.service.FacilityService;
import org.springframework.http.HttpStatus;
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
    public Facility create(@RequestBody Facility request) {
        return facilityService.create(
                request.getOrganization().getId(),
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
    public List<Facility> getAll() {
        return facilityService.listAll();
    }

    //get Facility by id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Facility getFacilityById(@PathVariable("id") Integer id) {
        return facilityService.getById(id);
    }

    //get Facility records by status
    @GetMapping("/status/{status}")
    @ResponseStatus(HttpStatus.OK)
    public List<Facility> getFacilityByStatus(@PathVariable("status") String status) {
        return facilityService.listByStatus(status);
    }

    //update Facility record
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Facility updateFacility(@PathVariable("id") Integer id, @RequestBody Facility facility) {
        return facilityService.update(id, facility);
    }

    //delete Facility record
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFacility(@PathVariable("id") Integer id) {
        facilityService.delete(id);
    }

}
