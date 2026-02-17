package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository <Admin, Long> {
}
