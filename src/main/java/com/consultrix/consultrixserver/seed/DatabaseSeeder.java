package com.consultrix.consultrixserver.seed;

import com.consultrix.consultrixserver.model.*;
import com.consultrix.consultrixserver.model.Module;
import com.consultrix.consultrixserver.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseSeeder implements org.springframework.boot.CommandLineRunner {

    private final OrganizationRepository organizationRepository;
    private final FacilityRepository facilityRepository;
    private final CohortRepository cohortRepository;
    private final ModuleRepository moduleRepository;
    private final AssignmentRepository assignmentRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final SubmissionRepository submissionRepository;
    private final GradeRepository gradeRepository;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.enabled:true}")
    private boolean seedEnabled;

    public DatabaseSeeder(
            OrganizationRepository organizationRepository,
            FacilityRepository facilityRepository,
            CohortRepository cohortRepository,
            ModuleRepository moduleRepository,
            AssignmentRepository assignmentRepository,
            StudentRepository studentRepository,
            InstructorRepository instructorRepository,
            SubmissionRepository submissionRepository,
            GradeRepository gradeRepository,
            AttendanceRepository attendanceRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.organizationRepository = organizationRepository;
        this.facilityRepository = facilityRepository;
        this.cohortRepository = cohortRepository;
        this.moduleRepository = moduleRepository;
        this.assignmentRepository = assignmentRepository;
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
        this.submissionRepository = submissionRepository;
        this.gradeRepository = gradeRepository;
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!seedEnabled || userRepository.count() > 0) {
            return;
        }

        Organization org = new Organization();
        org.setName("Consultrix Academy");
        org.setType("PEOPLESHORES");
        org.setContactEmail("info@consultrix.com");
        organizationRepository.save(org);

        User admin = new User();
        admin.setOrganization(org);
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setEmail("admin@email.com");
        admin.setPasswordHash(passwordEncoder.encode("password"));
        admin.setRole("ROLE_ADMIN");
        admin.setStatus("ACTIVE");
        userRepository.save(admin);

        Instructor instructor = new Instructor();
        instructor.setOrganization(org);
        instructor.setFirstName("Taylor");
        instructor.setLastName("Morgan");
        instructor.setEmail("instructor@email.com");
        instructor.setPasswordHash(passwordEncoder.encode("password"));
        instructor.setRole("ROLE_INSTRUCTOR");
        instructor.setStatus("ACTIVE");
        instructor.setTitle("Lead Instructor");
        instructor.setSpecialty("Full Stack Development");
        instructor.setOfficeHours("Mon/Wed 2-4pm");
        instructorRepository.save(instructor);

        Facility facility = new Facility();
        facility.setOrganization(org);
        facility.setName("Downtown Campus");
        facility.setAddress_line1("123 Main St");
        facility.setCity("Philadelphia");
        facility.setState("PA");
        facility.setCountry("USA");
        facility.setCapacity(40);
        facility.setLeaseStartDate(LocalDate.of(2024, 1, 1));
        facility.setLeaseEndDate(LocalDate.of(2026, 12, 31));
        facility.setStatus("ACTIVE");
        facilityRepository.save(facility);

        Cohort cohortSpring = new Cohort();
        cohortSpring.setFacility(facility);
        cohortSpring.setPrimaryInstructor(instructor);
        cohortSpring.setName("Spring 2025 Cohort");
        cohortSpring.setStartDate(LocalDate.of(2025, 1, 13));
        cohortSpring.setEndDate(LocalDate.of(2025, 6, 6));
        cohortSpring.setCapacity(24);
        cohortSpring.setStatus("ACTIVE");
        cohortRepository.save(cohortSpring);

        Cohort cohortSummer = new Cohort();
        cohortSummer.setFacility(facility);
        cohortSummer.setPrimaryInstructor(instructor);
        cohortSummer.setName("Summer 2025 Cohort");
        cohortSummer.setStartDate(LocalDate.of(2025, 6, 23));
        cohortSummer.setEndDate(LocalDate.of(2025, 11, 14));
        cohortSummer.setCapacity(24);
        cohortSummer.setStatus("RECRUITING");
        cohortRepository.save(cohortSummer);

        Module moduleJava = new Module();
        moduleJava.setCohort(cohortSpring);
        moduleJava.setTitle("Java Foundations");
        moduleJava.setDescription("Core Java, OOP, and testing fundamentals.");
        moduleJava.setStartDate(LocalDate.of(2025, 1, 13));
        moduleJava.setEndDate(LocalDate.of(2025, 2, 21));
        moduleJava.setOrderIndex(1);
        moduleRepository.save(moduleJava);

        Module moduleWeb = new Module();
        moduleWeb.setCohort(cohortSpring);
        moduleWeb.setTitle("Web APIs");
        moduleWeb.setDescription("RESTful services with Spring Boot.");
        moduleWeb.setStartDate(LocalDate.of(2025, 2, 24));
        moduleWeb.setEndDate(LocalDate.of(2025, 4, 4));
        moduleWeb.setOrderIndex(2);
        moduleRepository.save(moduleWeb);

        Module moduleCapstone = new Module();
        moduleCapstone.setCohort(cohortSummer);
        moduleCapstone.setTitle("Capstone Prep");
        moduleCapstone.setDescription("Requirements, planning, and demos.");
        moduleCapstone.setStartDate(LocalDate.of(2025, 6, 23));
        moduleCapstone.setEndDate(LocalDate.of(2025, 7, 18));
        moduleCapstone.setOrderIndex(1);
        moduleRepository.save(moduleCapstone);

        Assignment assignment1 = new Assignment();
        assignment1.setModule(moduleJava);
        assignment1.setTitle("Java Basics Lab");
        assignment1.setDescription("Implement core Java exercises and unit tests.");
        assignment1.setDueDate(LocalDate.of(2025, 1, 24));
        assignment1.setDueTime(LocalDateTime.of(2025, 1, 24, 17, 0));
        assignment1.setMaxScore(new BigDecimal("100.00"));
        assignmentRepository.save(assignment1);

        Assignment assignment2 = new Assignment();
        assignment2.setModule(moduleWeb);
        assignment2.setTitle("REST API Project");
        assignment2.setDescription("Build and document a Spring Boot API.");
        assignment2.setDueDate(LocalDate.of(2025, 3, 14));
        assignment2.setDueTime(LocalDateTime.of(2025, 3, 14, 17, 0));
        assignment2.setMaxScore(new BigDecimal("100.00"));
        assignmentRepository.save(assignment2);

        Assignment assignment3 = new Assignment();
        assignment3.setModule(moduleCapstone);
        assignment3.setTitle("Capstone Proposal");
        assignment3.setDescription("Submit a capstone proposal and roadmap.");
        assignment3.setDueDate(LocalDate.of(2025, 7, 3));
        assignment3.setDueTime(LocalDateTime.of(2025, 7, 3, 17, 0));
        assignment3.setMaxScore(new BigDecimal("50.00"));
        assignmentRepository.save(assignment3);

        List<Student> springStudents = new ArrayList<>();
        List<Student> summerStudents = new ArrayList<>();

        String[][] studentData = {
                {"Alex", "Rivera", "student@email.com"},
                {"Jordan", "Lee", "student1@email.com"},
                {"Sam", "Patel", "student2@email.com"},
                {"Taylor", "Nguyen", "student3@email.com"},
                {"Morgan", "Kim", "student4@email.com"},
                {"Riley", "Smith", "student5@email.com"},
                {"Casey", "Johnson", "student6@email.com"},
                {"Avery", "Brown", "student7@email.com"},
                {"Quinn", "Davis", "student8@email.com"},
                {"Parker", "Miller", "student9@email.com"},
                {"Jamie", "Wilson", "student10@email.com"},
                {"Drew", "Garcia", "student11@email.com"}
        };

        String[] graduationStatuses = {"ACTIVE", "ACTIVE", "ACTIVE", "ACTIVE", "COMPLETED", "WITHDRAWN"};
        String[] pipelineStages = {"NOT_STARTED", "IN_PROGRESS", "COMPLETED"};
        String[] interviewStages = {"NONE", "SCREEN", "TECHNICAL", "FINAL"};

        for (int i = 0; i < studentData.length; i++) {
            Student student = new Student();
            student.setOrganization(org);
            student.setFirstName(studentData[i][0]);
            student.setLastName(studentData[i][1]);
            student.setEmail(studentData[i][2]);
            student.setPasswordHash(passwordEncoder.encode("password"));
            student.setRole("ROLE_STUDENT");
            student.setStatus("ACTIVE");
            student.setGraduationStatus(graduationStatuses[i % graduationStatuses.length]);
            student.setPipelineStage(pipelineStages[i % pipelineStages.length]);
            student.setInterviewStage(interviewStages[i % interviewStages.length]);
            student.setResumeUrl("https://example.com/resumes/" + studentData[i][0].toLowerCase() + ".pdf");

            if (i % 3 == 0) {
                student.setClientName("ClientCo");
                student.setPlacementStartDate(LocalDate.of(2025, 5, 1));
            }

            if (i < 8) {
                student.setCohort(cohortSpring);
                springStudents.add(student);
            } else {
                student.setCohort(cohortSummer);
                summerStudents.add(student);
            }
        }

        studentRepository.saveAll(springStudents);
        studentRepository.saveAll(summerStudents);

        List<Submission> submissions = new ArrayList<>();
        for (int i = 0; i < springStudents.size(); i++) {
            Student student = springStudents.get(i);
            if (i < 6) {
                Submission submissionJava = new Submission();
                submissionJava.setAssignment(assignment1);
                submissionJava.setStudent(student);
                submissionJava.setSubmittedAt(LocalDateTime.of(2025, 1, 24, 15, 30));
                submissionJava.setContentUrl("https://github.com/" + student.getFirstName().toLowerCase() + "/java-basics");
                submissionJava.setStatus(i % 2 == 0 ? "SUBMITTED" : "LATE");
                submissions.add(submissionJava);

                Submission submissionApi = new Submission();
                submissionApi.setAssignment(assignment2);
                submissionApi.setStudent(student);
                submissionApi.setSubmittedAt(LocalDateTime.of(2025, 3, 14, 16, 45));
                submissionApi.setContentUrl("https://github.com/" + student.getFirstName().toLowerCase() + "/rest-api");
                submissionApi.setStatus("SUBMITTED");
                submissions.add(submissionApi);
            }
        }

        for (int i = 0; i < summerStudents.size(); i++) {
            Student student = summerStudents.get(i);
            if (i < 4) {
                Submission submissionProposal = new Submission();
                submissionProposal.setAssignment(assignment3);
                submissionProposal.setStudent(student);
                submissionProposal.setSubmittedAt(LocalDateTime.of(2025, 7, 2, 14, 0));
                submissionProposal.setContentUrl("https://example.com/capstone/" + student.getFirstName().toLowerCase());
                submissionProposal.setStatus("SUBMITTED");
                submissions.add(submissionProposal);
            }
        }

        submissionRepository.saveAll(submissions);

        List<Grade> grades = new ArrayList<>();
        for (int i = 0; i < submissions.size(); i++) {
            Submission submission = submissions.get(i);
            Grade grade = new Grade();
            grade.setSubmission(submission);
            grade.setInstructor(instructor);
            grade.setScore(BigDecimal.valueOf(82 + (i % 6) * 3));
            grade.setFeedback("Solid work with clear structure and documentation.");
            grades.add(grade);
        }
        gradeRepository.saveAll(grades);

        List<Attendance> attendanceRecords = new ArrayList<>();
        for (int i = 0; i < springStudents.size(); i++) {
            Student student = springStudents.get(i);
            Attendance attendance = new Attendance();
            attendance.setCohort(cohortSpring);
            attendance.setStudent(student);
            attendance.setAttendanceDate(LocalDate.of(2025, 1, 14));
            attendance.setStatus(i % 5 == 0 ? "LATE" : "PRESENT");
            attendance.setNote(i % 5 == 0 ? "Traffic delay" : null);
            attendanceRecords.add(attendance);
        }
        attendanceRepository.saveAll(attendanceRecords);
    }
}
