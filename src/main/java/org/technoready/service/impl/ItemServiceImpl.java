package org.technoready.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.technoready.dao.ItemDao;
import org.technoready.dto.request.CreateItemRequest;
import org.technoready.dto.request.UpdateItemRequest;
import org.technoready.entity.Item;
import org.technoready.service.ItemService;
import org.technoready.util.ItemMapper;

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
    public Item createItem(CreateItemRequest request) {
        log.debug("Creating item {}", request.getName());
        request.validate();

        Optional<Item> existingItem = jdbi.withExtension(ItemDao.class,
                dao -> dao.findByName(request.getName()));
        if (existingItem.isPresent()) {
            throw new IllegalArgumentException("Item with name " + request.getName() + " already exists");
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
                .orElseThrow(() -> new IllegalArgumentException("Item with id " + id + " does not exist"));
        Item updatedItem = ItemMapper.updateEntity(item, request);
        jdbi.withExtension(ItemDao.class, dao -> dao.update(updatedItem));

        log.info("Updated item {}", updatedItem);
        return updatedItem;
    }

    @Override
    public boolean deleteItem(Long id) {
        log.info("Deleting item {}", id);

        int rowsAffected = jdbi.withExtension(ItemDao.class, dao -> dao.delete(id));

        if (rowsAffected > 0) {
            log.info("Item deleted successfully {}", id);
            return true;
        }
        log.error("Item with id {} does not exist", id);
        return false;
    }




}
