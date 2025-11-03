package org.technoready.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferWithDetails {
    // Offer fields
    private Long id;
    private Long itemId;
    private Long userId;
    private BigDecimal offerAmount;
    private Offer.OfferStatus status;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // User fields (from JOIN)
    private String username;
    private String userEmail;
    private String userFullName;

    // Item fields (from JOIN)
    private String itemName;
    private String itemDescription;
    private BigDecimal itemCurrentPrice;
    private BigDecimal itemOriginalPrice;
    private Boolean itemIsAvailable;
}

