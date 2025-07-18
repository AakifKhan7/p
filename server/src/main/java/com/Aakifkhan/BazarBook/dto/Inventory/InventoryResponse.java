package com.Aakifkhan.BazarBook.dto.Inventory;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class InventoryResponse {
    private Long id;
    private String productName;
    private String shopName;
    private Integer quantity;
    private Double price;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
