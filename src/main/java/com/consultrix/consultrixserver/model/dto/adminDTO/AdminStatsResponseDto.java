package com.consultrix.consultrixserver.model.dto.adminDTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AdminStatsResponseDto {

    // ── Platform counts ───────────────────────────────────────────────────
    private int totalStudents;
    private int totalInstructors;
    private int totalCohorts;
    private int totalFacilities;
    private int totalModules;
    private int totalAssignments;
    private int totalSubmissions;
    private int totalGrades;

    // ── Student pipeline ─────────────────────────────────────────────────
    private int studentsNotStarted;
    private int studentsInProgress;
    private int studentsPlaced;

    // ── Graduation status ────────────────────────────────────────────────
    private int studentsGradActive;
    private int studentsGradCompleted;
    private int studentsGradWithdrawn;

    // ── Interview stage ──────────────────────────────────────────────────
    private int interviewNone;
    private int interviewScreen;
    private int interviewTechnical;
    private int interviewFinal;

    // ── Cohort status ────────────────────────────────────────────────────
    private int cohortsRecruiting;
    private int cohortsInterviewing;
    private int cohortsActive;
    private int cohortsCompleted;
    private int cohortsArchived;

    // ── Submission status ────────────────────────────────────────────────
    private int submissionsSubmitted;
    private int submissionsLate;
    private int submissionsMissing;
    private int submissionsGraded;
    private int submissionsUngraded;

    // ── Grade distribution (unique assignment-level grades) ───────────────
    private int gradeCountA;
    private int gradeCountB;
    private int gradeCountC;
    private int gradeCountD;
    private int gradeCountF;
    private BigDecimal platformAvgAssignmentGrade;

    // ── Facility status ──────────────────────────────────────────────────
    private int facilitiesActive;
    private int facilitiesPlanned;
    private int facilitiesClosed;
}
