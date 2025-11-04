package org.technoready.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferResponse {
    private Long id;
    private Long itemId;
    private Long userId;
    private BigDecimal offerAmount;
    private String status;
    private String message;
    private String createdAt;
    private String updatedAt;
}

