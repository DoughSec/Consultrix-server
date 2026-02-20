package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.Admin;
import com.consultrix.consultrixserver.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultrix/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

//    //create Admin record
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public Admin create(@RequestBody Admin request) {
//        return adminService.create(
//                request.getAdminLevel()
//        );
//    }

    //get all Admin records
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Admin> getAll() {
        return adminService.getAll();
    }

    //get Admin by id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Admin getAdminById(@PathVariable("id") Integer id) {
        return adminService.getById(id);
    }

    //update Admin record
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Admin updateAdmin(@PathVariable("id") Integer id, @RequestBody Admin admin) {
        return adminService.update(id, admin);
    }

    //delete Admin record
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdmin(@PathVariable("id") Integer id) {
        adminService.delete(id);
    }

}