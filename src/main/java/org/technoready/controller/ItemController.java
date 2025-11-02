package org.technoready.controller;


import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.technoready.dto.request.CreateItemRequest;
import org.technoready.dto.request.UpdateItemRequest;
import org.technoready.dto.response.ApiResponse;
import org.technoready.dto.response.ItemResponse;
import org.technoready.entity.Item;
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

        try{
            List<Item> items = itemService.getAllItems();
            List<ItemResponse> itemResponses = ItemMapper.toResponseList(items);

            response.status(200);
            return gson.toJson(ApiResponse.success(itemResponses));
        }catch(Exception e){
            log.error("Error while fetching all items", e);
            response.status(500);
            return gson.toJson(ApiResponse.error("Failed to get items:" + e.getMessage()));
        }
    }

    public String getItemById(Request request, Response response) {
        String idParam =  request.params(":id");
        log.info("GET /items/" + idParam);

        try{
            Long id = Long.parseLong(idParam);
            Optional<Item> item = itemService.getItemById(id);
            if(item.isPresent()) {
                ItemResponse itemResponse = ItemMapper.toResponse(item.get());
                response.status(200);
                return gson.toJson(ApiResponse.success("Item Found!",itemResponse));
            }else{
                response.status(400);
                return gson.toJson(ApiResponse.error("Failed to get item by id:" + id));
            }
        }catch(NumberFormatException e){
                log.error("Error while fetching item by id: " + idParam, e);
                response.status(400);
                return gson.toJson(ApiResponse.error("Failed to parse id:" + idParam));
        }catch(Exception e){
            log.error("Error while fetching item by id: " + idParam, e);
            response.status(500);
            return gson.toJson(ApiResponse.error("Failed to get item by id:" + idParam));
        }
    }

    public String createItem(Request request, Response response) {
        log.info("POST /items");
        try{
            CreateItemRequest createItemRequest = gson.fromJson(request.body(), CreateItemRequest.class);

            if(createItemRequest == null) {
                response.status(400);
                return gson.toJson(ApiResponse.error("Request body is invalid"));
            }

            Item item = itemService.createItem(createItemRequest);
            ItemResponse itemResponse = ItemMapper.toResponse(item);
            response.status(201);
            return gson.toJson(ApiResponse.success("Item Created!",itemResponse));
        }catch(IllegalArgumentException e){
            log.error("Error while creating item: " + e.getMessage(), e);
            response.status(400);
            return gson.toJson(ApiResponse.error(e.getMessage()));
        }catch(Exception e){
            log.error("Error while creating item: " + e.getMessage(), e);
            response.status(500);
            return gson.toJson(ApiResponse.error("Failed to create item: " + e.getMessage()));
        }
    }

    public String updateItem(Request request, Response response) {
        String idParam = request.params(":id");
        log.info("PUT /items/" + idParam);
        try {
            Long id = Long.parseLong(idParam);
            UpdateItemRequest updateItemRequest = gson.fromJson(request.body(), UpdateItemRequest.class);

            if (updateItemRequest == null) {
                response.status(400);
                return gson.toJson(ApiResponse.error("Request body is invalid"));
            }
            Item item = itemService.updateItem(id, updateItemRequest);
            ItemResponse itemResponse = ItemMapper.toResponse(item);
            response.status(200);
            return gson.toJson(ApiResponse.success("Item Updated!", itemResponse));
        } catch (NumberFormatException e) {
            log.error("Error while updating item: " + e.getMessage(), e);
            response.status(400);
            return gson.toJson(ApiResponse.error("Failed to parse id:" + idParam));
        }catch (IllegalArgumentException e){
            log.error("Error while updating item: " + e.getMessage(), e);
            response.status(400);
            return gson.toJson(ApiResponse.error("Failed to parse id:" + idParam));
        }catch(Exception e){
            log.error("Error while updating item: " + e.getMessage(), e);
            response.status(500);
            return gson.toJson(ApiResponse.error("Failed to create item: " + e.getMessage()));
        }
    }

    public String deleteItem(Request request, Response response) {
        String idParam = request.params(":id");
        log.info("DELETE /items/" + idParam);
        try {
            Long id = Long.parseLong(idParam);
            boolean isDeleted = itemService.deleteItem(id);
            if(isDeleted) {
                response.status(200);
                return gson.toJson(ApiResponse.success("Item Deleted!"));
            }else{
                response.status(400);
                return gson.toJson(ApiResponse.error("Failed to delete item by id:" + id));
            }
        }catch(NumberFormatException e){
            log.error("Error while deleting item: " + e.getMessage(), e);
            response.status(400);
            return gson.toJson(ApiResponse.error("Failed to parse id:" + idParam));
        }catch(Exception e){
            log.error("Error while deleting item: " + e.getMessage(), e);
            response.status(500);
            return gson.toJson(ApiResponse.error("Failed to delete item: " + e.getMessage()));
        }
    }


}
