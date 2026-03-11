package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.Attendance;
import com.consultrix.consultrixserver.model.dto.attendanceDTO.AttendanceRequestDto;
import com.consultrix.consultrixserver.model.dto.attendanceDTO.AttendanceResponseDto;
import com.consultrix.consultrixserver.service.AttendanceService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public AttendanceResponseDto create(@RequestBody AttendanceRequestDto request) {
         return attendanceService.create(
                 request.getCohortId(),
                 request.getStudentUserId(),
                 request.getAttendanceDate(),
                 request.getStatus(),
                 request.getNote()
         );
     }

     //get all attendance records
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<Attendance> getAll() {
         return attendanceService.listAll();
    }

    //get attendance by id
    @GetMapping("/{attendanceId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public Attendance getAttendanceById(@PathVariable("attendanceId") Integer attendanceId) {
         return attendanceService.getById(attendanceId);
    }

    //get attendance records by cohort
    @GetMapping("/cohort/{cohortId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Attendance> getAttendanceByCohort(@PathVariable("cohortId") Integer cohortId) {
         return attendanceService.listByCohort(cohortId);
    }

    //get attendance records by student
    @GetMapping("/student/{studentId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<Attendance> getAttendanceByStudent(@PathVariable("studentId") Integer studentId) {
        return attendanceService.listByStudent(studentId);
    }

    //update attendance record
    @PutMapping("/{attendanceId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public AttendanceResponseDto updateAttendance(@PathVariable("attendanceId") Integer attendanceId, @RequestBody AttendanceRequestDto attendance) {
         return attendanceService.update(attendanceId, attendance);
    }

    //delete attendance record
    @DeleteMapping("/{attendanceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public void deleteAttendance(@PathVariable("attendanceId") Integer attendanceId) {
         attendanceService.delete(attendanceId);
    }

}
