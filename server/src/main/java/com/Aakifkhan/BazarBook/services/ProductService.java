package com.Aakifkhan.BazarBook.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.Aakifkhan.BazarBook.model.Inventory.ProductModel;
import com.Aakifkhan.BazarBook.model.User.UserModel;
import com.Aakifkhan.BazarBook.dto.Product.ProductSearchRequest;
import com.Aakifkhan.BazarBook.dto.Product.ProductSearchResponse;
import com.Aakifkhan.BazarBook.dto.Product.ProductCreateRequest;
import com.Aakifkhan.BazarBook.repository.ProductRespository;
import org.modelmapper.ModelMapper;

@Service
public class ProductService {
    
    @Autowired
    private ProductRespository productRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private CurrentUserService currentUserService;
    
    /**
     * Search for products by name (case-insensitive)
     */
    public List<ProductSearchResponse> searchProducts(ProductSearchRequest request) {
        List<ProductModel> products = productRepository.findByProductNameIgnoreCaseContainingAndIsDeletedFalse(request.getProductName());
        return products.stream()
                .map(product -> modelMapper.map(product, ProductSearchResponse.class))
                .collect(Collectors.toList());
    }
    
    /**
     * Create new product
     */
    @Transactional
    public ProductSearchResponse createProduct(ProductCreateRequest request) {
        UserModel currentUser = currentUserService.getCurrentUser();
        
        // Check if product with same name exists
        if (productRepository.existsByProductNameIgnoreCaseAndIsDeletedFalse(request.getProductName())) {
            throw new RuntimeException("Product with this name already exists");
        }
        
        ProductModel product = new ProductModel();
        product.setProductName(request.getProductName());
        product.setCategory(request.getCategory());
        product.setDescription(request.getDescription());
        product.setImage(request.getImage());
        product.setCreatedBy(currentUser);
        product.setUpdatedBy(currentUser);
        
        ProductModel saved = productRepository.save(product);
        return modelMapper.map(saved, ProductSearchResponse.class);
    }
    
    /**
     * Get product by name (case-insensitive)
     */
    public ProductModel getProductByName(String productName) {
        return productRepository.findByProductNameIgnoreCaseAndIsDeletedFalse(productName)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
}
