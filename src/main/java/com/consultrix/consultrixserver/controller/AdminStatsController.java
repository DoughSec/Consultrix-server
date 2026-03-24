package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.dto.adminDTO.AdminStatsResponseDto;
import com.consultrix.consultrixserver.service.AdminStatsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultrix/admin")
public class AdminStatsController {

    private final AdminStatsService adminStatsService;

    public AdminStatsController(AdminStatsService adminStatsService) {
        this.adminStatsService = adminStatsService;
    }

    // get platform-wide stats for admin CRM dashboard
    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AdminStatsResponseDto getStats() {
        return adminStatsService.getStats();
    }
}
