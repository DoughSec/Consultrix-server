package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.Cohort;
import com.consultrix.consultrixserver.service.CohortService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultrix/cohort")
public class CohortController {
    private final CohortService cohortService;

    public CohortController(CohortService cohortService) {
        this.cohortService = cohortService;
    }

    //create Cohort record
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cohort create(@RequestBody Cohort request) {
        return cohortService.create(
                request.getFacility().getId(),
                request.getPrimaryInstructor().getId(),
                request.getName(),
                request.getStartDate(),
                request.getEndDate(),
                request.getCapacity(),
                request.getStatus()
        );
    }

    //get all Cohort records
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Cohort> getAll() {
        return cohortService.listAll();
    }

    //get Cohort by id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cohort getCohortById(@PathVariable("id") Integer id) {
        return cohortService.getById(id);
    }

    //get Cohort records by status
    @GetMapping("/status/{status}")
    @ResponseStatus(HttpStatus.OK)
    public List<Cohort> getCohortByStatus(@PathVariable("status") String status) {
        return cohortService.listByStatus(status);
    }

    //get Cohort records by facility
    @GetMapping("/facility/{facilityId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Cohort> getCohortByFacility(@PathVariable("facilityId") Integer facilityId) {
        return cohortService.listByFacility(facilityId);
    }

    //update Cohort record
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Cohort updateCohort(@PathVariable("id") Integer id, @RequestBody Cohort cohort) {
        return cohortService.update(id, cohort);
    }

    //delete Cohort record
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCohort(@PathVariable("id") Integer id) {
        cohortService.delete(id);
    }

}
