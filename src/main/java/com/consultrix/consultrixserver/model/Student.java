package com.consultrix.consultrixserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="students")
@DiscriminatorValue("STUDENT")
@PrimaryKeyJoinColumn(name = "user_id") //this is the foreign key that references the user_id in the users table
public class Student extends User {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cohort_id")
    private Cohort cohort;

    // ACTIVE / COMPLETED / WITHDRAWN
    @Column(name = "graduation_status", nullable = false)
    private String graduationStatus = "ACTIVE";

    // NOT_STARTED / IN_PROGRESS / COMPLETED
    @Column(name = "pipeline_stage", nullable = false)
    private String pipeLineStage = "NOT_STARTED";

    // NONE / SCREEN / TECHNICAL / FINAL
    @Column(name = "interview_stage", nullable = false)
    private String interviewStage = "NONE";

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "placement_start_date")
    private LocalDate placementStartDate;

    @Column(name = "resume_url")
    private String resumeUrl;

    @Lob
    @Column(name = "notes")
    private String notes;

}