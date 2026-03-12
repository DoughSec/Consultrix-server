package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.User;
import com.consultrix.consultrixserver.model.dto.userDTO.UserRequestDto;
import com.consultrix.consultrixserver.model.dto.userDTO.UserResponseDto;
import com.consultrix.consultrixserver.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultrix/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //create User record
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserResponseDto create(@RequestBody UserRequestDto request) {
        return userService.create(
                request.getOrganizationId(),
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPasswordHash(),
                request.getStatus()
        );
    }

    //get all User records
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getAll() {
        return userService.listAll();
    }

    //get User by id
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User getUserById(@PathVariable("userId") Integer userId) {
        return userService.getById(userId);
    }

    //update User record
    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserResponseDto updateUser(@PathVariable("userId") Integer userId, @RequestBody UserRequestDto request) {
        return userService.update(userId, request);
    }

    //delete User record
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUser(@PathVariable("userId") Integer userId) {
        userService.delete(userId);
    }

}

