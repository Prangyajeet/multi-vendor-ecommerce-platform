package com.prangyajeet.mvep.product.controller;

import com.prangyajeet.mvep.product.dto.ProductRequestDTO;
import com.prangyajeet.mvep.product.dto.ProductResponseDTO;
import com.prangyajeet.mvep.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import com.prangyajeet.mvep.response.ApiResponse;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ApiResponse<ProductResponseDTO> createProduct(
            @Valid @RequestBody ProductRequestDTO requestDTO) {

        ProductResponseDTO product =
                productService.createProduct(requestDTO);

        return new ApiResponse<>(
                true,
                "Product created successfully",
                product
        );
    }

    @GetMapping
    public ApiResponse<List<ProductResponseDTO>> getAllProducts() {

        List<ProductResponseDTO> products =
                productService.getAllProducts();

        return new ApiResponse<>(
                true,
                "Approved products fetched successfully",
                products
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponseDTO> getProductById(
            @PathVariable Long id) {

        ProductResponseDTO product =
                productService.getProductById(id);

        return new ApiResponse<>(
                true,
                "Approved product fetched successfully",
                product
        );
    }
    
    @GetMapping("/category/{categoryId}")
    public ApiResponse<List<ProductResponseDTO>> getProductsByCategory(
            @PathVariable Long categoryId) {

        List<ProductResponseDTO> products =
                productService.getProductsByCategory(categoryId);

        return new ApiResponse<>(
                true,
                "Products fetched successfully",
                products
        );
    }

    @GetMapping("/vendor/{vendorId}")
    public ApiResponse<List<ProductResponseDTO>> getProductsByVendor(
            @PathVariable Long vendorId) {

        List<ProductResponseDTO> products =
                productService.getProductsByVendor(vendorId);

        return new ApiResponse<>(
                true,
                "Vendor products fetched successfully",
                products
        );
    }

    @GetMapping("/search")
    public ApiResponse<List<ProductResponseDTO>> searchProducts(
            @RequestParam String keyword) {

        List<ProductResponseDTO> products =
                productService.searchProducts(keyword);

        return new ApiResponse<>(
                true,
                "Search completed successfully",
                products
        );
    }

    @GetMapping("/page")
    public ApiResponse<Page<ProductResponseDTO>> getProductsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<ProductResponseDTO> products =
                productService.getProductsWithPagination(page, size);

        return new ApiResponse<>(
                true,
                "Products fetched successfully",
                products
        );
    }

    // ===========================================
    // PAGINATION WITH SORTING
    // ===========================================

    @GetMapping("/page/sort")
    public ApiResponse<Page<ProductResponseDTO>> getProductsWithSorting(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Page<ProductResponseDTO> products =
                productService.getProductsWithSorting(
                        page,
                        size,
                        sortBy,
                        direction
                );

        return new ApiResponse<>(
                true,
                "Products fetched successfully",
                products
        );
    }

    // ===========================================
    // FILTER PRODUCTS BY PRICE
    // ===========================================

    @GetMapping("/filter")
    public ApiResponse<List<ProductResponseDTO>> filterProductsByPrice(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {

        List<ProductResponseDTO> products =
                productService.filterProductsByPrice(
                        minPrice,
                        maxPrice
                );

        return new ApiResponse<>(
                true,
                "Products filtered successfully",
                products
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO requestDTO) {

        ProductResponseDTO product =
                productService.updateProduct(id, requestDTO);

        return new ApiResponse<>(
                true,
                "Product updated successfully",
                product
        );
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        return new ApiResponse<>(
                true,
                "Product deleted successfully",
                null
        );
    }
}