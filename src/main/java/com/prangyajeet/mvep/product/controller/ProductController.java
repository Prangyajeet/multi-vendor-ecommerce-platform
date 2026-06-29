package com.prangyajeet.mvep.product.controller;

import com.prangyajeet.mvep.product.dto.ProductRequestDTO;
import com.prangyajeet.mvep.product.dto.ProductResponseDTO;
import com.prangyajeet.mvep.product.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ProductResponseDTO createProduct(
            @RequestBody ProductRequestDTO requestDTO) {

        return productService.createProduct(requestDTO);
    }

    @GetMapping
    public List<ProductResponseDTO> getAllProducts() {

        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductResponseDTO getProductById(@PathVariable Long id) {

        return productService.getProductById(id);
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductResponseDTO> getProductsByCategory(
            @PathVariable Long categoryId) {

        return productService.getProductsByCategory(categoryId);
    }

    @GetMapping("/vendor/{vendorId}")
    public List<ProductResponseDTO> getProductsByVendor(
            @PathVariable Long vendorId) {

        return productService.getProductsByVendor(vendorId);
    }

    @PutMapping("/{id}")
    public ProductResponseDTO updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequestDTO requestDTO) {

        return productService.updateProduct(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);

        return "Product deleted successfully";
    }
}