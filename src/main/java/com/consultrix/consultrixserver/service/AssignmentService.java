package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Assignment;
import com.consultrix.consultrixserver.repository.AssignmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;

    public AssignmentService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    //create
    public Assignment create(Assignment assignment) {
        List<Assignment> existing = assignmentRepository.findByModuleId(assignment.getModule().getId());
        if (existing != null && !existing.isEmpty()) {
            throw new IllegalArgumentException("Assignment already exists");
        }
        return assignmentRepository.save(assignment);
    }

    //getById
    public Assignment getById(Integer id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found")); //lamba function for exception
    }

    //getAll
    public List<Assignment> getAll() {
        return assignmentRepository.findAll();
    }

    //getAssignmentByModule
    public List<Assignment> getByModule(Integer moduleId) {
//        if (assignment == null || assignment.getModule() == null || assignment.getModule().getId() == null) {
//            throw new IllegalArgumentException("Assignment must include a module with an id");
//        }
//        Integer moduleId = assignment.getModule().getId();
        return assignmentRepository.findByModuleId(moduleId);
    }

    //update
    public Assignment update(Integer id, Assignment updated) {
        //get the already existing assignment
        Assignment existingAssignment = getById(id);

        //set the things we want to update
        existingAssignment.setMaxScore(updated.getMaxScore());
        existingAssignment.setDescription(updated.getDescription());
        existingAssignment.setUpdatedAt(updated.getUpdatedAt());

        return assignmentRepository.save(existingAssignment);
    }

    //delete
    public void delete(Integer id) {
        assignmentRepository.deleteById(id);
    }
}
