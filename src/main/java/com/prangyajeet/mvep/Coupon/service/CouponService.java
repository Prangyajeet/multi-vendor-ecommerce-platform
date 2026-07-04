package com.prangyajeet.mvep.Coupon.service;

import com.prangyajeet.mvep.common.enums.DiscountType;
import com.prangyajeet.mvep.Coupon.dto.ApplyCouponRequestDTO;
import com.prangyajeet.mvep.Coupon.dto.ApplyCouponResponseDTO;
import com.prangyajeet.mvep.Coupon.dto.CouponRequestDTO;
import com.prangyajeet.mvep.Coupon.dto.CouponResponseDTO;
import com.prangyajeet.mvep.Coupon.entity.Coupon;
import com.prangyajeet.mvep.Coupon.repository.CouponRepository;
import com.prangyajeet.mvep.exception.CouponAlreadyExistsException;
import com.prangyajeet.mvep.exception.CouponExpiredException;
import com.prangyajeet.mvep.exception.CouponInactiveException;
import com.prangyajeet.mvep.exception.CouponNotFoundException;
import com.prangyajeet.mvep.exception.InvalidCouponException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public CouponResponseDTO createCoupon(
            CouponRequestDTO requestDTO) {

        if (couponRepository.existsByCode(requestDTO.getCode())) {

            throw new CouponAlreadyExistsException(
                    "Coupon already exists with code : "
                            + requestDTO.getCode()
            );
        }

        Coupon coupon = new Coupon();

        coupon.setCode(requestDTO.getCode().toUpperCase());
        coupon.setDescription(requestDTO.getDescription());
        coupon.setDiscountType(requestDTO.getDiscountType());
        coupon.setDiscountValue(requestDTO.getDiscountValue());
        coupon.setMinimumOrderAmount(
                requestDTO.getMinimumOrderAmount()
        );
        coupon.setMaximumDiscount(
                requestDTO.getMaximumDiscount()
        );
        coupon.setExpiryDate(
                requestDTO.getExpiryDate()
        );
        coupon.setUsageLimit(
                requestDTO.getUsageLimit()
        );
        coupon.setUsedCount(0);
        coupon.setActive(requestDTO.getActive());

        Coupon savedCoupon =
                couponRepository.save(coupon);

        return mapToResponseDTO(savedCoupon);
    }

    public List<CouponResponseDTO> getAllCoupons() {

        return couponRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public CouponResponseDTO getCouponById(
            Long couponId) {

        Coupon coupon =
                couponRepository.findById(couponId)
                        .orElseThrow(() ->
                                new CouponNotFoundException(
                                        "Coupon not found with id : "
                                                + couponId
                                ));

        return mapToResponseDTO(coupon);
    }    public CouponResponseDTO updateCoupon(
            Long couponId,
            CouponRequestDTO requestDTO) {

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() ->
                        new CouponNotFoundException(
                                "Coupon not found with id : "
                                        + couponId
                        ));

        coupon.setCode(requestDTO.getCode().toUpperCase());
        coupon.setDescription(requestDTO.getDescription());
        coupon.setDiscountType(requestDTO.getDiscountType());
        coupon.setDiscountValue(requestDTO.getDiscountValue());
        coupon.setMinimumOrderAmount(
                requestDTO.getMinimumOrderAmount()
        );
        coupon.setMaximumDiscount(
                requestDTO.getMaximumDiscount()
        );
        coupon.setExpiryDate(
                requestDTO.getExpiryDate()
        );
        coupon.setUsageLimit(
                requestDTO.getUsageLimit()
        );
        coupon.setActive(
                requestDTO.getActive()
        );

        Coupon updatedCoupon =
                couponRepository.save(coupon);

        return mapToResponseDTO(updatedCoupon);
    }

    public void deleteCoupon(
            Long couponId) {

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() ->
                        new CouponNotFoundException(
                                "Coupon not found with id : "
                                        + couponId
                        ));

        couponRepository.delete(coupon);
    }

    public ApplyCouponResponseDTO applyCoupon(
            ApplyCouponRequestDTO requestDTO) {

        Coupon coupon = couponRepository
                .findByCode(
                        requestDTO.getCouponCode().toUpperCase()
                )
                .orElseThrow(() ->
                        new CouponNotFoundException(
                                "Coupon not found."
                        ));

        if (!coupon.getActive()) {

            throw new CouponInactiveException(
                    "Coupon is inactive."
            );
        }

        if (coupon.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new CouponExpiredException(
                    "Coupon has expired."
            );
        }

        if (coupon.getUsedCount()
                >= coupon.getUsageLimit()) {

            throw new InvalidCouponException(
                    "Coupon usage limit exceeded."
            );
        }

        if (requestDTO.getOrderAmount()
                .compareTo(
                        coupon.getMinimumOrderAmount()
                ) < 0) {

            throw new InvalidCouponException(
                    "Minimum order amount is "
                            + coupon.getMinimumOrderAmount()
            );
        }

        BigDecimal discount =
                BigDecimal.ZERO;

        if (coupon.getDiscountType()
                == DiscountType.PERCENTAGE) {

            discount =
                    requestDTO.getOrderAmount()
                            .multiply(
                                    coupon.getDiscountValue()
                            )
                            .divide(
                                    BigDecimal.valueOf(100)
                            );

            if (coupon.getMaximumDiscount() != null &&
                    discount.compareTo(
                            coupon.getMaximumDiscount()
                    ) > 0) {

                discount =
                        coupon.getMaximumDiscount();
            }

        } else {

            discount =
                    coupon.getDiscountValue();
        }

        BigDecimal finalAmount =
                requestDTO.getOrderAmount()
                        .subtract(discount);

        coupon.setUsedCount(
                coupon.getUsedCount() + 1
        );

        couponRepository.save(coupon);

        ApplyCouponResponseDTO response =
                new ApplyCouponResponseDTO();

        response.setCouponCode(
                coupon.getCode()
        );

        response.setOriginalAmount(
                requestDTO.getOrderAmount()
        );

        response.setDiscountAmount(
                discount
        );

        response.setFinalAmount(
                finalAmount
        );

        response.setMessage(
                "Coupon applied successfully."
        );

        return response;
    }

    private CouponResponseDTO mapToResponseDTO(
            Coupon coupon) {

        CouponResponseDTO response =
                new CouponResponseDTO();

        response.setId(
                coupon.getId()
        );

        response.setCode(
                coupon.getCode()
        );

        response.setDescription(
                coupon.getDescription()
        );

        response.setDiscountType(
                coupon.getDiscountType()
        );

        response.setDiscountValue(
                coupon.getDiscountValue()
        );

        response.setMinimumOrderAmount(
                coupon.getMinimumOrderAmount()
        );

        response.setMaximumDiscount(
                coupon.getMaximumDiscount()
        );

        response.setExpiryDate(
                coupon.getExpiryDate()
        );

        response.setUsageLimit(
                coupon.getUsageLimit()
        );

        response.setUsedCount(
                coupon.getUsedCount()
        );

        response.setActive(
                coupon.getActive()
        );

        return response;
    }
}