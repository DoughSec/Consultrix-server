package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Grade;
import com.consultrix.consultrixserver.model.Student;
import com.consultrix.consultrixserver.model.Submission;
import com.consultrix.consultrixserver.model.dto.adminDTO.AdminStatsResponseDto;
import com.consultrix.consultrixserver.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AdminStatsService {

    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final CohortRepository cohortRepository;
    private final FacilityRepository facilityRepository;
    private final ModuleRepository moduleRepository;
    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;
    private final GradeRepository gradeRepository;

    public AdminStatsService(
            StudentRepository studentRepository,
            InstructorRepository instructorRepository,
            CohortRepository cohortRepository,
            FacilityRepository facilityRepository,
            ModuleRepository moduleRepository,
            AssignmentRepository assignmentRepository,
            SubmissionRepository submissionRepository,
            GradeRepository gradeRepository) {
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
        this.cohortRepository = cohortRepository;
        this.facilityRepository = facilityRepository;
        this.moduleRepository = moduleRepository;
        this.assignmentRepository = assignmentRepository;
        this.submissionRepository = submissionRepository;
        this.gradeRepository = gradeRepository;
    }

    public AdminStatsResponseDto getStats() {
        AdminStatsResponseDto dto = new AdminStatsResponseDto();

        List<Student> students = studentRepository.findAll();
        List<Submission> submissions = submissionRepository.findAll();
        List<Grade> grades = gradeRepository.findAll();

        // ── Platform counts ───────────────────────────────────────────────
        dto.setTotalStudents((int) studentRepository.count());
        dto.setTotalInstructors((int) instructorRepository.count());
        dto.setTotalCohorts((int) cohortRepository.count());
        dto.setTotalFacilities((int) facilityRepository.count());
        dto.setTotalModules((int) moduleRepository.count());
        dto.setTotalAssignments((int) assignmentRepository.count());
        dto.setTotalSubmissions((int) submissionRepository.count());
        dto.setTotalGrades((int) gradeRepository.count());

        // ── Student pipeline ──────────────────────────────────────────────
        dto.setStudentsNotStarted((int) students.stream()
                .filter(s -> "NOT_STARTED".equals(s.getPipelineStage())).count());
        dto.setStudentsInProgress((int) students.stream()
                .filter(s -> "IN_PROGRESS".equals(s.getPipelineStage())).count());
        dto.setStudentsPlaced((int) students.stream()
                .filter(s -> "COMPLETED".equals(s.getPipelineStage())).count());

        // ── Graduation status ─────────────────────────────────────────────
        dto.setStudentsGradActive((int) students.stream()
                .filter(s -> "ACTIVE".equals(s.getGraduationStatus())).count());
        dto.setStudentsGradCompleted((int) students.stream()
                .filter(s -> "COMPLETED".equals(s.getGraduationStatus())).count());
        dto.setStudentsGradWithdrawn((int) students.stream()
                .filter(s -> "WITHDRAWN".equals(s.getGraduationStatus())).count());

        // ── Interview stage ───────────────────────────────────────────────
        dto.setInterviewNone((int) students.stream()
                .filter(s -> s.getInterviewStage() == null || "NONE".equals(s.getInterviewStage())).count());
        dto.setInterviewScreen((int) students.stream()
                .filter(s -> "SCREEN".equals(s.getInterviewStage())).count());
        dto.setInterviewTechnical((int) students.stream()
                .filter(s -> "TECHNICAL".equals(s.getInterviewStage())).count());
        dto.setInterviewFinal((int) students.stream()
                .filter(s -> "FINAL".equals(s.getInterviewStage())).count());

        // ── Cohort status ─────────────────────────────────────────────────
        dto.setCohortsRecruiting(cohortRepository.findByStatus("RECRUITING").size());
        dto.setCohortsInterviewing(cohortRepository.findByStatus("INTERVIEWING").size());
        dto.setCohortsActive(cohortRepository.findByStatus("ACTIVE").size());
        dto.setCohortsCompleted(cohortRepository.findByStatus("COMPLETED").size());
        dto.setCohortsArchived(cohortRepository.findByStatus("ARCHIVED").size());

        // ── Submission status ─────────────────────────────────────────────
        dto.setSubmissionsSubmitted(submissionRepository.findByStatus("SUBMITTED").size());
        dto.setSubmissionsLate(submissionRepository.findByStatus("LATE").size());
        dto.setSubmissionsMissing(submissionRepository.findByStatus("MISSING").size());

        // Graded = submissions that have at least one grade record
        long graded = submissions.stream()
                .filter(s -> gradeRepository.findBySubmissionId(s.getId()).stream().findFirst().isPresent())
                .count();
        dto.setSubmissionsGraded((int) graded);
        dto.setSubmissionsUngraded((int) (submissions.size() - graded));

        // ── Grade distribution ────────────────────────────────────────────
        int countA = 0, countB = 0, countC = 0, countD = 0, countF = 0;
        BigDecimal totalScore = BigDecimal.ZERO;
        BigDecimal totalMax = BigDecimal.ZERO;

        for (Grade grade : grades) {
            BigDecimal score = grade.getScore();
            BigDecimal max = grade.getSubmission().getAssignment().getMaxScore();
            if (max != null && max.compareTo(BigDecimal.ZERO) > 0 && score != null) {
                BigDecimal pct = score.multiply(BigDecimal.valueOf(100)).divide(max, 2, RoundingMode.HALF_UP);
                if (pct.compareTo(BigDecimal.valueOf(90)) >= 0) countA++;
                else if (pct.compareTo(BigDecimal.valueOf(80)) >= 0) countB++;
                else if (pct.compareTo(BigDecimal.valueOf(70)) >= 0) countC++;
                else if (pct.compareTo(BigDecimal.valueOf(60)) >= 0) countD++;
                else countF++;
                totalScore = totalScore.add(score);
                totalMax = totalMax.add(max);
            }
        }

        dto.setGradeCountA(countA);
        dto.setGradeCountB(countB);
        dto.setGradeCountC(countC);
        dto.setGradeCountD(countD);
        dto.setGradeCountF(countF);

        if (totalMax.compareTo(BigDecimal.ZERO) > 0) {
            dto.setPlatformAvgAssignmentGrade(
                    totalScore.multiply(BigDecimal.valueOf(100))
                            .divide(totalMax, 1, RoundingMode.HALF_UP));
        } else {
            dto.setPlatformAvgAssignmentGrade(null);
        }

        // ── Facility status ───────────────────────────────────────────────
        dto.setFacilitiesActive((int) facilityRepository.findAll().stream()
                .filter(f -> "ACTIVE".equals(f.getStatus())).count());
        dto.setFacilitiesPlanned((int) facilityRepository.findAll().stream()
                .filter(f -> "PLANNED".equals(f.getStatus())).count());
        dto.setFacilitiesClosed((int) facilityRepository.findAll().stream()
                .filter(f -> "CLOSED".equals(f.getStatus())).count());

        return dto;
    }
}
