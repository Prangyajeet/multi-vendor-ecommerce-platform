package com.prangyajeet.mvep.product.service;

import com.prangyajeet.mvep.category.entity.Category;
import com.prangyajeet.mvep.category.repository.CategoryRepository;
import com.prangyajeet.mvep.product.dto.ProductRequestDTO;
import com.prangyajeet.mvep.product.dto.ProductResponseDTO;
import com.prangyajeet.mvep.product.entity.Product;
import com.prangyajeet.mvep.product.repository.ProductRepository;
import com.prangyajeet.mvep.vendor.entity.Vendor;
import com.prangyajeet.mvep.vendor.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          VendorRepository vendorRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {

        Category category = categoryRepository.findById(
                requestDTO.getCategoryId()
        ).orElseThrow(() ->
                new RuntimeException("Category not found"));

        Vendor vendor = vendorRepository.findById(
                requestDTO.getVendorId()
        ).orElseThrow(() ->
                new RuntimeException("Vendor not found"));

        Product product = new Product();

        product.setName(requestDTO.getName());
        product.setDescription(requestDTO.getDescription());
        product.setPrice(requestDTO.getPrice());
        product.setStockQuantity(requestDTO.getStockQuantity());
        product.setImageUrl(requestDTO.getImageUrl());
        product.setCategory(category);
        product.setVendor(vendor);

        Product savedProduct = productRepository.save(product);

        return mapToResponseDTO(savedProduct);
    }

    public List<ProductResponseDTO> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public ProductResponseDTO getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        return mapToResponseDTO(product);
    }

    public List<ProductResponseDTO> getProductsByCategory(Long categoryId) {

        List<Product> products =
                productRepository.findByCategoryId(categoryId);

        return products.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getProductsByVendor(Long vendorId) {

        List<Product> products =
                productRepository.findProductsByVendorId(vendorId);

        return products.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public ProductResponseDTO updateProduct(Long id,
                                            ProductRequestDTO requestDTO) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        Category category = categoryRepository.findById(
                requestDTO.getCategoryId()
        ).orElseThrow(() ->
                new RuntimeException("Category not found"));

        Vendor vendor = vendorRepository.findById(
                requestDTO.getVendorId()
        ).orElseThrow(() ->
                new RuntimeException("Vendor not found"));

        existingProduct.setName(requestDTO.getName());
        existingProduct.setDescription(requestDTO.getDescription());
        existingProduct.setPrice(requestDTO.getPrice());
        existingProduct.setStockQuantity(requestDTO.getStockQuantity());
        existingProduct.setImageUrl(requestDTO.getImageUrl());
        existingProduct.setCategory(category);
        existingProduct.setVendor(vendor);

        Product updatedProduct =
                productRepository.save(existingProduct);

        return mapToResponseDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        productRepository.delete(product);
    }

    private ProductResponseDTO mapToResponseDTO(Product product) {

        ProductResponseDTO responseDTO =
                new ProductResponseDTO();

        responseDTO.setId(product.getId());
        responseDTO.setName(product.getName());
        responseDTO.setDescription(product.getDescription());
        responseDTO.setPrice(product.getPrice());
        responseDTO.setStockQuantity(product.getStockQuantity());
        responseDTO.setImageUrl(product.getImageUrl());

        responseDTO.setCategoryId(product.getCategory().getId());
        responseDTO.setCategoryName(product.getCategory().getName());

        if (product.getVendor() != null) {

            responseDTO.setVendorId(product.getVendor().getId());
            responseDTO.setVendorName(product.getVendor().getBusinessName());
        }

        return responseDTO;
    }
}