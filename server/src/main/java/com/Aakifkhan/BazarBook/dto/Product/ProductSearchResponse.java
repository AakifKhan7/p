package com.Aakifkhan.BazarBook.dto.Product;

import lombok.Data;

@Data
public class ProductSearchResponse {
    private Long id;
    private String productName;
    private String category;
    private String description;
    private String image;
}
