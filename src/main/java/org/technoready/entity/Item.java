package org.technoready.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal currentPrice;
    private BigDecimal originalPrice;
    @ColumnName("is_available")
    private boolean available;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer totalOffers;
    private BigDecimal highestOffer;
}
