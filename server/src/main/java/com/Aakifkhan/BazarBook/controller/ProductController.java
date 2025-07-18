package com.Aakifkhan.BazarBook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.Aakifkhan.BazarBook.dto.Product.ProductCreateRequest;
import com.Aakifkhan.BazarBook.dto.Product.ProductSearchRequest;
import com.Aakifkhan.BazarBook.dto.Product.ProductSearchResponse;
import com.Aakifkhan.BazarBook.model.Inventory.ProductModel;
import com.Aakifkhan.BazarBook.services.ProductService;
import org.modelmapper.ModelMapper;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@Validated
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ModelMapper modelMapper;
    
    /**
     * Search for products by name
     * @param request Search request containing product name
     * @return List of matching products
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductSearchResponse>> searchProducts(@RequestParam String productName) {
        ProductSearchRequest request = new ProductSearchRequest();
        request.setProductName(productName);
        List<ProductSearchResponse> products = productService.searchProducts(request);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Create new product
     * @param request Product creation request
     * @return Created product details
     */
    @PostMapping
    public ResponseEntity<ProductSearchResponse> createProduct(@RequestBody @Valid ProductCreateRequest request) {
        ProductSearchResponse product = productService.createProduct(request);
        return ResponseEntity.ok(product);
    }
    
    /**
     * Get product by name
     * @param productName Name of the product to retrieve
     * @return Product details
     */
    @GetMapping("/by-name")
    public ResponseEntity<ProductSearchResponse> getProductByName(@RequestParam String productName) {
        ProductModel product = productService.getProductByName(productName);
        return ResponseEntity.ok(modelMapper.map(product, ProductSearchResponse.class));
    }
}
