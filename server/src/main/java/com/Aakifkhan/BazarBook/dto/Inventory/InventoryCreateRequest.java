package com.Aakifkhan.BazarBook.dto.Inventory;

import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
public class InventoryCreateRequest {
    @NotNull(message = "Product name is required")
    private String productName;
    
    @NotNull(message = "Shop ID is required")
    private Long shopId;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
    
    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be at least 0")
    private Double price;
}
