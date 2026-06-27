package com.prangyajeet.mvep.address.service;

import com.prangyajeet.mvep.address.dto.AddressRequestDTO;
import com.prangyajeet.mvep.address.dto.AddressResponseDTO;
import com.prangyajeet.mvep.address.entity.Address;
import com.prangyajeet.mvep.address.repository.AddressRepository;
import com.prangyajeet.mvep.user.entity.User;
import com.prangyajeet.mvep.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(AddressRepository addressRepository,
                          UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    // Create Address
    public AddressResponseDTO createAddress(AddressRequestDTO requestDTO) {

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address address = new Address();

        address.setFullName(requestDTO.getFullName());
        address.setPhoneNumber(requestDTO.getPhoneNumber());
        address.setAddressLine(requestDTO.getAddressLine());
        address.setCity(requestDTO.getCity());
        address.setState(requestDTO.getState());
        address.setCountry(requestDTO.getCountry());
        address.setPostalCode(requestDTO.getPostalCode());
        address.setUser(user);

        Address savedAddress = addressRepository.save(address);

        return mapToResponse(savedAddress);
    }

    // Get All Addresses
    public List<AddressResponseDTO> getAllAddresses() {

        return addressRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get Address By Id
    public AddressResponseDTO getAddressById(Long id) {

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        return mapToResponse(address);
    }

    // Get Addresses By User
    public List<AddressResponseDTO> getAddressesByUser(Long userId) {

        return addressRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Update Address
    public AddressResponseDTO updateAddress(Long id,
                                            AddressRequestDTO requestDTO) {

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        address.setFullName(requestDTO.getFullName());
        address.setPhoneNumber(requestDTO.getPhoneNumber());
        address.setAddressLine(requestDTO.getAddressLine());
        address.setCity(requestDTO.getCity());
        address.setState(requestDTO.getState());
        address.setCountry(requestDTO.getCountry());
        address.setPostalCode(requestDTO.getPostalCode());
        address.setUser(user);

        Address updatedAddress = addressRepository.save(address);

        return mapToResponse(updatedAddress);
    }

    // Delete Address
    public String deleteAddress(Long id) {

        if (!addressRepository.existsById(id)) {
            throw new RuntimeException("Address not found");
        }

        addressRepository.deleteById(id);

        return "Address deleted successfully";
    }

    // DTO Mapper
    private AddressResponseDTO mapToResponse(Address address) {

        AddressResponseDTO dto = new AddressResponseDTO();

        dto.setId(address.getId());
        dto.setFullName(address.getFullName());
        dto.setPhoneNumber(address.getPhoneNumber());
        dto.setAddressLine(address.getAddressLine());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setCountry(address.getCountry());
        dto.setPostalCode(address.getPostalCode());

        dto.setUserId(address.getUser().getId());
        dto.setUserEmail(address.getUser().getEmail());

        return dto;
    }
}