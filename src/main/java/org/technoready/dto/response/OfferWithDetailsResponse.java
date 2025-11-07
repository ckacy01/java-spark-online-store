package org.technoready.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Offer With Details Response DTO
 * Includes user and item information
 * @version 1.2.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferWithDetailsResponse {
    // Offer info
    private Long id;
    private BigDecimal offerAmount;
    private String status;
    private String message;
    private String createdAt;
    private String updatedAt;

    // User info
    private UserInfoResponse user;

    // Item info
    private ItemInfoResponse item;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfoResponse {
        private Long id;
        private String username;
        private String email;
        private String fullName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemInfoResponse {
        private Long id;
        private String name;
        private String description;
        private BigDecimal currentPrice;
        private BigDecimal originalPrice;
        private Boolean isAvailable;
    }
}

