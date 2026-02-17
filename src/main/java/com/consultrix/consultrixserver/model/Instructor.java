package com.consultrix.consultrixserver.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "instructors")
@DiscriminatorValue("INSTRUCTOR")
@PrimaryKeyJoinColumn(name = "user_id")
public class Instructor extends User {

    @Column(name = "title")
    private String title;

    @Column(name = "specialty")
    private String specialty;

    @Column(name = "office_hours")
    private String officeHours;

}