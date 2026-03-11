package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Instructor;
import com.consultrix.consultrixserver.model.User;
import com.consultrix.consultrixserver.model.dto.instructorDTO.InstructorProfileRequestDto;
import com.consultrix.consultrixserver.model.dto.instructorDTO.InstructorProfileResponseDto;
import com.consultrix.consultrixserver.repository.InstructorRepository;
import com.consultrix.consultrixserver.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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

    //create
    public InstructorProfileResponseDto create(Integer userId, String firstName, String lastName, String email, String title, String specialty, String officeHours, String status) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
        if(instructorRepository.findByUser_Id(user.getId()) != null) {
            throw new IllegalArgumentException("Instructor already exists");
        }

        Instructor instructor = new Instructor();
        instructor.setFirstName(firstName);
        instructor.setLastName(lastName);
        instructor.setEmail(email);
        instructor.setOfficeHours(officeHours);
        instructor.setTitle(title);
        instructor.setSpecialty(specialty);
        instructor.setOfficeHours(officeHours);

        instructorRepository.save(instructor);

        InstructorProfileResponseDto instructorProfileResponseDto = new InstructorProfileResponseDto();
        instructorProfileResponseDto.setFirstName(firstName);
        instructorProfileResponseDto.setLastName(lastName);
        instructorProfileResponseDto.setEmail(email);
        instructorProfileResponseDto.setOfficeHours(officeHours);
        instructorProfileResponseDto.setSpecialty(specialty);
        instructorProfileResponseDto.setStatus(status);

        return instructorProfileResponseDto;
    }

    //getById
    public Instructor getById(Integer id) {
        return instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found")); //lambda function for exception
    }

    //getAll
    public List<Instructor> getAll() {
        return instructorRepository.findAll();
    }

    //update
    public InstructorProfileResponseDto update(Integer id, Integer userId, InstructorProfileRequestDto updated) {
        //get the already existing Instructor
        Instructor existingInstructor = getById(id);
        //verify user is either the instructor that's logged in or an admin
        if(!userId.equals(existingInstructor.getId())) {
            throw new IllegalArgumentException("User id does not match");
        }

        //set the things we want to update
        existingInstructor.setFirstName(updated.getFirstName());
        existingInstructor.setLastName(updated.getLastName());
        existingInstructor.setEmail(updated.getEmail());
        existingInstructor.setOfficeHours(updated.getOfficeHours());
        existingInstructor.setSpecialty(updated.getSpecialty());
        existingInstructor.setTitle(updated.getTitle());
        existingInstructor.setOfficeHours(updated.getOfficeHours());

        instructorRepository.save(existingInstructor);

        InstructorProfileResponseDto instructorProfileResponseDto = new InstructorProfileResponseDto();
        instructorProfileResponseDto.setFirstName(updated.getFirstName());
        instructorProfileResponseDto.setLastName(updated.getLastName());
        instructorProfileResponseDto.setEmail(updated.getEmail());
        instructorProfileResponseDto.setOfficeHours(updated.getOfficeHours());
        instructorProfileResponseDto.setSpecialty(updated.getSpecialty());
        instructorProfileResponseDto.setTitle(updated.getTitle());
        instructorProfileResponseDto.setStatus(updated.getStatus());

        return instructorProfileResponseDto;
    }

    //delete
    public void delete(Integer id) {
        instructorRepository.deleteById(id);
    }
}