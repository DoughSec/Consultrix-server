package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.Instructor;
import com.consultrix.consultrixserver.service.InstructorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultrix/instructor")
public class InstructorController {
    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    //create Instructor record
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Instructor create(@RequestBody Instructor request) {
        return instructorService.create(request);
    }

    //get all Instructor records
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Instructor> getAll() {
        return instructorService.getAll();
    }

    //get Instructor by id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Instructor getInstructorById(@PathVariable("id") Integer id) {
        return instructorService.getById(id);
    }

    //update Instructor record
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Instructor updateInstructor(@PathVariable("id") Integer id, @RequestBody Instructor instructor) {
        return instructorService.update(id, instructor);
    }

    //delete Instructor record
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInstructor(@PathVariable("id") Integer id) {
        instructorService.delete(id);
    }

}