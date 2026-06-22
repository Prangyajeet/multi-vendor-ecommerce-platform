package com.prangyajeet.mvep.vendor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VendorController {

    @GetMapping("/api/vendor/dashboard")
    public String vendorDashboard() {
        return "Welcome Vendor";
    }
}