package com.prangyajeet.mvep.vendor;

import com.prangyajeet.mvep.vendor.dto.VendorRequestDTO;
import com.prangyajeet.mvep.vendor.dto.VendorResponseDTO;
import com.prangyajeet.mvep.vendor.service.VendorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @PostMapping
    public VendorResponseDTO createVendor(
            @RequestBody VendorRequestDTO requestDTO) {

        return vendorService.createVendor(requestDTO);
    }

    @GetMapping
    public List<VendorResponseDTO> getAllVendors() {

        return vendorService.getAllVendors();
    }
}