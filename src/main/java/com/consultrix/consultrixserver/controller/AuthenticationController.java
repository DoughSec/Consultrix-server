package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.User;
import com.consultrix.consultrixserver.model.dto.authDTO.LoginRequestDto;
import com.consultrix.consultrixserver.model.dto.authDTO.LoginResponseDto;
import com.consultrix.consultrixserver.model.dto.authDTO.RegisterRequestDto;
import com.consultrix.consultrixserver.model.dto.userDTO.UserMeResponse;
import com.consultrix.consultrixserver.service.AuthenticationService;
import com.consultrix.consultrixserver.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/consultrix/auth")
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody RegisterRequestDto request) {
        return userService.register(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword()
        );
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDto login(@RequestBody LoginRequestDto request) {
        return authenticationService.login(request.getEmail(), request.getPassword());
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserMeResponse me(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authenticationService.extractBearerToken(authorizationHeader);
        return authenticationService.me(token);
    }

}
