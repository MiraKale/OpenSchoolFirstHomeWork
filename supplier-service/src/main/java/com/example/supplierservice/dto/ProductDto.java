package com.example.supplierservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProductDto {
    private Long id;
    @NotBlank(message = "product name should not be empty")
    private String name;
    private String description;
    @NotNull(message = "price cannot be null")
    @Min(value = 0,message = "price should be higher than 0")
    private BigDecimal price;
    private String categoryName;
}
