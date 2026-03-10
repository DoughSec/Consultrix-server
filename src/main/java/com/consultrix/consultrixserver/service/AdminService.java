package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Admin;
import com.consultrix.consultrixserver.model.User;
import com.consultrix.consultrixserver.model.dto.adminDTO.AdminRequestDto;
import com.consultrix.consultrixserver.model.dto.adminDTO.AdminResponseDto;
import com.consultrix.consultrixserver.repository.AdminRepository;
import com.consultrix.consultrixserver.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class AdminService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    public AdminService(AdminRepository adminRepository, UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    //create
    public AdminResponseDto create(Integer userId, String firstName, String lastName, String email, String status, String adminLevel) {
        if (userId == null) {
            throw new IllegalArgumentException("User id must not be null");
        }
        if(adminRepository.existsById(userId)) {
            throw new IllegalArgumentException("User is already an admin");
        }

//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        Admin admin  = new Admin();
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setEmail(email);
        admin.setAdminLevel(adminLevel);
        admin.setStatus(status);

        adminRepository.save(admin);

        AdminResponseDto adminResponseDto  = new AdminResponseDto();
        adminResponseDto.setUserId(userId);
        adminResponseDto.setFirstName(firstName);
        adminResponseDto.setLastName(lastName);
        adminResponseDto.setEmail(email);
        adminResponseDto.setAdminLevel(adminLevel);
        adminResponseDto.setStatus(status);

        return adminResponseDto;
    }

    //getById
    public Admin getById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Admin id must not be null");
        }

        return adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found")); //lamba function for exception
    }

    //getAll
    public List<Admin> getAll() {
        return adminRepository.findAll();
    }

    //update
    public AdminResponseDto update(Integer id, AdminRequestDto updated) {
        //get the already existing Admin
        Admin existingAdmin = getById(id);

        //set the things we want to update
        existingAdmin.setAdminLevel(updated.getAdminLevel());

        adminRepository.save(existingAdmin);

        AdminResponseDto adminResponseDto = new AdminResponseDto();
        adminResponseDto.setUserId(id);
        adminResponseDto.setFirstName(updated.getFirstName());
        adminResponseDto.setLastName(updated.getLastName());
        adminResponseDto.setEmail(updated.getEmail());
        adminResponseDto.setAdminLevel(updated.getAdminLevel());
        adminResponseDto.setStatus(updated.getStatus());

        return adminResponseDto;
    }

    //delete
    public void delete(Integer id) {
        adminRepository.deleteById(id);
    }
}