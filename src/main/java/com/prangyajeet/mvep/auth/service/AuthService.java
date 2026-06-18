package com.prangyajeet.mvep.auth.service;

import com.prangyajeet.mvep.auth.dto.RegisterRequest;
import com.prangyajeet.mvep.auth.dto.RegisterResponse;
import com.prangyajeet.mvep.user.entity.Role;
import com.prangyajeet.mvep.user.entity.User;
import com.prangyajeet.mvep.user.repository.RoleRepository;
import com.prangyajeet.mvep.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public AuthService(UserService userService,
                       RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
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
        user.setPassword(request.getPassword());
        user.setRole(role);

        userService.save(user);

        return new RegisterResponse("Registration successful");
    }
}