package com.Aakifkhan.BazarBook.dto.sales;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SalesCreateRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 500)
    private String category;

    @NotNull
    @Positive
    private Integer quantity;

    private String description;

    @NotNull
    @Positive
    private Double price;

    // Optional image URL / path
    private String image;

    @NotNull
    private Long shopId;
}
