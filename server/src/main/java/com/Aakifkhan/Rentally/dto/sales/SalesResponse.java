package com.Aakifkhan.Rentally.dto.sales;

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
    private double price;
    private String image;
    private Long shopId;
}
