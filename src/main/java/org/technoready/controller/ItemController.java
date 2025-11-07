package org.technoready.controller;


import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.technoready.dto.request.CreateItemRequest;
import org.technoready.dto.request.UpdateItemRequest;
import org.technoready.dto.response.ApiResponse;
import org.technoready.dto.response.ItemResponse;
import org.technoready.entity.Item;
import org.technoready.exception.BadRequestException;
import org.technoready.exception.NotFoundException;
import org.technoready.service.ItemService;
import org.technoready.util.ItemMapper;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Optional;


/**

 Item Controller
 Handles HTTP requests and responses for item operations
 Follows Single Responsibility Principle - only handles HTTP layer concerns
 Delegates business logic to ItemService
 */

@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final Gson gson;

    public String getAllItems(Request request, Response response) {
        log.info("GET /items - Fetching all items");
        List<Item> items = itemService.getAllItems();
        List<ItemResponse> itemResponses = ItemMapper.toResponseList(items);
        response.status(200);
        return gson.toJson(ApiResponse.success(itemResponses));
    }

    public String getAllAvailableItems(Request request, Response response) {
        log.info("GET /items/availability - Fetching all items");
        List<Item> items = itemService.getAllAvailableItems();
        List<ItemResponse> itemResponses = ItemMapper.toResponseList(items);
        response.status(200);
        return gson.toJson(ApiResponse.success(itemResponses));
    }

    public String getItemById(Request request, Response response) {
        Long id = Long.parseLong(request.params(":id"));
        log.info("GET /items/" + id);

        Item item = itemService.getItemById(id)
                .orElseThrow(() -> new org.technoready.exception.NotFoundException("Item not found with id: " + id));
        response.status(200);
        return gson.toJson(ApiResponse.success(ItemMapper.toResponse(item)));

    }

    public String createItem(Request request, Response response) {
        log.info("POST /items");
        CreateItemRequest body = gson.fromJson(request.body(), CreateItemRequest.class);
        if (body == null) throw new BadRequestException("Invalid request body");

        Item item = itemService.createItem(body);
        response.status(201);
        return gson.toJson(ApiResponse.success("Item created successfully", ItemMapper.toResponse(item)));
    }

    public String updateItem(Request request, Response response) {
        Long id = Long.parseLong(request.params(":id"));
        log.info("PUT /items/" + id);

        UpdateItemRequest body = gson.fromJson(request.body(), UpdateItemRequest.class);
        if (body == null) throw new BadRequestException("Invalid request body");

        Item item = itemService.updateItem(id, body);
        response.status(200);
        return gson.toJson(ApiResponse.success("Item updated successfully", ItemMapper.toResponse(item)));
    }

    public String deleteItem(Request request, Response response) {
        Long id = Long.parseLong(request.params(":id"));
        log.info("DELETE /items/" + id);

        itemService.deleteItem(id);
        response.status(200);
        return gson.toJson(ApiResponse.success("Item deleted successfully"));
    }

    public String getItemByName(Request request, Response response) {
        String nameParam = request.queryParams("name");
        log.info("GET /items/search" + nameParam);

        Optional<Item> item = itemService.getItemByName(nameParam);

        if(item.isPresent()) {
            ItemResponse itemResponse = ItemMapper.toResponse(item.get());
            response.status(200);
            return gson.toJson(ApiResponse.success("Item Found!", itemResponse));
        }else{
            throw new NotFoundException("Item not found with name: " + nameParam);
        }

    }

    public String updateAvailability(Request request, Response response) {
        Long id = Long.parseLong(request.params(":id"));
        Boolean available = Boolean.parseBoolean(request.queryParams("available"));
        log.info("PATCH /items/{}/availability?available={}", id, available);
        itemService.updateItemAvailability(id, available);
        response.status(200);
        return gson.toJson(ApiResponse.success("Availability updated to " + available));
    }


}
