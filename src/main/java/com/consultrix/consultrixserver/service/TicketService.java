package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Ticket;
import com.consultrix.consultrixserver.model.User;
import com.consultrix.consultrixserver.model.dto.ticketDTO.TicketResponseDto;
import com.consultrix.consultrixserver.repository.TicketRepository;
import com.consultrix.consultrixserver.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TwilioSmsService twilioSmsService;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository, TwilioSmsService twilioSmsService) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.twilioSmsService = twilioSmsService;
    }

    // create Ticket
    public TicketResponseDto create(Integer submittedByUserId, String issue) {
        if (submittedByUserId == null) {
            throw new IllegalArgumentException("submittedByUserId is required");
        }
        if (issue == null || issue.isBlank()) {
            throw new IllegalArgumentException("issue is required");
        }

        User user = userRepository.findById(submittedByUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + submittedByUserId));

        Ticket ticket = new Ticket();
        ticket.setSubmittedBy(user);
        ticket.setIssue(issue);
        ticket.setStatus("OPEN");

        ticketRepository.save(ticket);

        // Send Twilio SMS notification
        String submitterName = user.getFirstName() + " " + user.getLastName();
        twilioSmsService.sendTicketNotification(ticket.getId(), issue, submitterName);

        return toResponseDto(ticket);
    }

    // getAll
    @Transactional(readOnly = true)
    public List<Ticket> listAll() {
        return ticketRepository.findAll();
    }

    // getById
    @Transactional(readOnly = true)
    public Ticket getById(Integer ticketId) {
        if (ticketId == null) {
            throw new IllegalArgumentException("ticketId is required");
        }
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + ticketId));
    }

    // get active (OPEN) tickets
    @Transactional(readOnly = true)
    public List<Ticket> listActive() {
        return ticketRepository.findByStatusOrderByCreatedAtDesc("OPEN");
    }

    // get completed tickets
    @Transactional(readOnly = true)
    public List<Ticket> listComplete() {
        return ticketRepository.findByStatusOrderByCreatedAtDesc("COMPLETE");
    }

    // get tickets by user
    @Transactional(readOnly = true)
    public List<Ticket> listByUser(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is required");
        }
        return ticketRepository.findBySubmittedByIdOrderByCreatedAtDesc(userId);
    }

    // mark Ticket as complete
    public TicketResponseDto markComplete(Integer ticketId) {
        Ticket existing = getById(ticketId);

        existing.setStatus("COMPLETE");
        ticketRepository.save(existing);

        return toResponseDto(existing);
    }

    // delete Ticket
    public void delete(Integer ticketId) {
        if (ticketId == null) {
            throw new IllegalArgumentException("ticketId is required");
        }
        if (!ticketRepository.existsById(ticketId)) {
            throw new IllegalArgumentException("Ticket not found: " + ticketId);
        }
        ticketRepository.deleteById(ticketId);
    }

    // helper to map entity to response DTO
    private TicketResponseDto toResponseDto(Ticket ticket) {
        TicketResponseDto dto = new TicketResponseDto();
        dto.setTicketId(ticket.getId());
        dto.setSubmittedByUserId(ticket.getSubmittedBy().getId());
        dto.setSubmittedByName(ticket.getSubmittedBy().getFirstName() + " " + ticket.getSubmittedBy().getLastName());
        dto.setIssue(ticket.getIssue());
        dto.setStatus(ticket.getStatus());
        dto.setCreatedAt(ticket.getCreatedAt());
        return dto;
    }
}

