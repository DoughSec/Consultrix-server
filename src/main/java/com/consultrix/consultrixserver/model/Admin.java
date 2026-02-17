package com.consultrix.consultrixserver.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "admins")
@DiscriminatorValue("ADMIN")
@PrimaryKeyJoinColumn(name = "user_id")
public class Admin extends User {

    @Column(name="admin_level", nullable = false)
    private String adminLevel = "PEOPLESHORES_ADMIN";
}
