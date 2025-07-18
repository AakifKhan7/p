package com.Aakifkhan.BazarBook.dto.Product;

import lombok.Data;

@Data
public class ProductSearchRequest {
    private String productName;
    private String category;
}
