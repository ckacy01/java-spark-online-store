package org.technoready.util;


import org.technoready.dto.request.CreateItemRequest;
import org.technoready.dto.request.UpdateItemRequest;
import org.technoready.dto.response.ItemResponse;
import org.technoready.entity.Item;

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
                .build();
    }

    public static ItemResponse toResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
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
        if(request.getPrice() >  0) {
            item.setPrice(request.getPrice());
        }
        return item;
    }
}
