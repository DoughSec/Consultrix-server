package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.Student;
import com.consultrix.consultrixserver.model.dto.studentDTO.StudentRequestDto;
import com.consultrix.consultrixserver.model.dto.studentDTO.StudentResponseDto;
import com.consultrix.consultrixserver.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultrix/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    //create Student record
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public StudentResponseDto create(@RequestBody StudentRequestDto request) {
        return studentService.create(
                request.getCohortId(),
                request.getGraduationStatus(),
                request.getPipelineStage(),
                request.getInterviewStage(),
                request.getClientName(),
                request.getPlacementStartDate(),
                request.getResumeUrl(),
                request.getNotes()
        );
    }

    //get all Student records
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<Student> getAll() {
        return studentService.listAll();
    }

    //get Student by id
    @GetMapping("/{studentId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public Student getStudentById(@PathVariable("studentId") Integer studentId) {
        return studentService.getById(studentId);
    }

    //get Student records by cohort
    @GetMapping("/cohort/{cohortId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<Student> getStudentByCohort(@PathVariable("cohortId") Integer cohortId) {
        return studentService.listByCohort(cohortId);
    }

    //get Student records by pipeline stage
    @GetMapping("/pipeline/{pipelineStage}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<Student> getStudentByPipelineStage(@PathVariable("pipelineStage") String pipelineStage) {
        return studentService.listByPipelineStage(pipelineStage);
    }

    //get Student records by graduation status
    @GetMapping("/graduation/{graduationStatus}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<Student> getStudentByGraduationStatus(@PathVariable("graduationStatus") String graduationStatus) {
        return studentService.listByGraduationStatus(graduationStatus);
    }

    //update Student record
    @PutMapping("/{studentId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public StudentResponseDto updateStudent(@PathVariable("studentId") Integer studentId, @RequestBody StudentRequestDto request) {
        return studentService.update(studentId, request);
    }

    //delete Student record
    @DeleteMapping("/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteStudent(@PathVariable("studentId") Integer studentId) {
        studentService.delete(studentId);
    }

}

