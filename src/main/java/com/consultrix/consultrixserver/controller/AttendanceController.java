package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.Attendance;
import com.consultrix.consultrixserver.service.AttendanceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultrix/attendance")
public class AttendanceController {
     private final AttendanceService attendanceService;

     public AttendanceController(AttendanceService attendanceService) {
         this.attendanceService = attendanceService;
     }

    //create attendance record
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Attendance create(@RequestBody Attendance request) {
         return attendanceService.create(
                 request.getCohort().getId(),
                 request.getStudent().getId(),
                 request.getAttendanceDate(),
                 request.getStatus(),
                 request.getNote()
         );
     }

     //get all attendance records
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Attendance> getAll() {
         return attendanceService.listAll();
    }

    //get attendance by id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Attendance getAttendanceById(@PathVariable("id") Integer id) {
         return attendanceService.getById(id);
    }

    //get attendance records by cohort
    @GetMapping("/cohort/{cohortId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Attendance> getAttendanceByCohort(@PathVariable("cohortId") Integer cohortId) {
         return attendanceService.listByCohort(cohortId);
    }

    //get attendance records by student
    @GetMapping("/student/{studentId}")
    public List<Attendance> getAttendanceByStudent(@PathVariable("studentId") Integer studentId) {
        return attendanceService.listByStudent(studentId);
    }

    //update attendance record
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Attendance updateAttendance(@PathVariable("id") Integer id, @RequestBody Attendance attendance) {
         return attendanceService.update(id, attendance);
    }

    //delete attendance record
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAttendance(@PathVariable("id") Integer id) {
         attendanceService.delete(id);
    }

}
