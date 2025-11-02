package org.technoready.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private double price;
}
