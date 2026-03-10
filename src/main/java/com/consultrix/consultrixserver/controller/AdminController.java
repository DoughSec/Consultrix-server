package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.Admin;
import com.consultrix.consultrixserver.model.dto.adminDTO.AdminRequestDto;
import com.consultrix.consultrixserver.model.dto.adminDTO.AdminResponseDto;
import com.consultrix.consultrixserver.security.SecurityUtils;
import com.consultrix.consultrixserver.service.AdminService;
import com.consultrix.consultrixserver.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultrix/admin")
public class AdminController {
    private final AdminService adminService;
    private final AuthenticationService authService;

    public AdminController(AdminService adminService, AuthenticationService authService) {
        this.adminService = adminService;
        this.authService = authService;
    }

    //create Admin record
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdminResponseDto create(@RequestBody AdminRequestDto request) {
        return adminService.create(
                request.getUserId(),
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getStatus(),
                request.getAdminLevel()
                );
    }

    //get all Admin records
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Admin> getAll() {
        return adminService.getAll();
    }

    //get Admin by id
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Admin getAdminById(@PathVariable("userId") Integer userId) {
        return adminService.getById(userId);
    }

    //update Admin record
    @PutMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public AdminResponseDto updateAdmin(@RequestBody AdminRequestDto adminRequestDto) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return adminService.update(currentUserId.intValue(), adminRequestDto);
    }

    //delete Admin record
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteAdmin(@PathVariable("id") Integer id) {
//        adminService.delete(id);
//    }

}