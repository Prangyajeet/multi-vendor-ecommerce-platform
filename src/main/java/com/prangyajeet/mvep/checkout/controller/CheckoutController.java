package com.prangyajeet.mvep.checkout.controller;

import com.prangyajeet.mvep.checkout.dto.CheckoutRequestDTO;
import com.prangyajeet.mvep.checkout.dto.CheckoutResponseDTO;
import com.prangyajeet.mvep.checkout.service.CheckoutService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/checkout")
    public CheckoutResponseDTO checkout(
            @Valid @RequestBody CheckoutRequestDTO requestDTO) {

        return checkoutService.checkout(requestDTO);
    }
}