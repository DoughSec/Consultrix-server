package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Assignment;
import com.consultrix.consultrixserver.repository.AssignmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class AssignmentService {
    private final AssignmentRepository AssignmentRepository;

    public AssignmentService(AssignmentRepository AssignmentRepository) {
        this.AssignmentRepository = AssignmentRepository;
    }

    //create
    public Assignment create(Assignment assignment) {
        List<Assignment> existing = AssignmentRepository.findByModuleId(assignment.getModule().getId());
        if (existing != null && !existing.isEmpty()) {
            throw new IllegalArgumentException("Assignment already exists");
        }
        return AssignmentRepository.save(assignment);
    }

    //getById
    public Assignment getById(Integer id) {
        return AssignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found")); //lamba function for exception
    }

    //getAll
    public List<Assignment> getAll() {
        return AssignmentRepository.findAll();
    }
    //update
    public Assignment update(Integer id, Assignment updated) {
        //get the already existing assignment
        Assignment existingAssignment = getById(id);

        //set the things we want to update
        existingAssignment.setMaxScore(updated.getMaxScore());
        existingAssignment.setDescription(updated.getDescription());
        existingAssignment.setUpdatedAt(updated.getUpdatedAt());

        return AssignmentRepository.save(existingAssignment);
    }

    //delete
    public void delete(Integer id) {
        AssignmentRepository.deleteById(id);
    }
}
