package com.Aakifkhan.BazarBook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.Aakifkhan.BazarBook.dto.Inventory.InventoryCreateRequest;
import com.Aakifkhan.BazarBook.dto.Inventory.InventoryResponse;
import com.Aakifkhan.BazarBook.services.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@Validated
public class InventoryController {
    
    @Autowired
    private InventoryService inventoryService;
    
    /**
     * Create or update inventory
     * @param request Inventory creation request
     * @return Created/updated inventory details
     */
    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(@Valid @RequestBody InventoryCreateRequest request) {
        InventoryResponse response = inventoryService.createInventory(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get inventory for a shop
     * @param shopId Shop ID
     * @return List of inventory items
     */
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<InventoryResponse>> getInventoryByShop(@PathVariable Long shopId) {
        List<InventoryResponse> response = inventoryService.getInventoryByShop(shopId);
        return ResponseEntity.ok(response);
    }
}
