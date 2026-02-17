package com.consultrix.consultrixserver.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
@Table(name = "instructors")
public class Instructor {
    @Id
    @Column(name="user_id")
    private int userId;

    @Column(name = "title")
    private String title;

    @Column(name = "specialty")
    private String specialty;

    @Column(name = "office_hours")
    private String officeHours;

}