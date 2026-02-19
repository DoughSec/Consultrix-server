package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository <Admin, Integer> {
}
