package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Attendance;
import com.consultrix.consultrixserver.model.Cohort;
import com.consultrix.consultrixserver.model.Student;
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
    public Attendance create(Integer cohortId, Integer studentId, LocalDate attendanceDate, String status, String note) {
        if (cohortId == null || studentId == null || attendanceDate == null) {
            throw new IllegalArgumentException("cohortId, studentId, and attendanceDate are required");
        }

        // Prevent duplicates for same student+cohort+date
        if (attendanceRepository.findByCohortIdAndStudentIdAndAttendanceDate(cohortId, studentId, attendanceDate)) {
            throw new IllegalArgumentException("Attendance already exists for this student on this date in this cohort");
        }

        Cohort cohort = cohortRepository.findById(cohortId)
                .orElseThrow(() -> new IllegalArgumentException("Cohort not found: " + cohortId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        Attendance attendance = new Attendance();
        attendance.setCohort(cohort);
        attendance.setStudent(student);
        attendance.setAttendanceDate(attendanceDate);
        attendance.setStatus(status);
        attendance.setNote(note);

        return attendanceRepository.save(attendance);
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
    public Attendance update(Integer attendanceId, Attendance updated) {
        Attendance existing = getById(attendanceId);

        existing.setStatus(updated.getStatus());
        existing.setNote(updated.getNote());

        return attendanceRepository.save(existing);
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
