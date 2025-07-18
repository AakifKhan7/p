package com.Aakifkhan.BazarBook.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Aakifkhan.BazarBook.dto.Inventory.InventoryCreateRequest;
import com.Aakifkhan.BazarBook.dto.Inventory.InventoryResponse;
import com.Aakifkhan.BazarBook.model.Inventory.InventoryModel;
import com.Aakifkhan.BazarBook.model.Inventory.ProductModel;
import com.Aakifkhan.BazarBook.model.Shop.ShopModel;
import com.Aakifkhan.BazarBook.model.User.UserModel;
import com.Aakifkhan.BazarBook.repository.InventoryRepository;
import com.Aakifkhan.BazarBook.repository.ProductRespository;
import com.Aakifkhan.BazarBook.repository.ShopRepository;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    
    @Autowired
    private InventoryRepository inventoryRepository;
    
    @Autowired
    private ProductRespository productRepository;
    
    @Autowired
    private ShopRepository shopRepository;
    
    @Autowired
    private CurrentUserService currentUserService;
    
    @Autowired
    private ModelMapper modelMapper;
    
    /**
     * Create or update inventory
     * @param request Inventory creation request
     * @return Created/updated inventory details
     */
    @Transactional
    public InventoryResponse createInventory(InventoryCreateRequest request) {
        UserModel currentUser = currentUserService.getCurrentUser();
        
        // Validate shop belongs to current user
        ShopModel shop = shopRepository.findByIdAndIsDeletedFalse(request.getShopId())
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        if (!shop.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized: Shop does not belong to user");
        }
        
        // Get or create product
        ProductModel product = productRepository.findByProductNameIgnoreCaseAndIsDeletedFalse(request.getProductName())
                .orElseGet(() -> {
                    ProductModel newProduct = new ProductModel();
                    newProduct.setProductName(request.getProductName());
                    newProduct.setCategory(request.getCategory());
                    newProduct.setCreatedBy(currentUser);
                    newProduct.setUpdatedBy(currentUser);
                    return productRepository.save(newProduct);
                });
        
        // Create inventory
        InventoryModel inventory = new InventoryModel();
        inventory.setShop(shop);
        inventory.setProduct(product);
        inventory.setQuantity(request.getQuantity());
        inventory.setPrice(request.getPrice());
        inventory.setCreatedBy(currentUser);
        inventory.setUpdatedBy(currentUser);
        
        InventoryModel saved = inventoryRepository.save(inventory);
        return modelMapper.map(saved, InventoryResponse.class);
    }
    
    /**
     * Get inventory for a shop
     * @param shopId Shop ID
     * @return List of inventory items
     */
    public List<InventoryResponse> getInventoryByShop(Long shopId) {
        UserModel currentUser = currentUserService.getCurrentUser();
        ShopModel shop = shopRepository.findByIdAndIsDeletedFalse(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        if (!shop.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized: Shop does not belong to user");
        }
        
        List<InventoryModel> inventories = inventoryRepository.findByShopAndIsDeletedFalse(shop);
        return inventories.stream()
                .map(i -> modelMapper.map(i, InventoryResponse.class))
                .collect(Collectors.toList());
    }
}
