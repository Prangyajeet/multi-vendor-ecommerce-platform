package com.prangyajeet.mvep.auth.service;

import com.prangyajeet.mvep.auth.dto.LoginRequest;
import com.prangyajeet.mvep.auth.dto.LoginResponse;
import com.prangyajeet.mvep.auth.dto.RegisterRequest;
import com.prangyajeet.mvep.auth.dto.RegisterResponse;
import com.prangyajeet.mvep.user.entity.Role;
import com.prangyajeet.mvep.user.entity.User;
import com.prangyajeet.mvep.user.repository.RoleRepository;
import com.prangyajeet.mvep.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserService userService,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isEmailAlreadyRegistered(String email) {
        return userService.emailExists(email);
    }

    public RegisterResponse register(RegisterRequest request) {

        if (userService.emailExists(request.getEmail())) {
            return new RegisterResponse("Email already exists");
        }

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        // Encrypt password before saving
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(role);

        userService.save(user);

        return new RegisterResponse("Registration successful");
    }

    public LoginResponse login(LoginRequest request) {

        Optional<User> userOptional = userService.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            return new LoginResponse("Invalid email or password");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new LoginResponse("Invalid email or password");
        }

        return new LoginResponse("Login successful");
    }
}