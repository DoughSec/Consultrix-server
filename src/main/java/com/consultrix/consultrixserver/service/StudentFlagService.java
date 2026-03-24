package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Instructor;
import com.consultrix.consultrixserver.model.Student;
import com.consultrix.consultrixserver.model.StudentFlag;
import com.consultrix.consultrixserver.model.dto.flagDTO.StudentFlagRequestDto;
import com.consultrix.consultrixserver.model.dto.flagDTO.StudentFlagResponseDto;
import com.consultrix.consultrixserver.repository.InstructorRepository;
import com.consultrix.consultrixserver.repository.StudentFlagRepository;
import com.consultrix.consultrixserver.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class StudentFlagService {

    private final StudentFlagRepository studentFlagRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;

    public StudentFlagService(
            StudentFlagRepository studentFlagRepository,
            StudentRepository studentRepository,
            InstructorRepository instructorRepository
    ) {
        this.studentFlagRepository = studentFlagRepository;
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
    }

    // Create a new flag for a student, authored by the given instructor
    public StudentFlagResponseDto createFlag(Integer instructorId, StudentFlagRequestDto request) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found: " + instructorId));

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found: " + request.getStudentId()));

        StudentFlag flag = new StudentFlag();
        flag.setInstructor(instructor);
        flag.setStudent(student);
        flag.setReason(request.getReason());
        flag.setPriority(request.getPriority() != null ? request.getPriority() : "MEDIUM");
        flag.setResolved(false);

        studentFlagRepository.save(flag);

        return toDto(flag);
    }

    // Mark a flag as resolved, recording the resolution timestamp
    public StudentFlagResponseDto resolveFlag(Integer flagId) {
        StudentFlag flag = studentFlagRepository.findById(flagId)
                .orElseThrow(() -> new RuntimeException("Flag not found: " + flagId));

        flag.setResolved(true);
        flag.setResolvedAt(LocalDateTime.now());

        studentFlagRepository.save(flag);

        return toDto(flag);
    }

    // Retrieve all flags associated with a specific student
    @Transactional(readOnly = true)
    public List<StudentFlagResponseDto> getFlagsForStudent(Integer studentId) {
        return studentFlagRepository.findByStudentId(studentId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    // Retrieve all unresolved (active) flags across all students
    @Transactional(readOnly = true)
    public List<StudentFlagResponseDto> getActiveFlags() {
        return studentFlagRepository.findByResolved(false)
                .stream()
                .map(this::toDto)
                .toList();
    }

    // Retrieve all flags created by a specific instructor
    @Transactional(readOnly = true)
    public List<StudentFlagResponseDto> getFlagsForInstructor(Integer instructorId) {
        return studentFlagRepository.findByInstructorId(instructorId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    // Convert a StudentFlag entity to its response DTO
    private StudentFlagResponseDto toDto(StudentFlag flag) {
        StudentFlagResponseDto dto = new StudentFlagResponseDto();
        dto.setId(flag.getId());
        dto.setStudentId(flag.getStudent().getId());
        dto.setStudentFirstName(flag.getStudent().getFirstName());
        dto.setStudentLastName(flag.getStudent().getLastName());
        dto.setInstructorId(flag.getInstructor().getId());
        dto.setInstructorFirstName(flag.getInstructor().getFirstName());
        dto.setInstructorLastName(flag.getInstructor().getLastName());
        dto.setReason(flag.getReason());
        dto.setPriority(flag.getPriority());
        dto.setResolved(flag.isResolved());
        dto.setResolvedAt(flag.getResolvedAt());
        dto.setCreatedAt(flag.getCreatedAt());
        return dto;
    }

}
