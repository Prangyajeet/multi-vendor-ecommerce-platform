package com.prangyajeet.mvep.user.controller;

import com.prangyajeet.mvep.response.ApiResponse;
import com.prangyajeet.mvep.user.dto.AdminProfileDTO;
import com.prangyajeet.mvep.user.entity.User;
import com.prangyajeet.mvep.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public ApiResponse<String> adminDashboard() {

        return new ApiResponse<>(
                true,
                "Admin dashboard loaded successfully.",
                "Welcome Admin"
        );
    }

    @GetMapping("/me")
    public ApiResponse<AdminProfileDTO> getMyProfile() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userService.getUserByEmail(email);

        AdminProfileDTO profile = new AdminProfileDTO();

        profile.setId(user.getId());
        profile.setFirstName(user.getFirstName());
        profile.setLastName(user.getLastName());
        profile.setEmail(user.getEmail());
        profile.setPhoneNumber(user.getPhoneNumber());
        profile.setRole(user.getRole().getName().name());

        return new ApiResponse<>(
                true,
                "Admin profile fetched successfully.",
                profile
        );
    }
}