package com.prangyajeet.mvep.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    @GetMapping("/api/customer/dashboard")
    public String customerDashboard() {
        return "Welcome Customer";
    }

    @GetMapping("/api/customer/me")
    public String me() {
        return "Customer Authenticated";
    }
}