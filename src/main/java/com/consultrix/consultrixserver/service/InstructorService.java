package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Instructor;
import com.consultrix.consultrixserver.model.User;
import com.consultrix.consultrixserver.model.dto.instructorDTO.InstructorProfileRequestDto;
import com.consultrix.consultrixserver.model.dto.instructorDTO.InstructorProfileResponseDto;
import com.consultrix.consultrixserver.repository.InstructorRepository;
import com.consultrix.consultrixserver.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository;

    public InstructorService(InstructorRepository instructorRepository, UserRepository userRepository) {
        this.instructorRepository = instructorRepository;
        this.userRepository = userRepository;
    }

    // create
    public InstructorProfileResponseDto create(Integer userId, String firstName, String lastName, String email, String title, String specialty, String officeHours, String status) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is required");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        if (instructorRepository.findById(user.getId()).orElse(null) != null) {
            throw new IllegalArgumentException("Instructor already exists");
        }

        Instructor instructor = new Instructor();
        instructor.setFirstName(firstName);
        instructor.setLastName(lastName);
        instructor.setEmail(email);
        instructor.setTitle(title);
        instructor.setSpecialty(specialty);
        instructor.setOfficeHours(officeHours);

        instructorRepository.save(instructor);

        InstructorProfileResponseDto responseDto = new InstructorProfileResponseDto();
        responseDto.setUserId(instructor.getId());
        responseDto.setFirstName(instructor.getFirstName());
        responseDto.setLastName(instructor.getLastName());
        responseDto.setEmail(instructor.getEmail());
        responseDto.setTitle(instructor.getTitle());
        responseDto.setSpecialty(instructor.getSpecialty());
        responseDto.setOfficeHours(instructor.getOfficeHours());
        responseDto.setStatus(status);

        return responseDto;
    }

    // getById
    @Transactional(readOnly = true)
    public Instructor getById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("instructorId is required");
        }
        return instructorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found: " + id));
    }

    // getAll
    @Transactional(readOnly = true)
    public List<Instructor> getAll() {
        return instructorRepository.findAll();
    }

    // update
    public InstructorProfileResponseDto update(Integer id, Integer userId, InstructorProfileRequestDto updated) {
        Instructor existing = getById(id);

        // verify user is either the instructor that's logged in or an admin
        if (!userId.equals(existing.getId())) {
            throw new IllegalArgumentException("User id does not match");
        }

        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setEmail(updated.getEmail());
        existing.setTitle(updated.getTitle());
        existing.setSpecialty(updated.getSpecialty());
        existing.setOfficeHours(updated.getOfficeHours());

        instructorRepository.save(existing);

        InstructorProfileResponseDto responseDto = new InstructorProfileResponseDto();
        responseDto.setUserId(existing.getId());
        responseDto.setFirstName(existing.getFirstName());
        responseDto.setLastName(existing.getLastName());
        responseDto.setEmail(existing.getEmail());
        responseDto.setTitle(existing.getTitle());
        responseDto.setSpecialty(existing.getSpecialty());
        responseDto.setOfficeHours(existing.getOfficeHours());
        responseDto.setStatus(updated.getStatus());

        return responseDto;
    }

    // delete
    public void delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("instructorId is required");
        }
        if (!instructorRepository.existsById(id)) {
            throw new IllegalArgumentException("Instructor not found: " + id);
        }
        instructorRepository.deleteById(id);
    }
}