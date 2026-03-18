package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByStatusOrderByCreatedAtDesc(String status);
    List<Ticket> findBySubmittedByIdOrderByCreatedAtDesc(Integer userId);
}

