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
public class Offer {
    private Long id;
    private Long itemId;
    private Long userId;
    private BigDecimal offerAmount;
    private OfferStatus status;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum OfferStatus {
        PENDING,    // Offer is waiting for review
        ACCEPTED,   // Offer was accepted by seller
        REJECTED,   // Offer was rejected
        OUTBID      // Another user made a higher offer
    }
}
