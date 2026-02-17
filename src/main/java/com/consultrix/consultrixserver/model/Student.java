package com.consultrix.consultrixserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Data //replaces getter and setter
@Entity
@NoArgsConstructor
@Table(name="students") //no need for SQL query to create table, just need to run the application and it will create the table for us
public class Student {

    @Id
    @Column(name = "user_id")
    private int userId;

    @Column(name = "cohort_id")
    private int cohortId;

    @Column(name = "graduation_status")
    private String graduationStatus;

    @Column(name = "pipeline_stage")
    private String pipeLineStage;

    @Column(name = "interview_stage")
    private String interviewStage;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "placement_start_date")
    private LocalDate placementStartDate;

    @Column(name = "resume_url")
    private String resumeUrl;

    @Column(name = "notes")
    private String notes;

}