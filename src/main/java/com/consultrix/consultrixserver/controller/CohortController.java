package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.Cohort;
import com.consultrix.consultrixserver.model.dto.cohortDTO.CohortRequestDto;
import com.consultrix.consultrixserver.model.dto.cohortDTO.CohortResponseDto;
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
    public CohortResponseDto create(@RequestBody CohortRequestDto request) {
        return cohortService.create(
                request.getFacilityId(),
                request.getPrimaryInstructorUserId(),
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
    @GetMapping("/{cohortId}")
    @ResponseStatus(HttpStatus.OK)
    public Cohort getCohortById(@PathVariable("cohortId") Integer cohortId) {
        return cohortService.getById(cohortId);
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
    @PutMapping("/{cohortId}")
    @ResponseStatus(HttpStatus.OK)
    public CohortResponseDto updateCohort(@PathVariable("id") Integer cohortId, @RequestBody CohortRequestDto cohort) {
        return cohortService.update(cohortId, cohort);
    }

    //delete Cohort record
    @DeleteMapping("/{cohortId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCohort(@PathVariable("cohortId") Integer cohortId) {
        cohortService.delete(cohortId);
    }

}
