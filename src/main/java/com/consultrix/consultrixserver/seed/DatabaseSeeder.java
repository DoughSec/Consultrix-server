package com.consultrix.consultrixserver.seed;

import com.consultrix.consultrixserver.model.*;
import com.consultrix.consultrixserver.model.Module;
import com.consultrix.consultrixserver.model.StudentFlag;
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
    private final StudentFlagRepository studentFlagRepository;
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
            StudentFlagRepository studentFlagRepository,
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
        this.studentFlagRepository = studentFlagRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!seedEnabled || userRepository.count() > 0) {
            return;
        }

        // ── Organization ─────────────────────────────────────────────────────────
        Organization org = new Organization();
        org.setName("Consultrix Academy");
        org.setType("PEOPLESHORES");
        org.setContactEmail("info@consultrix.com");
        organizationRepository.save(org);

        // ── Admin ─────────────────────────────────────────────────────────────────
        User admin = new User();
        admin.setOrganization(org);
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setEmail("admin@email.com");
        admin.setPasswordHash(passwordEncoder.encode("password"));
        admin.setRole("ROLE_ADMIN");
        admin.setStatus("ACTIVE");
        userRepository.save(admin);

        // ── Instructors ───────────────────────────────────────────────────────────
        Instructor taylor  = createInstructor(org, "Taylor",  "Morgan",  "instructor@email.com",  "Lead Instructor",   "Full Stack Development", "Mon/Wed 2-4pm");
        Instructor jordan  = createInstructor(org, "Jordan",  "Lee",     "instructor2@email.com", "Senior Instructor", "Data Engineering",       "Tue/Thu 3-5pm");
        Instructor alex    = createInstructor(org, "Alex",    "Chen",    "instructor3@email.com", "Instructor",        "Cloud & DevOps",          "Mon/Fri 1-3pm");
        Instructor sam     = createInstructor(org, "Sam",     "Rivera",  "instructor4@email.com", "Instructor",        "Java/Backend",            "Wed/Fri 2-4pm");
        Instructor morgan  = createInstructor(org, "Morgan",  "Kim",     "instructor5@email.com", "Instructor",        "Frontend/React",          "Tue/Thu 1-3pm");

        // ── Facilities ────────────────────────────────────────────────────────────
        Facility nyc      = createFacility(org, "NYC Campus",          "160 Liberty St",   "New York",     "NY", "USA", 30, "ACTIVE",  LocalDate.of(2024, 1,  1), LocalDate.of(2027, 12, 31));
        Facility philly   = createFacility(org, "Philadelphia Campus", "123 Market St",    "Philadelphia", "PA", "USA", 25, "ACTIVE",  LocalDate.of(2024, 1,  1), LocalDate.of(2026, 12, 31));
        Facility atlanta  = createFacility(org, "Atlanta Campus",      "100 Peachtree St", "Atlanta",      "GA", "USA", 20, "ACTIVE",  LocalDate.of(2024, 6,  1), LocalDate.of(2027,  5, 31));
        Facility dallas   = createFacility(org, "Dallas Campus",       "400 Commerce St",  "Dallas",       "TX", "USA", 25, "PLANNED", LocalDate.of(2025, 7,  1), LocalDate.of(2028,  6, 30));

        // ── Cohorts ───────────────────────────────────────────────────────────────
        LocalDate spring2025Start = LocalDate.of(2025, 1, 13);
        LocalDate spring2025End   = LocalDate.of(2025, 6,  6);
        LocalDate summer2025Start = LocalDate.of(2025, 6, 23);
        LocalDate summer2025End   = LocalDate.of(2025, 11, 14);

        Cohort nycSpring   = createCohort(nyc,     taylor, "NYC Spring 2025", spring2025Start, spring2025End,          20, "ACTIVE");
        Cohort nycSummer   = createCohort(nyc,     jordan, "NYC Summer 2025", summer2025Start, summer2025End,          20, "RECRUITING");
        Cohort phiSpring   = createCohort(philly,  alex,   "PHI Spring 2025", spring2025Start, spring2025End,          18, "ACTIVE");
        Cohort phiFall     = createCohort(philly,  sam,    "PHI Fall 2024",   LocalDate.of(2024,  8, 26), LocalDate.of(2025, 1, 10), 18, "COMPLETED");
        Cohort atlSpring   = createCohort(atlanta, morgan, "ATL Spring 2025", spring2025Start, spring2025End,          16, "ACTIVE");
        Cohort atlWinter   = createCohort(atlanta, taylor, "ATL Winter 2024", LocalDate.of(2024, 10,  7), LocalDate.of(2025, 3, 14), 16, "COMPLETED");
        Cohort dalSummer   = createCohort(dallas,  jordan, "DAL Summer 2025", summer2025Start, summer2025End,          20, "INTERVIEWING");
        Cohort dalSpring   = createCohort(dallas,  sam,    "DAL Spring 2025", spring2025Start, spring2025End,          20, "ARCHIVED");

        // ── Curriculum for active cohorts (15 weeks) ──────────────────────────────
        List<Assignment> nycAssignments = createCurriculum(nycSpring, spring2025Start);
        List<Assignment> phiAssignments = createCurriculum(phiSpring, spring2025Start);
        List<Assignment> atlAssignments = createCurriculum(atlSpring, spring2025Start);

        // ── Curriculum for completed cohorts (6-week abbreviated) ─────────────────
        List<Assignment> phiFallAssignments    = createAbbreviatedCurriculum(phiFall,  LocalDate.of(2024,  8, 26));
        List<Assignment> atlWinterAssignments  = createAbbreviatedCurriculum(atlWinter, LocalDate.of(2024, 10,  7));

        // ── Students — NYC Spring 2025 (10 students) ──────────────────────────────
        String[][] nycData = {
                {"Alex",   "Rivera",   "student@email.com",  "GOOD"},
                {"Jordan", "Lee",      "student1@email.com", "GOOD"},
                {"Sam",    "Patel",    "student2@email.com", "GOOD"},
                {"Taylor", "Nguyen",   "student3@email.com", "AVERAGE"},
                {"Morgan", "Kim",      "student4@email.com", "AVERAGE"},
                {"Riley",  "Smith",    "student5@email.com", "AVERAGE"},
                {"Casey",  "Johnson",  "student6@email.com", "AVERAGE"},
                {"Avery",  "Brown",    "student7@email.com", "ATRISK"},
                {"Quinn",  "Davis",    "student8@email.com", "ATRISK"},
                {"Parker", "Miller",   "student9@email.com", "ATRISK"},
        };
        List<Student> nycStudents = createActiveStudents(org, nycSpring, nycData);

        // ── Students — PHI Spring 2025 (8 students) ───────────────────────────────
        String[][] phiData = {
                {"Jamie",   "Wilson",   "student10@email.com", "GOOD"},
                {"Drew",    "Garcia",   "student11@email.com", "GOOD"},
                {"Reese",   "Martinez", "student12@email.com", "AVERAGE"},
                {"Skyler",  "Anderson", "student13@email.com", "AVERAGE"},
                {"Dakota",  "Thomas",   "student14@email.com", "AVERAGE"},
                {"Blake",   "Jackson",  "student15@email.com", "ATRISK"},
                {"Cameron", "White",    "student16@email.com", "ATRISK"},
                {"Finley",  "Harris",   "student17@email.com", "ATRISK"},
        };
        List<Student> phiStudents = createActiveStudents(org, phiSpring, phiData);

        // ── Students — ATL Spring 2025 (8 students) ───────────────────────────────
        String[][] atlData = {
                {"Jordan", "Clark",   "student18@email.com", "GOOD"},
                {"Avery",  "Lewis",   "student19@email.com", "GOOD"},
                {"Morgan", "Walker",  "student20@email.com", "AVERAGE"},
                {"Taylor", "Hall",    "student21@email.com", "AVERAGE"},
                {"Casey",  "Allen",   "student22@email.com", "AVERAGE"},
                {"Riley",  "Young",   "student23@email.com", "ATRISK"},
                {"Quinn",  "King",    "student24@email.com", "ATRISK"},
                {"Alex",   "Wright",  "student25@email.com", "GOOD"},
        };
        List<Student> atlStudents = createActiveStudents(org, atlSpring, atlData);

        // ── Students — PHI Fall 2024 completed cohort (6 students) ───────────────
        String[][] phiFallData = {
                {"Maria",   "Santos",  "student26@email.com"},
                {"James",   "Chen",    "student27@email.com"},
                {"Priya",   "Patel",   "student28@email.com"},
                {"Michael", "Torres",  "student29@email.com"},
                {"Sarah",   "Kim",     "student30@email.com"},
                {"David",   "Johnson", "student31@email.com"},
        };
        // Placement clients for first 3 PHI Fall students
        String[] phiFallClients = {"Accenture", "Cigna", "IBM"};
        List<Student> phiFallStudents = createCompletedStudents(org, phiFall, phiFallData,
                phiFallClients, LocalDate.of(2025, 2, 10));

        // ── Students — ATL Winter 2024 completed cohort (5 students) ─────────────
        String[][] atlWinterData = {
                {"Emily",  "Davis",    "student32@email.com"},
                {"Marcus", "Brown",    "student33@email.com"},
                {"Sofia",  "Garcia",   "student34@email.com"},
                {"Lucas",  "Wilson",   "student35@email.com"},
                {"Aria",   "Martinez", "student36@email.com"},
        };
        // Placement clients for first 3 ATL Winter students
        String[] atlWinterClients = {"JPMorgan Chase", "UnitedHealth Group", "Accenture"};
        List<Student> atlWinterStudents = createCompletedStudents(org, atlWinter, atlWinterData,
                atlWinterClients, LocalDate.of(2025, 4, 14));

        // ── Submissions & Grades — Active cohorts ─────────────────────────────────
        // Only weeks 1-10 have past-due assignments (first 20 assignments).
        // Week 11 (assignments 20-21) is upcoming — no submissions yet.
        createActiveSubmissionsAndGrades(nycStudents, nycData, nycAssignments, taylor);
        createActiveSubmissionsAndGrades(phiStudents, phiData, phiAssignments, alex);
        createActiveSubmissionsAndGrades(atlStudents, atlData, atlAssignments, morgan);

        // ── Submissions & Grades — Completed cohorts ──────────────────────────────
        createCompletedSubmissionsAndGrades(phiFallStudents,   phiFallAssignments,   sam);
        createCompletedSubmissionsAndGrades(atlWinterStudents, atlWinterAssignments, taylor);

        // ── Attendance — Active cohorts (weeks 1-10 = 50 school days) ────────────
        createActiveAttendance(nycStudents, nycData, nycSpring, spring2025Start);
        createActiveAttendance(phiStudents, phiData, phiSpring, spring2025Start);
        createActiveAttendance(atlStudents, atlData, atlSpring, spring2025Start);

        // ── Attendance — Completed cohorts (3 weeks for variety) ─────────────────
        createCompletedAttendance(phiFallStudents,   phiFall,   LocalDate.of(2024,  8, 26));
        createCompletedAttendance(atlWinterStudents, atlWinter, LocalDate.of(2024, 10,  7));

        // ── Student Flags ─────────────────────────────────────────────────────────
        // Find the three at-risk students to flag by their email addresses
        Student averyBrown  = nycStudents.stream().filter(s -> s.getEmail().equals("student7@email.com")).findFirst().orElseThrow();
        Student blakeJackson = phiStudents.stream().filter(s -> s.getEmail().equals("student15@email.com")).findFirst().orElseThrow();
        Student rileyYoung  = atlStudents.stream().filter(s -> s.getEmail().equals("student23@email.com")).findFirst().orElseThrow();

        createFlag(averyBrown,  taylor, "Consistently late on assignments. At risk of failing module 10.", "HIGH");
        createFlag(blakeJackson, alex,  "Missed 3 consecutive classes. Needs attendance intervention.", "MEDIUM");
        createFlag(rileyYoung,  morgan, "Low quiz scores across Java and OOP modules.", "LOW");
    }

    // ── Helper: create and save an Instructor ────────────────────────────────────
    private Instructor createInstructor(Organization org, String firstName, String lastName,
                                        String email, String title, String specialty, String officeHours) {
        Instructor i = new Instructor();
        i.setOrganization(org);
        i.setFirstName(firstName);
        i.setLastName(lastName);
        i.setEmail(email);
        i.setPasswordHash(passwordEncoder.encode("password"));
        i.setRole("ROLE_INSTRUCTOR");
        i.setStatus("ACTIVE");
        i.setTitle(title);
        i.setSpecialty(specialty);
        i.setOfficeHours(officeHours);
        return instructorRepository.save(i);
    }

    // ── Helper: create and save a Facility ───────────────────────────────────────
    private Facility createFacility(Organization org, String name, String address, String city,
                                    String state, String country, int capacity, String status,
                                    LocalDate leaseStart, LocalDate leaseEnd) {
        Facility f = new Facility();
        f.setOrganization(org);
        f.setName(name);
        f.setAddress_line1(address);
        f.setCity(city);
        f.setState(state);
        f.setCountry(country);
        f.setCapacity(capacity);
        f.setStatus(status);
        f.setLeaseStartDate(leaseStart);
        f.setLeaseEndDate(leaseEnd);
        return facilityRepository.save(f);
    }

    // ── Helper: create and save a Cohort ─────────────────────────────────────────
    private Cohort createCohort(Facility facility, Instructor instructor, String name,
                                LocalDate start, LocalDate end, int capacity, String status) {
        Cohort c = new Cohort();
        c.setFacility(facility);
        c.setPrimaryInstructor(instructor);
        c.setName(name);
        c.setStartDate(start);
        c.setEndDate(end);
        c.setCapacity(capacity);
        c.setStatus(status);
        return cohortRepository.save(c);
    }

    // ── Helper: create the full 15-week curriculum for an active cohort ───────────
    // Returns the flat list of all saved Assignment objects in week order.
    private List<Assignment> createCurriculum(Cohort cohort, LocalDate cohortStart) {
        // Each entry: {title, weekOffset (start), weekOffset (end)}
        // Modules run Mon–Fri; end week determines assignment due dates.
        record ModuleDef(String title, int startWeek, int endWeek, List<String> assignmentTitles) {}

        List<ModuleDef> modules = List.of(
                new ModuleDef("Java Fundamentals",      0,  1, List.of("Hello World Exercises",        "Variables & Control Flow Lab")),
                new ModuleDef("OOP Principles",         1,  2, List.of("Class Design Challenge",        "Inheritance & Polymorphism Project")),
                new ModuleDef("Data Structures",        2,  3, List.of("Array & LinkedList Lab",        "Stack & Queue Implementation")),
                new ModuleDef("Algorithms & Complexity",3,  4, List.of("Sorting Algorithm Comparison",  "Big-O Analysis Report")),
                new ModuleDef("Database Fundamentals",  4,  5, List.of("ER Diagram Design",             "SQL Basics Lab")),
                new ModuleDef("SQL & JPA",              5,  6, List.of("JPA Entity Mapping",            "JPQL Query Lab")),
                new ModuleDef("Spring Boot Basics",     6,  7, List.of("First Spring App",              "Dependency Injection Lab")),
                new ModuleDef("REST API Design",        7,  8, List.of("CRUD API Build",                "API Documentation Project")),
                new ModuleDef("Security & Authentication", 8, 9, List.of("JWT Auth Implementation",    "Role-Based Access Lab")),
                new ModuleDef("Frontend HTML/CSS",      9, 10, List.of("Portfolio Page Build",          "Responsive Layout Lab")),
                new ModuleDef("JavaScript Essentials", 10, 11, List.of("DOM Manipulation Lab",          "Async JavaScript Project")),
                new ModuleDef("React Fundamentals",    11, 12, List.of("React Component Library",       "State Management Lab")),
                new ModuleDef("Full Stack Integration",12, 13, List.of("Full Stack Feature Sprint",     "Integration Testing Lab")),
                new ModuleDef("Testing & QA",          13, 14, List.of("Unit Test Suite",               "End-to-End Test Coverage")),
                new ModuleDef("Capstone Project",      14, 16, List.of("Capstone Proposal",             "Capstone MVP Demo", "Final Presentation"))
        );

        List<Assignment> allAssignments = new ArrayList<>();
        int orderIndex = 1;
        for (ModuleDef def : modules) {
            LocalDate modStart = cohortStart.plusWeeks(def.startWeek());
            LocalDate modEnd   = cohortStart.plusWeeks(def.endWeek()).minusDays(1); // Friday of end week

            Module mod = new Module();
            mod.setCohort(cohort);
            mod.setTitle(def.title());
            mod.setDescription("Module " + orderIndex + ": " + def.title());
            mod.setStartDate(modStart);
            mod.setEndDate(modEnd);
            mod.setOrderIndex(orderIndex++);
            moduleRepository.save(mod);

            // Due dates: Tuesday and Friday of the module's end week
            LocalDate endWeekMonday = cohortStart.plusWeeks(def.endWeek());
            List<Assignment> modAssignments = createAssignments(mod, endWeekMonday, def.assignmentTitles());
            allAssignments.addAll(modAssignments);
        }
        return allAssignments;
    }

    // ── Helper: create the 6-week abbreviated curriculum for completed cohorts ────
    // Uses the first 6 module definitions only.
    private List<Assignment> createAbbreviatedCurriculum(Cohort cohort, LocalDate cohortStart) {
        record ModuleDef(String title, int startWeek, int endWeek, List<String> assignmentTitles) {}

        List<ModuleDef> modules = List.of(
                new ModuleDef("Java Fundamentals",      0, 1, List.of("Hello World Exercises",       "Variables & Control Flow Lab")),
                new ModuleDef("OOP Principles",         1, 2, List.of("Class Design Challenge",       "Inheritance & Polymorphism Project")),
                new ModuleDef("Data Structures",        2, 3, List.of("Array & LinkedList Lab",       "Stack & Queue Implementation")),
                new ModuleDef("Algorithms & Complexity",3, 4, List.of("Sorting Algorithm Comparison", "Big-O Analysis Report")),
                new ModuleDef("Database Fundamentals",  4, 5, List.of("ER Diagram Design",            "SQL Basics Lab")),
                new ModuleDef("SQL & JPA",              5, 6, List.of("JPA Entity Mapping",           "JPQL Query Lab"))
        );

        List<Assignment> allAssignments = new ArrayList<>();
        int orderIndex = 1;
        for (ModuleDef def : modules) {
            LocalDate modStart = cohortStart.plusWeeks(def.startWeek());
            LocalDate modEnd   = cohortStart.plusWeeks(def.endWeek()).minusDays(1);

            Module mod = new Module();
            mod.setCohort(cohort);
            mod.setTitle(def.title());
            mod.setDescription("Module " + orderIndex + ": " + def.title());
            mod.setStartDate(modStart);
            mod.setEndDate(modEnd);
            mod.setOrderIndex(orderIndex++);
            moduleRepository.save(mod);

            LocalDate endWeekMonday = cohortStart.plusWeeks(def.endWeek());
            List<Assignment> modAssignments = createAssignments(mod, endWeekMonday, def.assignmentTitles());
            allAssignments.addAll(modAssignments);
        }
        return allAssignments;
    }

    // ── Helper: create and save assignments for one module ────────────────────────
    // Due dates: Tuesday (index 1) and Friday (index 4) of the end-week Monday.
    private List<Assignment> createAssignments(Module mod, LocalDate weekMonday, List<String> titles) {
        List<LocalDate> dueDates = List.of(
                weekMonday.plusDays(1),  // Tuesday
                weekMonday.plusDays(4),  // Friday
                weekMonday.plusDays(4)   // Friday (for 3rd assignment in Capstone)
        );

        List<Assignment> saved = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            LocalDate due = dueDates.get(Math.min(i, dueDates.size() - 1));
            Assignment a = new Assignment();
            a.setModule(mod);
            a.setTitle(titles.get(i));
            a.setDescription(titles.get(i) + " — submit via course portal.");
            a.setDueDate(due);
            a.setDueTime(due.atTime(17, 0));
            a.setMaxScore(new BigDecimal("100.00"));
            saved.add(assignmentRepository.save(a));
        }
        return saved;
    }

    // ── Helper: create active-cohort students with performance profiles ────────────
    private List<Student> createActiveStudents(Organization org, Cohort cohort, String[][] data) {
        List<Student> students = new ArrayList<>();
        for (String[] row : data) {
            String firstName = row[0];
            String lastName  = row[1];
            String email     = row[2];
            String profile   = row[3];

            Student s = new Student();
            s.setOrganization(org);
            s.setFirstName(firstName);
            s.setLastName(lastName);
            s.setEmail(email);
            s.setPasswordHash(passwordEncoder.encode("password"));
            s.setRole("ROLE_STUDENT");
            s.setStatus("ACTIVE");
            s.setCohort(cohort);
            s.setGraduationStatus("ACTIVE");

            switch (profile) {
                case "GOOD" -> {
                    s.setPipelineStage("IN_PROGRESS");
                    s.setInterviewStage("SCREEN");
                }
                case "AVERAGE" -> {
                    s.setPipelineStage("IN_PROGRESS");
                    s.setInterviewStage("NONE");
                }
                case "ATRISK" -> {
                    s.setPipelineStage("NOT_STARTED");
                    s.setInterviewStage("NONE");
                    s.setNotes("Struggling with coursework. Requires additional support.");
                }
            }

            students.add(studentRepository.save(s));
        }
        return students;
    }

    // ── Helper: create completed-cohort students with placement data ───────────────
    private List<Student> createCompletedStudents(Organization org, Cohort cohort,
                                                   String[][] data, String[] clients,
                                                   LocalDate placementStart) {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            Student s = new Student();
            s.setOrganization(org);
            s.setFirstName(data[i][0]);
            s.setLastName(data[i][1]);
            s.setEmail(data[i][2]);
            s.setPasswordHash(passwordEncoder.encode("password"));
            s.setRole("ROLE_STUDENT");
            s.setStatus("ACTIVE");
            s.setCohort(cohort);
            s.setGraduationStatus("COMPLETED");
            s.setPipelineStage("COMPLETED");
            s.setInterviewStage("FINAL");

            // First 3 students get a client placement
            if (i < clients.length) {
                s.setClientName(clients[i]);
                s.setPlacementStartDate(placementStart);
            }

            students.add(studentRepository.save(s));
        }
        return students;
    }

    // ── Helper: create submissions and grades for active-cohort students ──────────
    // Weeks 1-10 = assignments index 0-19 (past due).
    // Week 11 = assignments index 20-21 (upcoming, no submissions).
    // Weeks 12-15 = no submissions.
    private void createActiveSubmissionsAndGrades(List<Student> students, String[][] data,
                                                   List<Assignment> assignments, Instructor gradingInstructor) {
        // Past-due assignments: first 20 (index 0 to 19)
        int pastDueCount = Math.min(20, assignments.size());

        for (int si = 0; si < students.size(); si++) {
            Student student = students.get(si);
            String profile = data[si][3];

            List<Submission> submissions = new ArrayList<>();

            for (int ai = 0; ai < pastDueCount; ai++) {
                Assignment assignment = assignments.get(ai);
                String status = resolveSubmissionStatus(profile, ai);

                // MISSING means no submission record at all
                if (status == null) {
                    continue;
                }

                // submittedAt = Friday 4pm of that assignment's due week
                LocalDateTime submittedAt = assignment.getDueDate().atTime(16, 0);
                if ("LATE".equals(status)) {
                    // Submitted a day after due date to mark it late
                    submittedAt = assignment.getDueDate().plusDays(1).atTime(9, 0);
                }

                Submission sub = new Submission();
                sub.setAssignment(assignment);
                sub.setStudent(student);
                sub.setSubmittedAt(submittedAt);
                sub.setContentUrl("https://github.com/" + student.getFirstName().toLowerCase() + "/assignment-" + (ai + 1));
                sub.setStatus(status);
                submissions.add(submissionRepository.save(sub));
            }

            // Create grades for all submissions
            for (int gi = 0; gi < submissions.size(); gi++) {
                Submission sub = submissions.get(gi);
                Grade grade = new Grade();
                grade.setSubmission(sub);
                grade.setInstructor(gradingInstructor);
                grade.setScore(resolveScore(profile, gi));
                grade.setFeedback(resolveFeedback(profile));
                grade.setGradedAt(sub.getSubmittedAt().plusDays(2));
                gradeRepository.save(grade);
            }
        }
    }

    // ── Helper: determine submission status by profile and assignment index ────────
    // Returns null to indicate MISSING (no submission record created).
    private String resolveSubmissionStatus(String profile, int assignmentIndex) {
        return switch (profile) {
            // GOOD: all 20 submitted on time
            case "GOOD" -> "SUBMITTED";
            // AVERAGE: 1-14 SUBMITTED, 15-18 LATE, 19-20 missing
            case "AVERAGE" -> {
                if (assignmentIndex < 14)  yield "SUBMITTED";
                if (assignmentIndex < 18)  yield "LATE";
                yield null; // MISSING
            }
            // ATRISK: 1-8 alternating, 9-12 LATE, 13+ missing
            case "ATRISK" -> {
                if (assignmentIndex < 8)   yield assignmentIndex % 2 == 0 ? "SUBMITTED" : "LATE";
                if (assignmentIndex < 12)  yield "LATE";
                yield null; // MISSING
            }
            default -> null;
        };
    }

    // ── Helper: calculate grade score by profile and submission index ──────────────
    private BigDecimal resolveScore(String profile, int index) {
        int score = switch (profile) {
            case "GOOD"    -> 88 + (index % 8);
            case "AVERAGE" -> 72 + (index % 10);
            case "ATRISK"  -> 58 + (index % 12);
            default        -> 70;
        };
        return BigDecimal.valueOf(score);
    }

    // ── Helper: grade feedback message by performance profile ─────────────────────
    private String resolveFeedback(String profile) {
        return switch (profile) {
            case "GOOD"    -> "Excellent work! Demonstrates strong understanding of the material.";
            case "AVERAGE" -> "Satisfactory submission. Review the feedback sections for improvement areas.";
            case "ATRISK"  -> "Needs significant improvement. Please attend office hours for additional help.";
            default        -> "Submission received.";
        };
    }

    // ── Helper: create submissions and grades for completed-cohort students ────────
    // All 12 assignments (6 modules × 2) are submitted as SUBMITTED and graded.
    private void createCompletedSubmissionsAndGrades(List<Student> students,
                                                      List<Assignment> assignments,
                                                      Instructor gradingInstructor) {
        for (Student student : students) {
            for (int ai = 0; ai < assignments.size(); ai++) {
                Assignment assignment = assignments.get(ai);
                LocalDateTime submittedAt = assignment.getDueDate().atTime(15, 30);

                Submission sub = new Submission();
                sub.setAssignment(assignment);
                sub.setStudent(student);
                sub.setSubmittedAt(submittedAt);
                sub.setContentUrl("https://github.com/" + student.getFirstName().toLowerCase() + "/assignment-" + (ai + 1));
                sub.setStatus("SUBMITTED");
                submissionRepository.save(sub);

                Grade grade = new Grade();
                grade.setSubmission(sub);
                grade.setInstructor(gradingInstructor);
                grade.setScore(BigDecimal.valueOf(80 + (ai % 15)));
                grade.setFeedback("Good work completing this module assignment.");
                grade.setGradedAt(submittedAt.plusDays(2));
                gradeRepository.save(grade);
            }
        }
    }

    // ── Helper: create attendance for active-cohort students (weeks 1-10, Mon-Fri) ─
    private void createActiveAttendance(List<Student> students, String[][] data,
                                        Cohort cohort, LocalDate cohortStart) {
        // 10 weeks × 5 days = 50 school days per student
        List<LocalDate> schoolDays = new ArrayList<>();
        for (int week = 0; week < 10; week++) {
            LocalDate monday = cohortStart.plusWeeks(week);
            for (int day = 0; day < 5; day++) {
                schoolDays.add(monday.plusDays(day));
            }
        }

        for (int si = 0; si < students.size(); si++) {
            Student student = students.get(si);
            String profile = data[si][3];

            for (int di = 0; di < schoolDays.size(); di++) {
                LocalDate date = schoolDays.get(di);
                String status = resolveAttendanceStatus(profile, di);

                Attendance attendance = new Attendance();
                attendance.setCohort(cohort);
                attendance.setStudent(student);
                attendance.setAttendanceDate(date);
                attendance.setStatus(status);
                attendance.setNote(resolveAttendanceNote(status));
                attendanceRepository.save(attendance);
            }
        }
    }

    // ── Helper: resolve attendance status by profile and day index ────────────────
    private String resolveAttendanceStatus(String profile, int dayIndex) {
        return switch (profile) {
            // GOOD: 95% present, 5% late (every 20th day)
            case "GOOD"    -> dayIndex % 20 == 19 ? "LATE" : "PRESENT";
            // AVERAGE: 85% present, 10% absent (every 10th), 5% late (every 20th)
            case "AVERAGE" -> {
                if (dayIndex % 10 == 9)  yield "ABSENT";
                if (dayIndex % 20 == 14) yield "LATE";
                yield "PRESENT";
            }
            // ATRISK: 70% present, 20% absent (every 5th), 10% late (every 10th)
            case "ATRISK"  -> {
                if (dayIndex % 5 == 4)   yield "ABSENT";
                if (dayIndex % 10 == 2)  yield "LATE";
                yield "PRESENT";
            }
            default -> "PRESENT";
        };
    }

    // ── Helper: note text for non-PRESENT attendance records ─────────────────────
    private String resolveAttendanceNote(String status) {
        return switch (status) {
            case "ABSENT" -> "Absent without notification";
            case "LATE"   -> "Arrived late";
            default       -> null;
        };
    }

    // ── Helper: create attendance for completed cohorts (3 weeks only) ────────────
    private void createCompletedAttendance(List<Student> students, Cohort cohort, LocalDate cohortStart) {
        for (int week = 0; week < 3; week++) {
            LocalDate monday = cohortStart.plusWeeks(week);
            for (int day = 0; day < 5; day++) {
                LocalDate date = monday.plusDays(day);
                for (int si = 0; si < students.size(); si++) {
                    Student student = students.get(si);
                    // Simple pattern: most students present, occasional late
                    String status = (si % 4 == 0 && day == 4) ? "LATE" : "PRESENT";
                    Attendance attendance = new Attendance();
                    attendance.setCohort(cohort);
                    attendance.setStudent(student);
                    attendance.setAttendanceDate(date);
                    attendance.setStatus(status);
                    attendance.setNote("LATE".equals(status) ? "Arrived late" : null);
                    attendanceRepository.save(attendance);
                }
            }
        }
    }

    // ── Helper: create and save a StudentFlag ─────────────────────────────────────
    private void createFlag(Student student, Instructor instructor, String reason, String priority) {
        StudentFlag flag = new StudentFlag();
        flag.setStudent(student);
        flag.setInstructor(instructor);
        flag.setReason(reason);
        flag.setPriority(priority);
        flag.setResolved(false);
        studentFlagRepository.save(flag);
    }

}
