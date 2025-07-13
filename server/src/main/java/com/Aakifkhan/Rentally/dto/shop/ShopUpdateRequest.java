package com.Aakifkhan.Rentally.dto.shop;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ShopUpdateRequest {
    @NotBlank
    @Size(max = 255)
    private String shopName;

    @NotBlank
    @Size(max = 255)
    private String shopAddress;

    @NotBlank
    @Size(max = 15)
    private String shopPhone;
}
