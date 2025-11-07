package org.technoready.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOfferRequest {
    private Long itemId;
    private Long userId;
    private BigDecimal offerAmount;
    private String message;

    public void validate() {
        if (itemId == null) {
            throw new IllegalArgumentException("Item ID is required");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (offerAmount == null) {
            throw new IllegalArgumentException("Offer amount is required");
        }
        if (offerAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Offer amount must be greater than zero");
        }
        if (offerAmount.scale() > 2) {
            throw new IllegalArgumentException("Offer amount can have maximum 2 decimal places");
        }
    }
}
