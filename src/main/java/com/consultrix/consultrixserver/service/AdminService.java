package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Admin;
import com.consultrix.consultrixserver.repository.AdminRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class AdminService {
    private final AdminRepository AdminRepository;

    public AdminService(AdminRepository AdminRepository) {
        this.AdminRepository = AdminRepository;
    }

    //create
    public Admin create(Admin admin) {
        if (admin == null || admin.getId() == null) {
            throw new IllegalArgumentException("Admin and its id must not be null");
        }
        if(AdminRepository.existsById(admin.getId())) {
            throw new IllegalArgumentException("Admin already exists");
        }
        return AdminRepository.save(admin);
    }

    //getById
    public Admin getById(Integer id) {
        return AdminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found")); //lamba function for exception
    }

    //getAll
    public List<Admin> getAll() {
        return AdminRepository.findAll();
    }

    //update
    public Admin update(Integer id, Admin updated) {
        //get the already existing Admin
        Admin existingAdmin = getById(id);

        //set the things we want to update
        existingAdmin.setAdminLevel(updated.getAdminLevel());

        return AdminRepository.save(existingAdmin);
    }

    //delete
    public void delete(Integer id) {
        AdminRepository.deleteById(id);
    }
}