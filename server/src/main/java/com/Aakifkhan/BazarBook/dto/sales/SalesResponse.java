package com.Aakifkhan.BazarBook.dto.sales;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesResponse {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Integer quantity;
    private java.sql.Timestamp createdAt;
    private double price;
    private String image;
    private Long shopId;
}
