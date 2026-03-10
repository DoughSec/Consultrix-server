package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.User;
import com.consultrix.consultrixserver.repository.UserRepository;
import com.consultrix.consultrixserver.security.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    // registration for users
    public User register(
            String firstName, String lastName, String email, String password
    ) {
//        User user = new User();
//        user.setFirstName(firstName);
//        user.setLastName(lastName);
//        user.setEmail(email);
//        user.setPasswordHash(this.passwordEncoder.encode(password));
//        user.setRole("ROLE_USER");

//        return userRepository.save(user);
        return null;
    }

}
