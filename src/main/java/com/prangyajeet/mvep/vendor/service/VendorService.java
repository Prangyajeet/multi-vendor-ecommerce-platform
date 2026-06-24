package com.prangyajeet.mvep.vendor.service;

import com.prangyajeet.mvep.user.entity.User;
import com.prangyajeet.mvep.user.repository.UserRepository;
import com.prangyajeet.mvep.vendor.dto.VendorRequestDTO;
import com.prangyajeet.mvep.vendor.dto.VendorResponseDTO;
import com.prangyajeet.mvep.vendor.entity.Vendor;
import com.prangyajeet.mvep.vendor.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;

    public VendorService(VendorRepository vendorRepository,
                         UserRepository userRepository) {
        this.vendorRepository = vendorRepository;
        this.userRepository = userRepository;
    }

    public VendorResponseDTO createVendor(VendorRequestDTO requestDTO) {

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Vendor vendor = new Vendor();

        vendor.setBusinessName(requestDTO.getBusinessName());
        vendor.setBusinessAddress(requestDTO.getBusinessAddress());
        vendor.setGstNumber(requestDTO.getGstNumber());
        vendor.setUser(user);

        Vendor savedVendor = vendorRepository.save(vendor);

        return mapToResponseDTO(savedVendor);
    }

    public List<VendorResponseDTO> getAllVendors() {

        return vendorRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private VendorResponseDTO mapToResponseDTO(Vendor vendor) {

        VendorResponseDTO responseDTO =
                new VendorResponseDTO();

        responseDTO.setId(vendor.getId());
        responseDTO.setBusinessName(vendor.getBusinessName());
        responseDTO.setBusinessAddress(vendor.getBusinessAddress());
        responseDTO.setGstNumber(vendor.getGstNumber());

        responseDTO.setUserId(
                vendor.getUser().getId()
        );

        responseDTO.setUserEmail(
                vendor.getUser().getEmail()
        );

        return responseDTO;
    }
}