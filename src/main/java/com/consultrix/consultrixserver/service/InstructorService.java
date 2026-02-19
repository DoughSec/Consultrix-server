package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Instructor;
import com.consultrix.consultrixserver.repository.InstructorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class InstructorService {
    private final InstructorRepository InstructorRepository;

    public InstructorService(InstructorRepository InstructorRepository) {
        this.InstructorRepository = InstructorRepository;
    }

    //create
    public Instructor create(Instructor instructor) {
        if (instructor == null || instructor.getId() == null) {
            throw new IllegalArgumentException("Instructor and its id must not be null");
        }
        if(InstructorRepository.existsById(instructor.getId())) {
            throw new IllegalArgumentException("Instructor already exists");
        }
        return InstructorRepository.save(instructor);
    }

    //getById
    public Instructor getById(Integer id) {
        return InstructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found")); //lambda function for exception
    }

    //getAll
    public List<Instructor> getAll() {
        return InstructorRepository.findAll();
    }

    //update
    public Instructor update(Integer id, Instructor updated) {
        //get the already existing Instructor
        Instructor existingInstructor = getById(id);

        //set the things we want to update
        existingInstructor.setSpecialty(updated.getSpecialty());
        existingInstructor.setTitle(updated.getTitle());
        existingInstructor.setOfficeHours(updated.getOfficeHours());

        return InstructorRepository.save(existingInstructor);
    }

    //delete
    public void delete(Integer id) {
        InstructorRepository.deleteById(id);
    }
}