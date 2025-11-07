package org.technoready.service;

import org.technoready.dto.request.CreateItemRequest;
import org.technoready.dto.request.UpdateItemRequest;
import org.technoready.entity.Item;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Item Service Interface
 * Defines business logic contract for item operations
 * Follows Interface Segregation Principle
 * Allows for easy mocking in tests and multiple implementations
 */

public interface ItemService {
    /**
     * Retrieve all items
     */
    List<Item> getAllItems();
    /**
     * Find all available
     */
    List<Item> getAllAvailableItems();
    /**
     * Create a new item
     */
    Item createItem(CreateItemRequest request);
    /**
     * Retrieve an Item by id
     */
    Optional<Item> getItemById(Long id);
    /**
     * Update an existing Item
     */
    Item updateItem(Long id, UpdateItemRequest request);
    /**
     * Delete an item by id
     */
    boolean deleteItem(Long id);
    /**
     * Search and item by name
     */
    Optional<Item> getItemByName(String name);
    /**
     * Update availability
     */
    int updateItemAvailability(Long id, Boolean available);
    /**
     * Item exists
     */
    boolean itemExists(Long id);
    /**
     * Get total offers count for an item
     */
    int getOfferCountForItem(Long itemId);

    /**
     * Get highest offer amount for an item
     */
    BigDecimal getHighestOfferForItem(Long itemId);
}
