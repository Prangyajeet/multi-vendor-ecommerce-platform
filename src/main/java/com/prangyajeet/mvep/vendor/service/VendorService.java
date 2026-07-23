package com.prangyajeet.mvep.vendor.service;

import com.prangyajeet.mvep.common.enums.NotificationType;
import com.prangyajeet.mvep.notification.service.NotificationService;
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
    private final NotificationService notificationService;

    public VendorService(VendorRepository vendorRepository,
                         UserRepository userRepository,
                         NotificationService notificationService) {

        this.vendorRepository = vendorRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    // ==========================
    // CREATE VENDOR
    // ==========================

    public VendorResponseDTO createVendor(VendorRequestDTO requestDTO) {

        if (requestDTO.getUserId() == null) {
            throw new RuntimeException("User Id is required");
        }

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (vendorRepository.findByUserId(user.getId()).isPresent()) {
            throw new RuntimeException("Vendor already exists for this user");
        }

        if (requestDTO.getGstNumber() != null &&
                vendorRepository.findByGstNumber(requestDTO.getGstNumber()).isPresent()) {

            throw new RuntimeException("GST Number already exists");
        }

        Vendor vendor = new Vendor();

        vendor.setBusinessName(requestDTO.getBusinessName());
        vendor.setBusinessAddress(requestDTO.getBusinessAddress());
        vendor.setGstNumber(requestDTO.getGstNumber());
        vendor.setUser(user);

        Vendor savedVendor = vendorRepository.save(vendor);

        /*
         * Notify Vendor
         */
        notificationService.sendVendorNotification(
                user,
                "Vendor Registration Successful",
                "Your vendor registration request has been submitted successfully. Please wait for admin approval.",
                NotificationType.VENDOR
        );

        /*
         * Notify Admins
         */
        notificationService.sendNotificationToAdmins(
                "Vendor Approval Pending",
                user.getFirstName() + " "
                        + user.getLastName()
                        + " has submitted a vendor registration request.",
                NotificationType.SYSTEM
        );

        return mapToResponseDTO(savedVendor);
    }

    // ==========================
    // GET ALL
    // ==========================

    public List<VendorResponseDTO> getAllVendors() {

        return vendorRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // ==========================
    // GET BY ID
    // ==========================

    public VendorResponseDTO getVendorById(Long id) {

        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Vendor not found"));

        return mapToResponseDTO(vendor);
    }

    // ==========================
    // UPDATE
    // ==========================

    public VendorResponseDTO updateVendor(Long id,
                                          VendorRequestDTO requestDTO) {

        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Vendor not found"));

        vendor.setBusinessName(requestDTO.getBusinessName());
        vendor.setBusinessAddress(requestDTO.getBusinessAddress());
        vendor.setGstNumber(requestDTO.getGstNumber());

        Vendor updatedVendor = vendorRepository.save(vendor);

        return mapToResponseDTO(updatedVendor);
    }

    // ==========================
    // DELETE
    // ==========================

    public void deleteVendor(Long id) {

        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Vendor not found"));

        vendorRepository.delete(vendor);
    }

    // ==========================
    // DTO MAPPER
    // ==========================

    private VendorResponseDTO mapToResponseDTO(Vendor vendor) {

        VendorResponseDTO responseDTO = new VendorResponseDTO();

        responseDTO.setId(vendor.getId());
        responseDTO.setBusinessName(vendor.getBusinessName());
        responseDTO.setBusinessAddress(vendor.getBusinessAddress());
        responseDTO.setGstNumber(vendor.getGstNumber());

        responseDTO.setUserId(vendor.getUser().getId());
        responseDTO.setUserEmail(vendor.getUser().getEmail());

        return responseDTO;
    }
}