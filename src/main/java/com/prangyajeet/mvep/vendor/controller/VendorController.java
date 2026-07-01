package com.prangyajeet.mvep.vendor.controller;

import com.prangyajeet.mvep.vendor.dto.VendorRequestDTO;
import com.prangyajeet.mvep.vendor.dto.VendorResponseDTO;
import com.prangyajeet.mvep.vendor.service.VendorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@CrossOrigin(origins = "*")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @PostMapping
    public ResponseEntity<VendorResponseDTO> createVendor(
            @RequestBody VendorRequestDTO requestDTO) {

        VendorResponseDTO response =
                vendorService.createVendor(requestDTO);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VendorResponseDTO>> getAllVendors() {

        return ResponseEntity.ok(
                vendorService.getAllVendors()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendorResponseDTO> getVendorById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                vendorService.getVendorById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendorResponseDTO> updateVendor(
            @PathVariable Long id,
            @RequestBody VendorRequestDTO requestDTO) {

        return ResponseEntity.ok(
                vendorService.updateVendor(id, requestDTO)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVendor(
            @PathVariable Long id) {

        vendorService.deleteVendor(id);

        return ResponseEntity.ok(
                "Vendor deleted successfully"
        );
    }
}