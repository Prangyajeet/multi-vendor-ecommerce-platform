package com.prangyajeet.mvep.admin.controller;

import com.prangyajeet.mvep.product.dto.ProductResponseDTO;
import com.prangyajeet.mvep.product.service.ProductService;
import com.prangyajeet.mvep.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ApiResponse<List<ProductResponseDTO>> getAllProducts() {

        return new ApiResponse<>(
                true,
                "All products fetched successfully",
                productService.getAllProductsForAdmin()
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponseDTO> getProductById(
            @PathVariable Long id) {

        return new ApiResponse<>(
                true,
                "Product fetched successfully",
                productService.getProductByIdForAdmin(id)
        );
    }

    @PutMapping("/{id}/approve")
    public ApiResponse<ProductResponseDTO> approveProduct(
            @PathVariable Long id) {

        return new ApiResponse<>(
                true,
                "Product approved successfully",
                productService.approveProduct(id)
        );
    }

    @PutMapping("/{id}/reject")
    public ApiResponse<ProductResponseDTO> rejectProduct(
            @PathVariable Long id) {

        return new ApiResponse<>(
                true,
                "Product rejected successfully",
                productService.rejectProduct(id)
        );
    }
}