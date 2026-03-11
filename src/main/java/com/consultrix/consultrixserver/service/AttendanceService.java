package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Attendance;
import com.consultrix.consultrixserver.model.Cohort;
import com.consultrix.consultrixserver.model.Student;
import com.consultrix.consultrixserver.model.dto.attendanceDTO.AttendanceRequestDto;
import com.consultrix.consultrixserver.model.dto.attendanceDTO.AttendanceResponseDto;
import com.consultrix.consultrixserver.repository.AttendanceRepository;
import com.consultrix.consultrixserver.repository.CohortRepository;
import com.consultrix.consultrixserver.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final CohortRepository cohortRepository;
    private final StudentRepository studentRepository;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            CohortRepository cohortRepository,
            StudentRepository studentRepository
    ) {
        this.attendanceRepository = attendanceRepository;
        this.cohortRepository = cohortRepository;
        this.studentRepository = studentRepository;
    }

    // create attendance
    public AttendanceResponseDto create(Integer cohortId, Integer studentUserId, LocalDate attendanceDate, String status, String note) {
        if (cohortId == null || studentUserId == null || attendanceDate == null) {
            throw new IllegalArgumentException("cohortId, studentId, and attendanceDate are required");
        }

        // Prevent duplicates for same student+cohort+date
        if (attendanceRepository.findByCohortIdAndStudentIdAndAttendanceDate(cohortId, studentUserId, attendanceDate)) {
            throw new IllegalArgumentException("Attendance already exists for this student on this date in this cohort");
        }

        Cohort cohort = cohortRepository.findById(cohortId)
                .orElseThrow(() -> new IllegalArgumentException("Cohort not found: " + cohortId));

        Student student = studentRepository.findById(studentUserId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentUserId));

        Attendance attendance = new Attendance();
        attendance.setCohort(cohort);
        attendance.setStudent(student);
        attendance.setAttendanceDate(attendanceDate);
        attendance.setStatus(status);
        attendance.setNote(note);

        attendanceRepository.save(attendance);

        AttendanceResponseDto attendanceResponseDto = new AttendanceResponseDto();
        attendanceResponseDto.setAttendanceId(attendance.getId());
        attendanceResponseDto.setCohortId(attendance.getCohort().getId());
        attendanceResponseDto.setStudentUserId(attendance.getStudent().getId());
        attendanceResponseDto.setAttendanceDate(attendance.getAttendanceDate());
        attendanceResponseDto.setStatus(attendance.getStatus());
        attendanceResponseDto.setNote(attendance.getNote());

        return attendanceResponseDto;
    }

    // getAll
    @Transactional(readOnly = true)
    public List<Attendance> listAll() {
        return attendanceRepository.findAll();
    }

    // getByCohort
    @Transactional(readOnly = true)
    public List<Attendance> listByCohort(Integer cohortId) {
        if (cohortId == null) {
            throw new IllegalArgumentException("cohortId is required");
        }
        return attendanceRepository.findByCohortId(cohortId);
    }

    // getByStudent
    @Transactional(readOnly = true)
    public List<Attendance> listByStudent(Integer studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("studentId is required");
        }
        return attendanceRepository.findByStudentId(studentId);
    }

    // getById
    @Transactional(readOnly = true)
    public Attendance getById(Integer attendanceId) {
        if (attendanceId == null) {
            throw new IllegalArgumentException("attendanceId is required");
        }
        return attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new IllegalArgumentException("Attendance not found: " + attendanceId));
    }

    // update Attendance
    public AttendanceResponseDto update(Integer attendanceId, AttendanceRequestDto updated) {
        Attendance existing = getById(attendanceId);

        existing.setAttendanceDate(updated.getAttendanceDate());
        existing.setStatus(updated.getStatus());
        existing.setNote(updated.getNote());

        attendanceRepository.save(existing);

        AttendanceResponseDto attendanceResponseDto = new AttendanceResponseDto();
        attendanceResponseDto.setAttendanceId(existing.getId());
        attendanceResponseDto.setCohortId(existing.getCohort().getId());
        attendanceResponseDto.setStudentUserId(existing.getStudent().getId());
        attendanceResponseDto.setAttendanceDate(existing.getAttendanceDate());
        attendanceResponseDto.setStatus(existing.getStatus());
        attendanceResponseDto.setNote(existing.getNote());

        return attendanceResponseDto;
    }

    // delete attendance
    public void delete(Integer attendanceId) {
        if (attendanceId == null) {
            throw new IllegalArgumentException("attendanceId is required");
        }
        if (!attendanceRepository.existsById(attendanceId)) {
            throw new IllegalArgumentException("Attendance not found: " + attendanceId);
        }
        attendanceRepository.deleteById(attendanceId);
    }
}
