package com.prangyajeet.mvep.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @GetMapping("/api/admin/dashboard")
    public String adminDashboard() {
        return "Welcome Admin";
    }
}