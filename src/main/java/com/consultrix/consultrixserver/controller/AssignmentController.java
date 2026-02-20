package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.Assignment;
import com.consultrix.consultrixserver.service.AssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultrix/assignment")
public class AssignmentController {
    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    //create Assignment record
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Assignment create(@RequestBody Assignment request) {
        return assignmentService.create(request);
    }

    //get all Assignment records
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Assignment> getAll() {
        return assignmentService.getAll();
    }

    //get Assignment by id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Assignment getAssignmentById(@PathVariable("id") Integer id) {
        return assignmentService.getById(id);
    }

    //get Assignment records by module
    @GetMapping("/module/{moduleId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Assignment> getAssignmentByModule(@PathVariable("moduleId") Integer moduleId) {
        return assignmentService.getByModule(moduleId);
    }

    //update Assignment record
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Assignment updateAssignment(@PathVariable("id") Integer id, @RequestBody Assignment assignment) {
        return assignmentService.update(id, assignment);
    }

    //delete Assignment record
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAssignment(@PathVariable("id") Integer id) {
        assignmentService.delete(id);
    }

}
