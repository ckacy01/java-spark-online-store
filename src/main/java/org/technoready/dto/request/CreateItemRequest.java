package org.technoready.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Create Item Request DTO
 * Data Transfer Object for items creation
 * Separates API contract from domain model (Interface Segregation Principle)
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateItemRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private boolean available;

    public void validate() {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }
}
