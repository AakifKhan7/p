package com.Aakifkhan.BazarBook.dto.shop;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopResponse {
    private Long id;
    private String shopName;
    private String shopAddress;
    private String shopPhone;
}
