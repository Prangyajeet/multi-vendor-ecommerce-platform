package com.prangyajeet.mvep.address.controller;

import com.prangyajeet.mvep.address.dto.AddressRequestDTO;
import com.prangyajeet.mvep.address.dto.AddressResponseDTO;
import com.prangyajeet.mvep.address.service.AddressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    // Create Address
    @PostMapping
    public AddressResponseDTO createAddress(
            @RequestBody AddressRequestDTO requestDTO) {
        return addressService.createAddress(requestDTO);
    }

    // Get All Addresses
    @GetMapping
    public List<AddressResponseDTO> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    // Get Address By Id
    @GetMapping("/{id}")
    public AddressResponseDTO getAddressById(
            @PathVariable Long id) {
        return addressService.getAddressById(id);
    }

    // Get Addresses By User
    @GetMapping("/user/{userId}")
    public List<AddressResponseDTO> getAddressesByUser(
            @PathVariable Long userId) {
        return addressService.getAddressesByUser(userId);
    }

    // Update Address
    @PutMapping("/{id}")
    public AddressResponseDTO updateAddress(
            @PathVariable Long id,
            @RequestBody AddressRequestDTO requestDTO) {

        return addressService.updateAddress(id, requestDTO);
    }

    // Delete Address
    @DeleteMapping("/{id}")
    public String deleteAddress(
            @PathVariable Long id) {

        return addressService.deleteAddress(id);
    }
}