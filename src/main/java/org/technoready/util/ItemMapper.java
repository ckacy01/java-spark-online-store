package org.technoready.util;


import org.technoready.dto.request.CreateItemRequest;
import org.technoready.dto.request.UpdateItemRequest;
import org.technoready.dto.response.ItemResponse;
import org.technoready.entity.Item;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Item Mapper utility
 * Maps between Entity and DTO objects
 */
public class ItemMapper {

    public static Item toEntity(CreateItemRequest request) {
        return  Item.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .available(request.isAvailable())
                .currentPrice(request.getPrice())
                .originalPrice(request.getPrice())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static ItemResponse toResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .available(item.isAvailable())
                .currentPrice(item.getCurrentPrice())
                .originalPrice(item.getOriginalPrice())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .totalOffers(item.getTotalOffers())
                .highestOffer(item.getHighestOffer())
                .build();
    }

    public static List<ItemResponse> toResponseList(List<Item> items) {
        return items.stream()
                .map(ItemMapper::toResponse)
                .collect(Collectors.toList());
    }

    public static Item updateEntity(Item item, UpdateItemRequest request) {
        if(request.getName() != null && !request.getName().isEmpty()) {
            item.setName(request.getName());
        }
        if(request.getDescription() != null && !request.getDescription().isEmpty()) {
            item.setDescription(request.getDescription());
        }
        if(request.getPrice() != null || request.getPrice().compareTo(BigDecimal.ZERO) > 0) {
            item.setPrice(request.getPrice());
        }
        item.setUpdatedAt(LocalDateTime.now());
        item.setCurrentPrice(request.getPrice());

        return item;
    }
    
    public static ItemResponse toResponseWithStats(Item item, Integer totalOffers, java.math.BigDecimal highestOffer) {
        ItemResponse response = toResponse(item);
        response.setTotalOffers(totalOffers);
        response.setHighestOffer(highestOffer);
        return response;
    }

}
