package com.prangyajeet.mvep.vendor.controller;

import com.prangyajeet.mvep.product.dto.ProductRequestDTO;
import com.prangyajeet.mvep.product.dto.ProductResponseDTO;
import com.prangyajeet.mvep.product.service.ProductService;
import com.prangyajeet.mvep.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/products")
public class VendorProductController {

    private final ProductService productService;

    public VendorProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ApiResponse<ProductResponseDTO> createProduct(
            @Valid @RequestBody ProductRequestDTO requestDTO) {

        ProductResponseDTO product =
                productService.createProduct(requestDTO);

        return new ApiResponse<>(
                true,
                "Product submitted for approval successfully.",
                product
        );
    }

    @GetMapping
    public ApiResponse<List<ProductResponseDTO>> getMyProducts() {

        return new ApiResponse<>(
                true,
                "Vendor products fetched successfully.",
                productService.getMyProducts()
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponseDTO> getMyProduct(
            @PathVariable Long id) {

        return new ApiResponse<>(
                true,
                "Vendor product fetched successfully.",
                productService.getMyProductById(id)
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO requestDTO) {

        return new ApiResponse<>(
                true,
                "Product updated successfully. Waiting for admin approval.",
                productService.updateProduct(id, requestDTO)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        return new ApiResponse<>(
                true,
                "Product deleted successfully.",
                null
        );
    }
}