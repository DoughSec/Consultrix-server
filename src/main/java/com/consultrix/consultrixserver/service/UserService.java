package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Organization;
import com.consultrix.consultrixserver.model.User;
import com.consultrix.consultrixserver.model.dto.userDTO.UserRequestDto;
import com.consultrix.consultrixserver.model.dto.userDTO.UserResponseDto;
import com.consultrix.consultrixserver.repository.OrganizationRepository;
import com.consultrix.consultrixserver.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, OrganizationRepository organizationRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // create User (registration)
    public UserResponseDto create(Integer organizationId, String firstName, String lastName, String email, String password, String status) {
        if (email == null) {
            throw new IllegalArgumentException("email is required");
        }

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        Organization organization = null;
        if (organizationId != null) {
            organization = organizationRepository.findById(organizationId)
                    .orElseThrow(() -> new IllegalArgumentException("Organization not found: " + organizationId));
        }

        User user = new User();
        user.setOrganization(organization);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPasswordHash(this.passwordEncoder.encode(password));
        user.setStatus(status != null ? status : "ACTIVE");
        user.setRole("ROLE_USER");

        userRepository.save(user);

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setUserId(user.getId());
        responseDto.setOrganizationId(user.getOrganization() != null ? user.getOrganization().getId() : null);
        responseDto.setFirstName(user.getFirstName());
        responseDto.setLastName(user.getLastName());
        responseDto.setEmail(user.getEmail());
        responseDto.setStatus(user.getStatus());

        return responseDto;
    }

    // registration for users (kept for backward compatibility)
    public User register(
            String firstName, String lastName, String email, String password
    ) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPasswordHash(this.passwordEncoder.encode(password));
        user.setRole("ROLE_USER");

        return userRepository.save(user);
    }

    // getAll
    @Transactional(readOnly = true)
    public List<User> listAll() {
        return userRepository.findAll();
    }

    // getById
    @Transactional(readOnly = true)
    public User getById(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is required");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }

    // update User
    public UserResponseDto update(Integer userId, UserRequestDto updated) {
        User existing = getById(userId);

        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setEmail(updated.getEmail());
        existing.setStatus(updated.getStatus());

        if (updated.getPasswordHash() != null && !updated.getPasswordHash().isBlank()) {
            existing.setPasswordHash(this.passwordEncoder.encode(updated.getPasswordHash()));
        }

        userRepository.save(existing);

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setUserId(existing.getId());
        responseDto.setOrganizationId(existing.getOrganization() != null ? existing.getOrganization().getId() : null);
        responseDto.setFirstName(existing.getFirstName());
        responseDto.setLastName(existing.getLastName());
        responseDto.setEmail(existing.getEmail());
        responseDto.setStatus(existing.getStatus());

        return responseDto;
    }

    // delete User
    public void delete(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is required");
        }
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        userRepository.deleteById(userId);
    }
}
