package com.example.supplierservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReviewDto {
    private Long id;
    @NotNull(message = "review cannot be without product")
    private Long productId;
    private String comment;
    @NotNull(message = "evaluation cannot be null")
    private String evaluation;
    @NotNull(message = "timestamp cannot be null")
    private long timestamp;

    public ReviewDto(Long productId, String evaluation, long timestamp) {
        this.productId = productId;
        this.evaluation = evaluation;
        this.timestamp = timestamp;
    }
}
