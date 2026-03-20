package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Module;
import com.consultrix.consultrixserver.model.Assignment;
import com.consultrix.consultrixserver.model.Grade;
import com.consultrix.consultrixserver.model.Student;
import com.consultrix.consultrixserver.model.Submission;
import com.consultrix.consultrixserver.model.dto.assignmentDTO.AssignmentRequestDto;
import com.consultrix.consultrixserver.model.dto.assignmentDTO.AssignmentResponseDto;
import com.consultrix.consultrixserver.model.dto.assignmentDTO.StudentCourseworkResponseDto;
import com.consultrix.consultrixserver.repository.AssignmentRepository;
import com.consultrix.consultrixserver.repository.GradeRepository;
import com.consultrix.consultrixserver.repository.ModuleRepository;
import com.consultrix.consultrixserver.repository.StudentRepository;
import com.consultrix.consultrixserver.repository.SubmissionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final ModuleService moduleService;
    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;
    private final SubmissionRepository submissionRepository;
    private final GradeRepository gradeRepository;

    public AssignmentService(AssignmentRepository assignmentRepository, ModuleService moduleService,
                             StudentRepository studentRepository, ModuleRepository moduleRepository,
                             SubmissionRepository submissionRepository,
                             GradeRepository gradeRepository) {
        this.assignmentRepository = assignmentRepository;
        this.moduleService = moduleService;
        this.studentRepository = studentRepository;
        this.moduleRepository = moduleRepository;
        this.submissionRepository = submissionRepository;
        this.gradeRepository = gradeRepository;
    }

    //create
    public AssignmentResponseDto create(Integer moduleId, String title, String description, LocalDate dueDate, LocalDateTime dueTime, BigDecimal maxScore) {
        List<Assignment> existing = assignmentRepository.findByModuleId(moduleId);

        for(Assignment assignment : existing) {
            if (assignment.getTitle().equals(title)) {
                throw new IllegalArgumentException("Assignment already exists");
            }
        }

        Module module = moduleService.getById(moduleId);

        Assignment assignment = new Assignment();
        assignment.setModule(module);
        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setDueDate(dueDate);
        assignment.setDueTime(dueTime);
        assignment.setMaxScore(maxScore);

        assignmentRepository.save(assignment);

        AssignmentResponseDto assignmentResponseDto = new AssignmentResponseDto();
        assignmentResponseDto.setAssignmentId(assignment.getId());
        assignmentResponseDto.setModuleId(assignment.getModule().getId());
        assignmentResponseDto.setTitle(assignment.getTitle());
        assignmentResponseDto.setDescription(assignment.getDescription());
        assignmentResponseDto.setDueDate(assignment.getDueDate());
        assignmentResponseDto.setDueTime(assignment.getDueTime());
        assignmentResponseDto.setMaxScore(assignment.getMaxScore());

        return assignmentResponseDto;
    }

    //getById
    public Assignment getById(Integer id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found")); //lamba function for exception
    }

    //getAll
    public List<Assignment> getAll() {
        return assignmentRepository.findAll();
    }

    //getAssignmentByModule
    public List<Assignment> getByModule(Integer moduleId) {
        if (moduleId == null) {
            throw new IllegalArgumentException("moduleId cannot be null");
        }
        return assignmentRepository.findByModuleId(moduleId);
    }

    //getMyAssignments - upcoming assignments for the currently logged in student
    public List<AssignmentResponseDto> getMyAssignments(Integer studentId) {
        List<Assignment> allAssignments = getAssignmentsForStudentCohort(studentId);
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        // filter to only upcoming assignments with no submission from this student
        return allAssignments.stream()
                .filter(assignment -> {
                    // check that the due date has not passed yet
                    if (assignment.getDueTime() != null) {
                        if (now.isAfter(assignment.getDueTime())) {
                            return false;
                        }
                    } else if (assignment.getDueDate() != null) {
                        if (today.isAfter(assignment.getDueDate())) {
                            return false;
                        }
                    }
                    // exclude assignments the student has already submitted
                    return submissionRepository.findByAssignmentIdAndStudentId(assignment.getId(), studentId).isEmpty();
                })
                .sorted(buildAssignmentComparator())
                .map(this::toAssignmentResponseDto)
                .toList();
    }

    public List<StudentCourseworkResponseDto> getMyCoursework(Integer studentId) {
        return getAssignmentsForStudentCohort(studentId).stream()
                .sorted(buildAssignmentComparator())
                .map(assignment -> toStudentCourseworkResponseDto(studentId, assignment))
                .toList();
    }

    //update
    public AssignmentResponseDto update(Integer id, AssignmentRequestDto updated) {
        //get the already existing assignment
        Assignment existingAssignment = getById(id);

        //set the things we want to update
        existingAssignment.setTitle(updated.getTitle());
        existingAssignment.setDescription(updated.getDescription());
        existingAssignment.setDueDate(updated.getDueDate());
        existingAssignment.setDueTime(updated.getDueTime());
        existingAssignment.setMaxScore(updated.getMaxScore());

        assignmentRepository.save(existingAssignment);

        AssignmentResponseDto assignmentResponseDto = new AssignmentResponseDto();
        assignmentResponseDto.setAssignmentId(existingAssignment.getId());
        assignmentResponseDto.setModuleId(existingAssignment.getModule().getId());
        assignmentResponseDto.setTitle(existingAssignment.getTitle());
        assignmentResponseDto.setDescription(existingAssignment.getDescription());
        assignmentResponseDto.setDueDate(existingAssignment.getDueDate());
        assignmentResponseDto.setDueTime(existingAssignment.getDueTime());
        assignmentResponseDto.setMaxScore(existingAssignment.getMaxScore());

        return assignmentResponseDto;
    }

    //delete
    public void delete(Integer id) {
        assignmentRepository.deleteById(id);
    }

    private List<Assignment> getAssignmentsForStudentCohort(Integer studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("Must be logged in");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        if (student.getCohort() == null) {
            return new ArrayList<>();
        }

        List<Module> modules = moduleRepository.findByCohortIdOrderByOrderIndexAsc(student.getCohort().getId());
        List<Assignment> allAssignments = new ArrayList<>();

        for (Module module : modules) {
            allAssignments.addAll(assignmentRepository.findByModuleId(module.getId()));
        }

        return allAssignments;
    }

    private AssignmentResponseDto toAssignmentResponseDto(Assignment assignment) {
        AssignmentResponseDto dto = new AssignmentResponseDto();
        dto.setAssignmentId(assignment.getId());
        dto.setModuleId(assignment.getModule().getId());
        dto.setTitle(assignment.getTitle());
        dto.setDescription(assignment.getDescription());
        dto.setDueDate(assignment.getDueDate());
        dto.setDueTime(assignment.getDueTime());
        dto.setMaxScore(assignment.getMaxScore());
        return dto;
    }

    private StudentCourseworkResponseDto toStudentCourseworkResponseDto(Integer studentId, Assignment assignment) {
        Optional<Submission> submission = submissionRepository.findByAssignmentIdAndStudentId(assignment.getId(), studentId);
        Optional<Grade> grade = submission.flatMap(existingSubmission ->
                gradeRepository.findBySubmissionId(existingSubmission.getId()).stream().findFirst()
        );

        StudentCourseworkResponseDto dto = new StudentCourseworkResponseDto();
        dto.setAssignmentId(assignment.getId());
        dto.setModuleId(assignment.getModule().getId());
        dto.setModuleTitle(assignment.getModule().getTitle());
        dto.setCohortId(assignment.getModule().getCohort().getId());
        dto.setCohortName(assignment.getModule().getCohort().getName());
        dto.setTitle(assignment.getTitle());
        dto.setDescription(assignment.getDescription());
        dto.setDueDate(assignment.getDueDate());
        dto.setDueTime(assignment.getDueTime());
        dto.setMaxScore(assignment.getMaxScore());

        if (submission.isPresent()) {
            Submission existingSubmission = submission.get();
            dto.setSubmissionId(existingSubmission.getId());
            dto.setSubmittedAt(existingSubmission.getSubmittedAt());
            dto.setSubmissionStatus(existingSubmission.getStatus());
        }

        if (grade.isPresent()) {
            Grade existingGrade = grade.get();
            dto.setGradeId(existingGrade.getId());
            dto.setScore(existingGrade.getScore());
            dto.setFeedback(existingGrade.getFeedback());
            dto.setAssignmentGradePercentage(
                    calculateAssignmentGradePercentage(existingGrade.getScore(), assignment.getMaxScore())
            );
            dto.setCourseworkStatus("GRADED");
        } else if (submission.isPresent()) {
            dto.setCourseworkStatus("SUBMITTED");
        } else if (assignment.getDueTime() != null && LocalDateTime.now().isAfter(assignment.getDueTime())) {
            dto.setCourseworkStatus("LATE");
        } else {
            dto.setCourseworkStatus("PENDING");
        }

        return dto;
    }

    private BigDecimal calculateAssignmentGradePercentage(BigDecimal score, BigDecimal maxScore) {
        if (score == null || maxScore == null || maxScore.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return score
                .multiply(new BigDecimal("100"))
                .divide(maxScore, 2, java.math.RoundingMode.HALF_UP);
    }

    private Comparator<Assignment> buildAssignmentComparator() {
        return Comparator
                .comparing((Assignment assignment) -> assignment.getModule().getOrderIndex())
                .thenComparing(assignment -> assignment.getDueTime() != null
                        ? assignment.getDueTime()
                        : assignment.getDueDate() != null
                        ? assignment.getDueDate().atStartOfDay()
                        : LocalDateTime.MAX
                )
                .thenComparing(Assignment::getId);
    }
}
