package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.Ticket;
import com.consultrix.consultrixserver.model.dto.ticketDTO.TicketRequestDto;
import com.consultrix.consultrixserver.model.dto.ticketDTO.TicketResponseDto;
import com.consultrix.consultrixserver.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultrix/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    //create Ticket record
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_STUDENT','ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public TicketResponseDto create(@RequestBody TicketRequestDto request) {
        return ticketService.create(
                request.getSubmittedByUserId(),
                request.getIssue()
        );
    }

    //get all Ticket records
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Ticket> getAll() {
        return ticketService.listAll();
    }

    //get Ticket by id
    @GetMapping("/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_STUDENT','ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public Ticket getTicketById(@PathVariable("ticketId") Integer ticketId) {
        return ticketService.getById(ticketId);
    }

    //get active (OPEN) Ticket records
    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Ticket> getActiveTickets() {
        return ticketService.listActive();
    }

    //get completed Ticket records
    @GetMapping("/complete")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Ticket> getCompleteTickets() {
        return ticketService.listComplete();
    }

    //get Ticket records by user
    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_STUDENT','ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<Ticket> getTicketsByUser(@PathVariable("userId") Integer userId) {
        return ticketService.listByUser(userId);
    }

    //mark Ticket as complete
    @PatchMapping("/{ticketId}/complete")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public TicketResponseDto markComplete(@PathVariable("ticketId") Integer ticketId) {
        return ticketService.markComplete(ticketId);
    }

    //delete Ticket record
    @DeleteMapping("/{ticketId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteTicket(@PathVariable("ticketId") Integer ticketId) {
        ticketService.delete(ticketId);
    }
}

