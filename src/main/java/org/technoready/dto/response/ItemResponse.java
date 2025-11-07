package org.technoready.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Item Response DTO
 * Data Transfer Object for user responses
 * Decouples internal representation from API response
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemResponse {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean available;
    private BigDecimal currentPrice;
    private BigDecimal originalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer totalOffers;
    private BigDecimal highestOffer;
}
