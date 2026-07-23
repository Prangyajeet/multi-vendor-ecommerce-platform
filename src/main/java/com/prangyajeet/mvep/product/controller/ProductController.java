package com.prangyajeet.mvep.product.controller;

import com.prangyajeet.mvep.product.dto.ProductResponseDTO;
import com.prangyajeet.mvep.product.service.ProductService;
import com.prangyajeet.mvep.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ApiResponse<List<ProductResponseDTO>> getAllProducts() {

        return new ApiResponse<>(
                true,
                "Approved products fetched successfully",
                productService.getAllProducts()
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponseDTO> getProductById(
            @PathVariable Long id) {

        return new ApiResponse<>(
                true,
                "Approved product fetched successfully",
                productService.getProductById(id)
        );
    }

    @GetMapping("/category/{categoryId}")
    public ApiResponse<List<ProductResponseDTO>> getProductsByCategory(
            @PathVariable Long categoryId) {

        return new ApiResponse<>(
                true,
                "Products fetched successfully",
                productService.getProductsByCategory(categoryId)
        );
    }

    @GetMapping("/vendor/{vendorId}")
    public ApiResponse<List<ProductResponseDTO>> getProductsByVendor(
            @PathVariable Long vendorId) {

        return new ApiResponse<>(
                true,
                "Vendor products fetched successfully",
                productService.getProductsByVendor(vendorId)
        );
    }

    @GetMapping("/search")
    public ApiResponse<List<ProductResponseDTO>> searchProducts(
            @RequestParam String keyword) {

        return new ApiResponse<>(
                true,
                "Search completed successfully",
                productService.searchProducts(keyword)
        );
    }

    @GetMapping("/page")
    public ApiResponse<Page<ProductResponseDTO>> getProductsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return new ApiResponse<>(
                true,
                "Products fetched successfully",
                productService.getProductsWithPagination(page, size)
        );
    }

    @GetMapping("/page/sort")
    public ApiResponse<Page<ProductResponseDTO>> getProductsWithSorting(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return new ApiResponse<>(
                true,
                "Products fetched successfully",
                productService.getProductsWithSorting(
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    @GetMapping("/filter")
    public ApiResponse<List<ProductResponseDTO>> filterProductsByPrice(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {

        return new ApiResponse<>(
                true,
                "Products filtered successfully",
                productService.filterProductsByPrice(
                        minPrice,
                        maxPrice
                )
        );
    }
}