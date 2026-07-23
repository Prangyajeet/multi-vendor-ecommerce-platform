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
import com.prangyajeet.mvep.user.entity.User;
import com.prangyajeet.mvep.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private static final int LOW_STOCK_THRESHOLD = 10;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            VendorRepository vendorRepository,
            UserRepository userRepository,
            NotificationService notificationService) {
    	
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }
    
    private Vendor getLoggedInVendor() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Logged-in user not found."
                        ));

        return vendorRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new VendorNotFoundException(
                                "Vendor profile not found."
                        ));
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

        Product updatedProduct = productRepository.save(product);

        if (updatedProduct.getStockQuantity() == 0) {

            notificationService.sendVendorNotification(
                    updatedProduct.getVendor().getUser(),
                    "Out of Stock",
                    "Your product \"" + updatedProduct.getName()
                            + "\" is now out of stock. Please restock it.",
                    NotificationType.PRODUCT
            );

        } else if (updatedProduct.getStockQuantity() <= LOW_STOCK_THRESHOLD) {

            notificationService.sendVendorNotification(
                    updatedProduct.getVendor().getUser(),
                    "Low Stock Alert",
                    "Your product \"" + updatedProduct.getName()
                            + "\" is running low on stock. Only "
                            + updatedProduct.getStockQuantity()
                            + " item(s) remaining.",
                    NotificationType.PRODUCT
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

        Vendor vendor = getLoggedInVendor();
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
                        + "\" has been approved and is now available for customers.",
                NotificationType.PRODUCT
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
                        + "\" has been rejected. Please review and update it before submitting again.",
                NotificationType.PRODUCT
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

        Vendor vendor = getLoggedInVendor();
        
        if (!existingProduct.getVendor().getId().equals(vendor.getId())) {

            throw new RuntimeException(
                    "You are not authorized to update this product."
            );
        }

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

        Vendor vendor = getLoggedInVendor();

        if (!product.getVendor().getId().equals(vendor.getId())) {

            throw new RuntimeException(
                    "You are not authorized to delete this product."
            );
        }

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
 // GET LOGGED-IN VENDOR PRODUCTS
 // =====================================================

 public List<ProductResponseDTO> getMyProducts() {

     Vendor vendor = getLoggedInVendor();

     return productRepository.findProductsByVendorId(vendor.getId())
             .stream()
             .map(this::mapToResponseDTO)
             .collect(Collectors.toList());
 }

 // =====================================================
 // GET MY PRODUCT BY ID
 // =====================================================

 public ProductResponseDTO getMyProductById(Long productId) {

     Vendor vendor = getLoggedInVendor();

     Product product = productRepository.findById(productId)
             .orElseThrow(() ->
                     new ProductNotFoundException(
                             "Product not found with id : " + productId
                     ));

     if (!product.getVendor().getId().equals(vendor.getId())) {
         throw new RuntimeException(
                 "You are not authorized to access this product."
         );
     }

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