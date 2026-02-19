package com.consultrix.consultrixserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.consultrix.consultrixserver.model.Module;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Integer> {
    List<Module> findByCohortIdOrderByOrderIndexAsc(Integer cohortId);
}
