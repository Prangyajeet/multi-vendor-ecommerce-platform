package com.prangyajeet.mvep.product.service;

import com.prangyajeet.mvep.category.entity.Category;
import com.prangyajeet.mvep.category.repository.CategoryRepository;
import com.prangyajeet.mvep.common.enums.NotificationType;
import com.prangyajeet.mvep.exception.CategoryNotFoundException;
import com.prangyajeet.mvep.exception.ProductNotFoundException;
import com.prangyajeet.mvep.exception.VendorNotFoundException;
import com.prangyajeet.mvep.notification.service.NotificationService;
import com.prangyajeet.mvep.product.dto.ProductRequestDTO;
import com.prangyajeet.mvep.product.dto.ProductResponseDTO;
import com.prangyajeet.mvep.product.entity.Product;
import com.prangyajeet.mvep.common.enums.ProductStatus;
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
    private final NotificationService notificationService;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            VendorRepository vendorRepository,
            NotificationService notificationService) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
        this.notificationService = notificationService;
    }

    // =====================================================
    // REDUCE STOCK
    // =====================================================

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

        if (product.getStockQuantity() == 0) {

            notificationService.sendVendorNotification(
                    product.getVendor().getUser(),
                    "Product Out Of Stock",
                    "Your product \"" + product.getName()
                            + "\" is now out of stock."
            );

        } else if (product.getStockQuantity() <= 5) {

            notificationService.sendVendorNotification(
                    product.getVendor().getUser(),
                    "Low Stock Alert",
                    "Your product \"" + product.getName()
                            + "\" has only "
                            + product.getStockQuantity()
                            + " items remaining."
            );
        }
    }

    // =====================================================
    // CREATE PRODUCT
    // =====================================================

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
        product.setStatus(ProductStatus.PENDING);

        Product savedProduct = productRepository.save(product);

        notificationService.sendNotificationToAdmins(
                "New Product Approval Required",
                "Vendor "
                        + vendor.getBusinessName()
                        + " submitted a new product \""
                        + savedProduct.getName()
                        + "\" for approval.",
                NotificationType.PRODUCT
        );

        return mapToResponseDTO(savedProduct);
    }

    // =====================================================
    // GET ALL APPROVED PRODUCTS
    // =====================================================

    public List<ProductResponseDTO> getAllProducts() {

        return productRepository.findByStatus(ProductStatus.APPROVED)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // =====================================================
    // GET PRODUCT BY ID
    // =====================================================

    public ProductResponseDTO getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id : " + id
                        ));

        if (product.getStatus() != ProductStatus.APPROVED) {

            throw new ProductNotFoundException(
                    "Product not found with id : " + id
            );
        }

        return mapToResponseDTO(product);
    }

    // =====================================================
    // GET PRODUCTS BY CATEGORY
    // =====================================================

    public List<ProductResponseDTO> getProductsByCategory(Long categoryId) {

        List<Product> products =
                productRepository.findByCategoryIdAndStatus(
                        categoryId,
                        ProductStatus.APPROVED
                );

        return products.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // =====================================================
    // GET PRODUCTS BY VENDOR
    // =====================================================

    public List<ProductResponseDTO> getProductsByVendor(Long vendorId) {

        List<Product> products =
                productRepository.findByVendorIdAndStatus(
                        vendorId,
                        ProductStatus.APPROVED
                );

        return products.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    // =====================================================
    // SEARCH PRODUCTS
    // =====================================================

    public List<ProductResponseDTO> searchProducts(String keyword) {

        List<Product> products =
                productRepository.findByNameContainingIgnoreCaseAndStatus(
                        keyword,
                        ProductStatus.APPROVED
                );

        return products.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // =====================================================
    // PAGINATION
    // =====================================================

    public Page<ProductResponseDTO> getProductsWithPagination(
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Product> productPage =
                productRepository.findAll(pageable);

        List<ProductResponseDTO> dtoList = productPage.getContent()
                .stream()
                .filter(product ->
                        product.getStatus() == ProductStatus.APPROVED)
                .map(this::mapToResponseDTO)
                .toList();

        return new org.springframework.data.domain.PageImpl<>(
                dtoList,
                pageable,
                productPage.getTotalElements()
        );
    }

    // =====================================================
    // PAGINATION WITH SORTING
    // =====================================================

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

        List<ProductResponseDTO> dtoList = productPage.getContent()
                .stream()
                .filter(product ->
                        product.getStatus() == ProductStatus.APPROVED)
                .map(this::mapToResponseDTO)
                .toList();

        return new org.springframework.data.domain.PageImpl<>(
                dtoList,
                pageable,
                productPage.getTotalElements()
        );
    }

    // =====================================================
    // FILTER PRODUCTS BY PRICE
    // =====================================================

    public List<ProductResponseDTO> filterProductsByPrice(
            BigDecimal minPrice,
            BigDecimal maxPrice) {

        List<Product> products =
                productRepository.findByPriceBetween(
                        minPrice,
                        maxPrice
                );

        return products.stream()
                .filter(product ->
                        product.getStatus() == ProductStatus.APPROVED)
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // =====================================================
    // APPROVE PRODUCT
    // =====================================================

    public ProductResponseDTO approveProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id : " + productId
                        ));

        product.setStatus(ProductStatus.APPROVED);

        Product savedProduct =
                productRepository.save(product);

        notificationService.sendVendorNotification(
                product.getVendor().getUser(),
                "Product Approved",
                "Congratulations! Your product \""
                        + product.getName()
                        + "\" has been approved and is now available for customers."
        );

        return mapToResponseDTO(savedProduct);
    }

    // =====================================================
    // REJECT PRODUCT
    // =====================================================

    public ProductResponseDTO rejectProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id : " + productId
                        ));

        product.setStatus(ProductStatus.REJECTED);

        Product savedProduct =
                productRepository.save(product);

        notificationService.sendVendorNotification(
                product.getVendor().getUser(),
                "Product Rejected",
                "Your product \""
                        + product.getName()
                        + "\" has been rejected. Please review and update it before submitting again."
        );

        return mapToResponseDTO(savedProduct);
    }

    // =====================================================
    // UPDATE PRODUCT
    // =====================================================

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

        existingProduct.setStatus(ProductStatus.PENDING);

        Product updatedProduct =
                productRepository.save(existingProduct);

        notificationService.sendNotificationToAdmins(
                "Product Re-Approval Required",
                "Vendor "
                        + vendor.getBusinessName()
                        + " updated product \""
                        + updatedProduct.getName()
                        + "\" and it requires approval again.",
                NotificationType.PRODUCT
        );

        return mapToResponseDTO(updatedProduct);
    }

    // =====================================================
    // DELETE PRODUCT
    // =====================================================

    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id : " + id
                        ));

        productRepository.delete(product);
    }
    
    public List<ProductResponseDTO> getAllProductsForAdmin() {

        return productRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public ProductResponseDTO getProductByIdForAdmin(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id : " + id
                        ));

        return mapToResponseDTO(product);
    }

    // =====================================================
    // MAP ENTITY TO DTO
    // =====================================================

    private ProductResponseDTO mapToResponseDTO(Product product) {

        ProductResponseDTO responseDTO =
                new ProductResponseDTO();

        responseDTO.setId(product.getId());
        responseDTO.setName(product.getName());
        responseDTO.setDescription(product.getDescription());
        responseDTO.setPrice(product.getPrice());
        responseDTO.setStockQuantity(product.getStockQuantity());
        responseDTO.setImageUrl(product.getImageUrl());
        responseDTO.setStatus(product.getStatus());

        responseDTO.setCategoryId(product.getCategory().getId());
        responseDTO.setCategoryName(product.getCategory().getName());

        if (product.getVendor() != null) {
            responseDTO.setVendorId(product.getVendor().getId());
            responseDTO.setVendorName(product.getVendor().getBusinessName());
        }

        return responseDTO;
    }
}