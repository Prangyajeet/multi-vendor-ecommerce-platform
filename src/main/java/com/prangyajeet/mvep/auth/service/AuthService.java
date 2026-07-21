package com.prangyajeet.mvep.auth.service;

import com.prangyajeet.mvep.auth.dto.LoginRequest;
import com.prangyajeet.mvep.auth.dto.LoginResponse;
import com.prangyajeet.mvep.auth.dto.RegisterRequest;
import com.prangyajeet.mvep.auth.dto.RegisterResponse;
import com.prangyajeet.mvep.common.enums.NotificationType;
import com.prangyajeet.mvep.notification.service.NotificationService;
import com.prangyajeet.mvep.security.JwtUtil;
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
    private final JwtUtil jwtUtil;
    private final NotificationService notificationService;

    public AuthService(UserService userService,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       NotificationService notificationService) {

        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.notificationService = notificationService;
    }

    public boolean isEmailAlreadyRegistered(String email) {
        return userService.emailExists(email);
    }

    public RegisterResponse register(RegisterRequest request) {

        if (userService.emailExists(request.getEmail())) {
            return new RegisterResponse("Email already exists");
        }

        if (userService.phoneNumberExists(request.getPhoneNumber())) {
            return new RegisterResponse("Phone number already exists");
        }

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() ->
                        new RuntimeException("Role not found"));

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(role);

        User savedUser = userService.save(user);

        /*
         * Welcome Notification
         */
        notificationService.sendWelcomeNotification(
                savedUser
        );

        /*
         * Notify Admins
         */
        notificationService.sendNotificationToAdmins(
                "New User Registration",
                savedUser.getFirstName() + " "
                        + savedUser.getLastName()
                        + " has registered as "
                        + savedUser.getRole().getName(),
                NotificationType.SYSTEM
        );

        return new RegisterResponse(
                "Registration successful"
        );
    }

    public LoginResponse login(LoginRequest request) {

        Optional<User> userOptional =
                userService.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {

            return new LoginResponse(
                    "Invalid email or password",
                    null,
                    null,
                    null,
                    null
            );
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            return new LoginResponse(
                    "Invalid email or password",
                    null,
                    null,
                    null,
                    null
            );
        }

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().getName().toString()
        );

        return new LoginResponse(
                "Login successful",
                token,
                user.getId(),
                user.getEmail(),
                user.getRole().getName().toString()
        );
    }
}