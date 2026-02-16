package com.consultrix.consultrixserver.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
@Table(name = "admins")
public class Admin {
//    @Id
    @Column(name = "user_id")
    private int userId;

    @Column(name="admin_level")
    private String adminLevel;
}
