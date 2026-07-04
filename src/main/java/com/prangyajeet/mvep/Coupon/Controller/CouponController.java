package com.prangyajeet.mvep.Coupon.Controller;

import com.prangyajeet.mvep.Coupon.dto.ApplyCouponRequestDTO;
import com.prangyajeet.mvep.Coupon.dto.ApplyCouponResponseDTO;
import com.prangyajeet.mvep.Coupon.dto.CouponRequestDTO;
import com.prangyajeet.mvep.Coupon.dto.CouponResponseDTO;
import com.prangyajeet.mvep.Coupon.service.CouponService;
import com.prangyajeet.mvep.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    // =========================
    // ADMIN APIs
    // =========================

    @PostMapping("/api/admin/coupons")
    public ApiResponse<CouponResponseDTO> createCoupon(
            @Valid @RequestBody CouponRequestDTO requestDTO) {

        CouponResponseDTO response =
                couponService.createCoupon(requestDTO);

        return new ApiResponse<>(
                true,
                "Coupon created successfully.",
                response
        );
    }

    @GetMapping("/api/admin/coupons")
    public ApiResponse<List<CouponResponseDTO>> getAllCoupons() {

        return new ApiResponse<>(
                true,
                "Coupons fetched successfully.",
                couponService.getAllCoupons()
        );
    }

    @GetMapping("/api/admin/coupons/{couponId}")
    public ApiResponse<CouponResponseDTO> getCouponById(
            @PathVariable Long couponId) {

        return new ApiResponse<>(
                true,
                "Coupon fetched successfully.",
                couponService.getCouponById(couponId)
        );
    }

    @PutMapping("/api/admin/coupons/{couponId}")
    public ApiResponse<CouponResponseDTO> updateCoupon(
            @PathVariable Long couponId,
            @Valid @RequestBody CouponRequestDTO requestDTO) {

        return new ApiResponse<>(
                true,
                "Coupon updated successfully.",
                couponService.updateCoupon(
                        couponId,
                        requestDTO
                )
        );
    }

    @DeleteMapping("/api/admin/coupons/{couponId}")
    public ApiResponse<Void> deleteCoupon(
            @PathVariable Long couponId) {

        couponService.deleteCoupon(couponId);

        return new ApiResponse<>(
                true,
                "Coupon deleted successfully.",
                null
        );
    }

    // =========================
    // CUSTOMER APIs
    // =========================

    @PostMapping("/api/customer/coupons/apply")
    public ApiResponse<ApplyCouponResponseDTO> applyCoupon(
            @Valid @RequestBody
            ApplyCouponRequestDTO requestDTO) {

        return new ApiResponse<>(
                true,
                "Coupon applied successfully.",
                couponService.applyCoupon(requestDTO)
        );
    }
}