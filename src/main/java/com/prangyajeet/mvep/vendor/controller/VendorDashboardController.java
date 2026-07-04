package com.prangyajeet.mvep.vendor.controller;

import com.prangyajeet.mvep.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vendor")
public class VendorDashboardController {

    @GetMapping("/dashboard")
    public ApiResponse<String> vendorDashboard() {

        return new ApiResponse<>(
                true,
                "Vendor dashboard loaded successfully.",
                "Welcome Vendor"
        );
    }

    @GetMapping("/me")
    public ApiResponse<String> myProfile() {

        return new ApiResponse<>(
                true,
                "Vendor profile fetched successfully.",
                "Vendor Authenticated"
        );
    }
}