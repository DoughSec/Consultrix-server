package com.consultrix.consultrixserver.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Prevent Spring Boot from auto-registering the JWT filter as a servlet filter.
    // It must only run inside the Spring Security filter chain.
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration(JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // public – anyone can register, login, or hit /me
                        .requestMatchers("/consultrix/auth/**").permitAll()
                        .requestMatchers("/error").permitAll()

                        // ADMIN-only endpoints
                        .requestMatchers("/consultrix/organizations/**").hasRole("ADMIN")
                        .requestMatchers("/consultrix/facilities/**").hasRole("ADMIN")
                        .requestMatchers("/consultrix/cohorts/**").hasRole("ADMIN")
                        .requestMatchers("/consultrix/users/**").hasRole("ADMIN")
                        .requestMatchers("/consultrix/instructors/**").hasRole("ADMIN")

                        // INSTRUCTOR or ADMIN
                        .requestMatchers("/consultrix/instructors/**").hasAnyRole("INSTRUCTOR", "ADMIN")
                        .requestMatchers("/consultrix/assignments/**").hasAnyRole("INSTRUCTOR", "ADMIN")
                        .requestMatchers("/consultrix/attendance/**").hasAnyRole("INSTRUCTOR", "ADMIN")
                        .requestMatchers("/consultrix/modules/**").hasAnyRole("INSTRUCTOR", "ADMIN")
                        .requestMatchers("/consultrix/grades/**").hasAnyRole("STUDENT", "INSTRUCTOR", "ADMIN")

                        // STUDENT, INSTRUCTOR, or ADMIN
                        .requestMatchers("/consultrix/students/**").hasAnyRole("STUDENT", "INSTRUCTOR", "ADMIN")
                        .requestMatchers("/consultrix/submissions/**").hasAnyRole("STUDENT", "INSTRUCTOR", "ADMIN")
                        .requestMatchers("/consultrix/notifications/**").hasAnyRole("STUDENT", "INSTRUCTOR", "ADMIN")

                        // everything else requires authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}