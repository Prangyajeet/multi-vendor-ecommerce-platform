package com.prangyajeet.mvep.user.controller;

import com.prangyajeet.mvep.response.ApiResponse;
import com.prangyajeet.mvep.user.dto.CustomerProfileDTO;
import com.prangyajeet.mvep.user.entity.User;
import com.prangyajeet.mvep.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final UserService userService;

    public CustomerController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public ApiResponse<String> customerDashboard() {

        return new ApiResponse<>(
                true,
                "Customer dashboard loaded successfully.",
                "Welcome Customer"
        );
    }

    @GetMapping("/me")
    public ApiResponse<CustomerProfileDTO> me() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userService.getUserByEmail(email);

        CustomerProfileDTO profile = new CustomerProfileDTO();

        profile.setId(user.getId());
        profile.setFirstName(user.getFirstName());
        profile.setLastName(user.getLastName());
        profile.setEmail(user.getEmail());
        profile.setPhoneNumber(user.getPhoneNumber());
        profile.setRole(user.getRole().getName().name());

        return new ApiResponse<>(
                true,
                "Customer profile fetched successfully.",
                profile
        );
    }
}