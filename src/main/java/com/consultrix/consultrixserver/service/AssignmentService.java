package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Module;
import com.consultrix.consultrixserver.model.Assignment;
import com.consultrix.consultrixserver.model.dto.assignmentDTO.AssignmentRequestDto;
import com.consultrix.consultrixserver.model.dto.assignmentDTO.AssignmentResponseDto;
import com.consultrix.consultrixserver.repository.AssignmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final ModuleService moduleService;

    public AssignmentService(AssignmentRepository assignmentRepository, ModuleService moduleService) {
        this.assignmentRepository = assignmentRepository;
        this.moduleService = moduleService;
    }

    //create
    public AssignmentResponseDto create(Integer moduleId, String title, String description, LocalDate dueDate, LocalDateTime dueTime, BigDecimal maxScore) {
        List<Assignment> existing = assignmentRepository.findByModuleId(moduleId);

        for(Assignment assignment : existing) {
            if (assignment.getTitle().equals(title)) {
                throw new IllegalArgumentException("Assignment already exists");
            }
        }

        Module module = moduleService.getById(moduleId);

        Assignment assignment = new Assignment();
        assignment.setModule(module);
        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setDueDate(dueDate);
        assignment.setDueTime(dueTime);
        assignment.setMaxScore(maxScore);

        assignmentRepository.save(assignment);

        AssignmentResponseDto assignmentResponseDto = new AssignmentResponseDto();
        assignmentResponseDto.setAssignmentId(assignment.getId());
        assignmentResponseDto.setModuleId(assignment.getModule().getId());
        assignmentResponseDto.setTitle(assignment.getTitle());
        assignmentResponseDto.setDescription(assignment.getDescription());
        assignmentResponseDto.setDueDate(assignment.getDueDate());
        assignmentResponseDto.setDueTime(assignment.getDueTime());
        assignmentResponseDto.setMaxScore(assignment.getMaxScore());

        return assignmentResponseDto;
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
        if (moduleId == null) {
            throw new IllegalArgumentException("moduleId cannot be null");
        }
        return assignmentRepository.findByModuleId(moduleId);
    }

    //update
    public AssignmentResponseDto update(Integer id, AssignmentRequestDto updated) {
        //get the already existing assignment
        Assignment existingAssignment = getById(id);

        //set the things we want to update
        existingAssignment.setTitle(updated.getTitle());
        existingAssignment.setDescription(updated.getDescription());
        existingAssignment.setDueDate(updated.getDueDate());
        existingAssignment.setDueTime(updated.getDueTime());
        existingAssignment.setMaxScore(updated.getMaxScore());

        assignmentRepository.save(existingAssignment);

        AssignmentResponseDto assignmentResponseDto = new AssignmentResponseDto();
        assignmentResponseDto.setAssignmentId(existingAssignment.getId());
        assignmentResponseDto.setModuleId(existingAssignment.getModule().getId());
        assignmentResponseDto.setTitle(existingAssignment.getTitle());
        assignmentResponseDto.setDescription(existingAssignment.getDescription());
        assignmentResponseDto.setDueDate(existingAssignment.getDueDate());
        assignmentResponseDto.setDueTime(existingAssignment.getDueTime());
        assignmentResponseDto.setMaxScore(existingAssignment.getMaxScore());

        return assignmentResponseDto;
    }

    //delete
    public void delete(Integer id) {
        assignmentRepository.deleteById(id);
    }
}
