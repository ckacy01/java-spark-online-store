package org.technoready.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.technoready.dao.ItemDao;
import org.technoready.dto.request.CreateItemRequest;
import org.technoready.dto.request.UpdateItemRequest;
import org.technoready.entity.Item;
import org.technoready.exception.ConflictException;
import org.technoready.exception.NotFoundException;
import org.technoready.service.ItemService;
import org.technoready.util.ItemMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Item Service Implementation
 * Contains business logic for items
 */

@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final Jdbi jdbi;

    @Override
    public List<Item> getAllItems() {
        log.debug("Getting all items");
        return jdbi.withExtension(ItemDao.class, ItemDao::findAll);
    }

    @Override
    public List<Item> getAllAvailableItems() {
        log.debug("Getting all items available");
        return jdbi.withExtension(ItemDao.class, ItemDao::findAllAvailable);
    }

    @Override
    public Item createItem(CreateItemRequest request) {
        log.debug("Creating item {}", request.getName());
        request.validate();

        Optional<Item> existingItem = jdbi.withExtension(ItemDao.class,
                dao -> dao.findByName(request.getName()));
        if (existingItem.isPresent()) {
            throw new ConflictException("Item with name " + request.getName() + " already exists");
        }

        Item item = ItemMapper.toEntity(request);
        long generatedId = jdbi
                .withExtension(ItemDao.class, dao -> dao.insert(item));
        item.setId(generatedId);

        log.debug("Created item {}", item);
        return item;
    }

    @Override
    public Optional<Item> getItemById(Long id) {
        log.debug("Getting item with id {}", id);
        return jdbi.withExtension(ItemDao.class, dao -> dao.findById(id));
    }

    @Override
    public Item updateItem(Long id, UpdateItemRequest request) {
        log.info("Updating item {}", request.getName());
        request.validate();

        Item item = getItemById(id)
                .orElseThrow(() -> new NotFoundException("Item with id " + id + " does not exist"));

        Item updatedItem = ItemMapper.updateEntity(item, request);
        jdbi.withExtension(ItemDao.class, dao -> dao.update(updatedItem));

        log.info("Updated item {}", updatedItem);
        return updatedItem;
    }

    @Override
    public boolean deleteItem(Long id) {
        log.info("Deleting item {}", id);

        int rowsAffected = jdbi.withExtension(ItemDao.class, dao -> dao.delete(id));

        if (rowsAffected == 0) {
            log.info("Item deleted successfully {}", id);
            throw new NotFoundException("Item with id " + id + " does not exist");
        }
        log.error("Item with id {} deleted successfully!", id);
        return true;
    }

    @Override
    public Optional<Item> getItemByName(String name) {
        log.debug("Getting item by name {}", name);
        return jdbi.withExtension(ItemDao.class, dao -> dao.findByName(name));
    }

    @Override
    public int updateItemAvailability(Long id, Boolean available) {
        log.debug("Updating item availability {}", id);
        if(!itemExists(id)){
            throw new NotFoundException("Item with id " + id + " does not exist");
        }
        return jdbi.withExtension(ItemDao.class, dao -> dao.updateAvailability(id, available));
    }

    @Override
    public boolean itemExists(Long id) {
        log.debug("Getting item with id {}", id);
        return jdbi.withExtension(ItemDao.class, dao -> dao.exists(id));
    }

    @Override
    public int getOfferCountForItem(Long itemId) {
        log.debug("Getting offer count for item: {}", itemId);
        return jdbi.withExtension(ItemDao.class, dao -> dao.countOffersByItemId(itemId));
    }

    @Override
    public BigDecimal getHighestOfferForItem(Long itemId) {
        log.debug("Getting highest offer for item: {}", itemId);
        return jdbi.withExtension(ItemDao.class, dao -> dao.getHighestOfferByItemId(itemId));
    }

}
