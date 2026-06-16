package com.prangyajeet.mvep.auth.service;

import com.prangyajeet.mvep.user.service.UserService;
import org.springframework.stereotype.Service;
import com.prangyajeet.mvep.user.repository.RoleRepository;

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
}