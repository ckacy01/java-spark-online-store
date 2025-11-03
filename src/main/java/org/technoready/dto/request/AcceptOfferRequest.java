package org.technoready.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcceptOfferRequest {
    private Long offerId;

    public void validate() {
        if (offerId == null) {
            throw new IllegalArgumentException("Offer ID is required");
        }
    }
}
