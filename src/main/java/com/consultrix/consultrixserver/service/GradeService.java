package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.*;
import com.consultrix.consultrixserver.model.dto.gradeDTO.GradeProfileResponseDto;
import com.consultrix.consultrixserver.model.dto.gradeDTO.GradeRequestDto;
import com.consultrix.consultrixserver.model.dto.gradeDTO.GradeResponseDto;
import com.consultrix.consultrixserver.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class GradeService {

    private final GradeRepository gradeRepository;
    private final SubmissionRepository submissionRepository;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;
    private final AssignmentRepository assignmentRepository;

    public GradeService(GradeRepository gradeRepository, SubmissionRepository submissionRepository, InstructorRepository instructorRepository,
                        StudentRepository studentRepository, AssignmentRepository assignmentRepository) {
        this.gradeRepository = gradeRepository;
        this.submissionRepository = submissionRepository;
        this.instructorRepository = instructorRepository;
        this.studentRepository = studentRepository;
        this.assignmentRepository = assignmentRepository;
    }

    // create Grade
    public GradeResponseDto create(Integer submissionId, Integer instructorUserId, BigDecimal score, String feedback) {
        if (submissionId == null || instructorUserId == null) {
            throw new IllegalArgumentException("submissionId and instructorUserId are required");
        }

        // Prevent duplicates for same submission + instructor
        if (gradeRepository.findBySubmissionIdAndInstructorId(submissionId, instructorUserId).isPresent()) {
            throw new IllegalArgumentException("Grade already exists for this submission by this instructor");
        }

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found: " + submissionId));

        Instructor instructor = instructorRepository.findById(instructorUserId)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found: " + instructorUserId));

        Grade grade = new Grade();
        grade.setSubmission(submission);
        grade.setInstructor(instructor);
        grade.setScore(score);
        grade.setFeedback(feedback);

        gradeRepository.save(grade);

        GradeResponseDto responseDto = new GradeResponseDto();
        responseDto.setGradeId(grade.getId());
        responseDto.setSubmissionId(grade.getSubmission().getId());
        responseDto.setInstructorUserId(grade.getInstructor().getId());
        responseDto.setScore(grade.getScore());
        responseDto.setFeedback(grade.getFeedback());

        return responseDto;
    }

    // getAll
    @Transactional(readOnly = true)
    public List<Grade> listAll() {
        return gradeRepository.findAll();
    }

    // getById
    @Transactional(readOnly = true)
    public Grade getById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("gradeId is required");
        }
        return gradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Grade not found with id: " + id));
    }

    // getBySubmission
    @Transactional(readOnly = true)
    public List<Grade> listBySubmission(Integer submissionId) {
        if (submissionId == null) {
            throw new IllegalArgumentException("submissionId is required");
        }
        return gradeRepository.findBySubmissionId(submissionId);
    }

    // getByInstructor
    @Transactional(readOnly = true)
    public List<Grade> listByInstructor(Integer instructorUserId) {
        if (instructorUserId == null) {
            throw new IllegalArgumentException("instructorUserId is required");
        }
        return gradeRepository.findByInstructorId(instructorUserId);
    }

    //getMyGrades
    @Transactional(readOnly = true)
    public List<GradeProfileResponseDto> getMyGrades(Integer studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("Must be logged in");
        }
        if (!studentRepository.existsById(studentId)) {
            throw new IllegalArgumentException("Student not found with id: " + studentId);
        }

        List<Grade> grades = gradeRepository.findBySubmissionStudentId(studentId);

        // pre-compute the overall earned score (sum of all graded scores for this student)
        BigDecimal overallEarnedScore = grades.stream()
                .map(Grade::getScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Scope the overall percentage to the student's own cohort assignments only
        BigDecimal overallPercent = calculateOverallGradePercentageForStudent(studentId, overallEarnedScore);
        String overallLetter = calculateOverallLetterGrade(overallPercent);

        return grades.stream().map(grade -> {
            Submission submission = grade.getSubmission();
            Assignment assignment = submission.getAssignment();
            Integer moduleId = assignment.getModule().getId();

            GradeProfileResponseDto dto = new GradeProfileResponseDto();
            dto.setSubmissionId(submission.getId());
            dto.setAssignmentId(assignment.getId());
            dto.setModuleId(moduleId);
            dto.setScore(grade.getScore());
            dto.setFeedback(grade.getFeedback());

            dto.setOverallGradePercentage(overallPercent);
            dto.setOverallLetterGrade(overallLetter);
            dto.setModuleGradePercentage(calculateModuleGradePercentage(studentId, moduleId));
            dto.setAssignmentGradePercentage(calculateAssignmentGradePercentage(grade.getScore(), assignment.getMaxScore()));

            return dto;
        }).toList();
    }

    // getByAssignment — all grades for all submissions belonging to an assignment
    @Transactional(readOnly = true)
    public List<Grade> listByAssignment(Integer assignmentId) {
        if (assignmentId == null) {
            throw new IllegalArgumentException("assignmentId is required");
        }
        return gradeRepository.findBySubmission_Assignment_Id(assignmentId);
    }

    // update Grade
    public GradeResponseDto update(Integer id, GradeRequestDto updated) {
        Grade existing = getById(id);

        existing.setScore(updated.getScore());
        existing.setFeedback(updated.getFeedback());

        gradeRepository.save(existing);

        GradeResponseDto responseDto = new GradeResponseDto();
        responseDto.setGradeId(existing.getId());
        responseDto.setSubmissionId(existing.getSubmission().getId());
        responseDto.setInstructorUserId(existing.getInstructor().getId());
        responseDto.setScore(existing.getScore());
        responseDto.setFeedback(existing.getFeedback());

        return responseDto;
    }

    // delete Grade
    public void delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("gradeId is required");
        }
        if (!gradeRepository.existsById(id)) {
            throw new IllegalArgumentException("Grade not found with id: " + id);
        }
        gradeRepository.deleteById(id);
    }

    // ── grade-calculation helpers ───────────────────────────────────────

    //Convert an overall percentage into a letter grade
    private String calculateOverallLetterGrade(BigDecimal overallGradePercentage) {
        if (overallGradePercentage == null) {
            return "N/A";
        }
        if (overallGradePercentage.compareTo(new BigDecimal("90")) >= 0) {
            return "A";
        } else if (overallGradePercentage.compareTo(new BigDecimal("80")) >= 0) {
            return "B";
        } else if (overallGradePercentage.compareTo(new BigDecimal("70")) >= 0) {
            return "C";
        } else if (overallGradePercentage.compareTo(new BigDecimal("60")) >= 0) {
            return "D";
        } else {
            return "F";
        }
    }

    // Overall grade percentage scoped to only the student's own cohort's assignments
    private BigDecimal calculateOverallGradePercentageForStudent(Integer studentId, BigDecimal overallEarnedScore) {
        LocalDateTime now = LocalDateTime.now();

        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null || student.getCohort() == null) {
            return calculateOverallGradePercentage(overallEarnedScore);
        }

        List<Assignment> cohortAssignments = assignmentRepository.findByModule_Cohort_Id(student.getCohort().getId());
        BigDecimal maxScore = BigDecimal.ZERO;
        for (Assignment assignment : cohortAssignments) {
            if (isAssignmentPastDue(assignment, now)) {
                maxScore = maxScore.add(assignment.getMaxScore());
            }
        }

        if (maxScore.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        return overallEarnedScore
                .multiply(new BigDecimal("100"))
                .divide(maxScore, 2, RoundingMode.HALF_UP);
    }

    //find the overall grade percentage based on all assignments
    private BigDecimal calculateOverallGradePercentage(BigDecimal overallEarnedScore) {
        LocalDateTime now = LocalDateTime.now();

        List<Assignment> assignments = assignmentRepository.findAll();
        BigDecimal overallMaxScore = BigDecimal.ZERO;

        for (Assignment assignment : assignments) {
            if (isAssignmentPastDue(assignment, now)) {
                overallMaxScore = overallMaxScore.add(assignment.getMaxScore());
            }
        }

        if (overallMaxScore.compareTo(BigDecimal.ZERO) == 0) {
            return null; // no assignments are due yet
        }

        return overallEarnedScore
                .multiply(new BigDecimal("100"))
                .divide(overallMaxScore, 2, RoundingMode.HALF_UP);
    }

    //calculate the module percent score based on the current student's scores for each assignment in a module
    private BigDecimal calculateModuleGradePercentage(Integer studentId, Integer moduleId) {
        LocalDateTime now = LocalDateTime.now();

        List<Assignment> moduleAssignments = assignmentRepository.findByModuleId(moduleId);
        BigDecimal moduleMaxScore = BigDecimal.ZERO;

        for (Assignment assignment : moduleAssignments) {
            if (isAssignmentPastDue(assignment, now)) {
                moduleMaxScore = moduleMaxScore.add(assignment.getMaxScore());
            }
        }

        if (moduleMaxScore.compareTo(BigDecimal.ZERO) == 0) {
            return null; // no module assignments are due yet
        }

        // sum the student's graded scores for submissions in this module
        List<Grade> moduleGrades = gradeRepository.findBySubmissionStudentId(studentId).stream()
                .filter(g -> g.getSubmission().getAssignment().getModule().getId().equals(moduleId))
                .toList();

        BigDecimal moduleEarnedScore = moduleGrades.stream()
                .map(Grade::getScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return moduleEarnedScore
                .multiply(new BigDecimal("100"))
                .divide(moduleMaxScore, 2, RoundingMode.HALF_UP);
    }

    //calculateAssignmentGradePercentage based on an existing assignment score and max-score of that assignment
    private BigDecimal calculateAssignmentGradePercentage(BigDecimal score, BigDecimal maxScore) {
        if (maxScore == null || maxScore.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        return score
                .multiply(new BigDecimal("100"))
                .divide(maxScore, 2, RoundingMode.HALF_UP);
    }

    //check if the assignment is past due
    private boolean isAssignmentPastDue(Assignment assignment, LocalDateTime now) {
        if (assignment.getDueTime() != null) {
            return now.isAfter(assignment.getDueTime());
        }
        if (assignment.getDueDate() != null) {
            return now.toLocalDate().isAfter(assignment.getDueDate());
        }
        return false;
    }
}