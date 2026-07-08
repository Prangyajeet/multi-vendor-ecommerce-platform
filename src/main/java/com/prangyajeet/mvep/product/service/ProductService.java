package com.prangyajeet.mvep.product.service;

import com.prangyajeet.mvep.category.entity.Category;
import com.prangyajeet.mvep.category.repository.CategoryRepository;
import com.prangyajeet.mvep.exception.CategoryNotFoundException;
import com.prangyajeet.mvep.exception.ProductNotFoundException;
import com.prangyajeet.mvep.exception.VendorNotFoundException;
import com.prangyajeet.mvep.product.dto.ProductRequestDTO;
import com.prangyajeet.mvep.product.dto.ProductResponseDTO;
import com.prangyajeet.mvep.product.entity.Product;
import com.prangyajeet.mvep.product.repository.ProductRepository;
import com.prangyajeet.mvep.vendor.entity.Vendor;
import com.prangyajeet.mvep.vendor.repository.VendorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    
    public void reduceStock(Long productId, Integer quantity) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id : " + productId
                        ));

        if (product.getStockQuantity() < quantity) {

            throw new IllegalArgumentException(
                    "Insufficient stock for product : " + product.getName()
            );
        }

        product.setStockQuantity(
                product.getStockQuantity() - quantity
        );

        productRepository.save(product);
    }

    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {

        Category category = categoryRepository.findById(
                requestDTO.getCategoryId()
        ).orElseThrow(() ->
                new CategoryNotFoundException(
                        "Category not found with id : "
                                + requestDTO.getCategoryId()
                ));

        Vendor vendor = vendorRepository.findById(
                requestDTO.getVendorId()
        ).orElseThrow(() ->
                new VendorNotFoundException(
                        "Vendor not found with id : "
                                + requestDTO.getVendorId()
                ));

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
                        new ProductNotFoundException(
                                "Product not found with id : " + id
                        ));

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

    public List<ProductResponseDTO> searchProducts(String keyword) {

        List<Product> products =
                productRepository.findByNameContainingIgnoreCase(keyword);

        return products.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // ===========================
    // PAGINATION
    // ===========================

    public Page<ProductResponseDTO> getProductsWithPagination(
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Product> productPage =
                productRepository.findAll(pageable);

        return productPage.map(this::mapToResponseDTO);
    }

    // ===========================
    // PAGINATION WITH SORTING
    // ===========================

    public Page<ProductResponseDTO> getProductsWithSorting(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage =
                productRepository.findAll(pageable);

        return productPage.map(this::mapToResponseDTO);
    }    // ===========================
    // FILTER PRODUCTS BY PRICE
    // ===========================

    public List<ProductResponseDTO> filterProductsByPrice(
            BigDecimal minPrice,
            BigDecimal maxPrice) {

        List<Product> products =
                productRepository.findByPriceBetween(
                        minPrice,
                        maxPrice
                );

        return products.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public ProductResponseDTO updateProduct(
            Long id,
            ProductRequestDTO requestDTO) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id : " + id
                        ));

        Category category = categoryRepository.findById(
                requestDTO.getCategoryId()
        ).orElseThrow(() ->
                new CategoryNotFoundException(
                        "Category not found with id : "
                                + requestDTO.getCategoryId()
                ));

        Vendor vendor = vendorRepository.findById(
                requestDTO.getVendorId()
        ).orElseThrow(() ->
                new VendorNotFoundException(
                        "Vendor not found with id : "
                                + requestDTO.getVendorId()
                ));

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
                        new ProductNotFoundException(
                                "Product not found with id : " + id
                        ));

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