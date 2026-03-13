package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Cohort;
import com.consultrix.consultrixserver.model.Organization;
import com.consultrix.consultrixserver.model.Student;
import com.consultrix.consultrixserver.model.dto.studentDTO.StudentRequestDto;
import com.consultrix.consultrixserver.model.dto.studentDTO.StudentResponseDto;
import com.consultrix.consultrixserver.repository.CohortRepository;
import com.consultrix.consultrixserver.repository.OrganizationRepository;
import com.consultrix.consultrixserver.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final CohortRepository cohortRepository;
    private final OrganizationRepository orgRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository, CohortRepository cohortRepository, OrganizationRepository orgRepository
    , PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.cohortRepository = cohortRepository;
        this.orgRepository = orgRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // create Student
    public StudentResponseDto create(
            Integer organizationId, String firstName, String lastName, String email,
            String password, String status,
            Integer cohortId, String graduationStatus, String pipelineStage,
            String interviewStage, String clientName, LocalDate placementStartDate,
            String resumeUrl, String notes
    ) {
        if (cohortId == null) {
            throw new IllegalArgumentException("cohortId is required");
        }

        Cohort cohort = cohortRepository.findById(cohortId)
                .orElseThrow(() -> new IllegalArgumentException("Cohort not found: " + cohortId));

        Organization org = orgRepository.findById(organizationId)
                .orElseThrow(() -> new IllegalArgumentException("Organization not found: " + organizationId));

        Student student = new Student();
        student.setOrganization(org);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setPasswordHash(this.passwordEncoder.encode(password));
        student.setStatus(status);
        student.setRole("ROLE_STUDENT");
        student.setCohort(cohort);
        student.setGraduationStatus(graduationStatus);
        student.setPipelineStage(pipelineStage);
        student.setInterviewStage(interviewStage);
        student.setClientName(clientName);
        student.setPlacementStartDate(placementStartDate);
        student.setResumeUrl(resumeUrl);
        student.setNotes(notes);

        studentRepository.save(student);

        StudentResponseDto responseDto = new StudentResponseDto();
        responseDto.setUserId(student.getId());
        responseDto.setCohortId(student.getCohort().getId());
        responseDto.setGraduationStatus(student.getGraduationStatus());
        responseDto.setPipelineStage(student.getPipelineStage());
        responseDto.setInterviewStage(student.getInterviewStage());
        responseDto.setClientName(student.getClientName());
        responseDto.setPlacementStartDate(student.getPlacementStartDate());
        responseDto.setResumeUrl(student.getResumeUrl());
        responseDto.setNotes(student.getNotes());

        return responseDto;
    }

    // getAll
    @Transactional(readOnly = true)
    public List<Student> listAll() {
        return studentRepository.findAll();
    }

    // getByCohort
    @Transactional(readOnly = true)
    public List<Student> listByCohort(Integer cohortId) {
        if (cohortId == null) {
            throw new IllegalArgumentException("cohortId is required");
        }
        return studentRepository.findByCohortId(cohortId);
    }

    // getByPipelineStage
    @Transactional(readOnly = true)
    public List<Student> listByPipelineStage(String pipelineStage) {
        if (pipelineStage == null) {
            throw new IllegalArgumentException("pipelineStage is required");
        }
        return studentRepository.findByPipelineStage(pipelineStage);
    }

    // getByGraduationStatus
    @Transactional(readOnly = true)
    public List<Student> listByGraduationStatus(String graduationStatus) {
        if (graduationStatus == null) {
            throw new IllegalArgumentException("graduationStatus is required");
        }
        return studentRepository.findByGraduationStatus(graduationStatus);
    }

    // getById
    @Transactional(readOnly = true)
    public Student getById(Integer studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("studentId is required");
        }
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));
    }

    // update Student
    public StudentResponseDto update(Integer studentId, StudentRequestDto updated) {
        Student existing = getById(studentId);

        Organization org = orgRepository.findById(updated.getOrganizationId())
                .orElseThrow(() -> new IllegalArgumentException("Organization not found: " + updated.getOrganizationId()));

        Cohort cohort = cohortRepository.findById(updated.getCohortId())
                .orElseThrow(() -> new IllegalArgumentException("Cohort not found: " + updated.getCohortId()));

        existing.setOrganization(org);
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setEmail(updated.getEmail());
        existing.setPasswordHash(this.passwordEncoder.encode(updated.getPassword()));
        existing.setStatus(updated.getStatus());
        existing.setCohort(cohort);
        existing.setGraduationStatus(updated.getGraduationStatus());
        existing.setPipelineStage(updated.getPipelineStage());
        existing.setInterviewStage(updated.getInterviewStage());
        existing.setClientName(updated.getClientName());
        existing.setPlacementStartDate(updated.getPlacementStartDate());
        existing.setResumeUrl(updated.getResumeUrl());
        existing.setNotes(updated.getNotes());

        studentRepository.save(existing);

        StudentResponseDto responseDto = new StudentResponseDto();
        responseDto.setUserId(existing.getId());
        responseDto.setCohortId(existing.getCohort().getId());
        responseDto.setGraduationStatus(existing.getGraduationStatus());
        responseDto.setPipelineStage(existing.getPipelineStage());
        responseDto.setInterviewStage(existing.getInterviewStage());
        responseDto.setClientName(existing.getClientName());
        responseDto.setPlacementStartDate(existing.getPlacementStartDate());
        responseDto.setResumeUrl(existing.getResumeUrl());
        responseDto.setNotes(existing.getNotes());

        return responseDto;
    }

    // delete Student
    public void delete(Integer studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("studentId is required");
        }
        if (!studentRepository.existsById(studentId)) {
            throw new IllegalArgumentException("Student not found: " + studentId);
        }
        studentRepository.deleteById(studentId);
    }

    // update Student password
    public void updatePassword(Integer studentId, String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password is required");
        }
        Student existing = getById(studentId);
        existing.setPasswordHash(passwordEncoder.encode(newPassword));
        studentRepository.save(existing);
    }
}
