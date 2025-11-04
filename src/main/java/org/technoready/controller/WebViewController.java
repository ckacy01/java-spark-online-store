package org.technoready.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.technoready.entity.Item;
import org.technoready.entity.OfferWithDetails;
import org.technoready.entity.User;
import org.technoready.service.ItemService;
import org.technoready.service.OfferService;
import org.technoready.service.UserService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Web View Controller
 * Handles rendering of Mustache templates
 * @version 1.2.0
 */
@Slf4j
@RequiredArgsConstructor
public class WebViewController {

    private final ItemService itemService;
    private final OfferService offerService;
    private final UserService userService;

    /**
     * GET / - Home page with all available items
     */
    public ModelAndView getHomePage(Request request, Response response) {
        log.info("GET / - Rendering home page");
        response.type("text/html; charset=utf-8");

        try {
            // Get all available items with stats
            List<Item> items = itemService.getAllAvailableItems();

            List<Map<String, Object>> itemsWithStats = items.stream()
                    .map(item -> {
                        Map<String, Object> itemMap = new HashMap<>();
                        itemMap.put("id", item.getId());
                        itemMap.put("name", item.getName());
                        itemMap.put("description", item.getDescription());
                        itemMap.put("price", String.format("%.2f", item.getPrice()));
                        itemMap.put("currentPrice", String.format("%.2f", item.getCurrentPrice()));
                        itemMap.put("originalPrice", String.format("%.2f", item.getOriginalPrice()));

                        // Get offer stats
                        int totalOffers = itemService.getOfferCountForItem(item.getId());
                        BigDecimal highestOffer = itemService.getHighestOfferForItem(item.getId());

                        itemMap.put("totalOffers", totalOffers);
                        itemMap.put("highestOffer", highestOffer != null ? String.format("%.2f", highestOffer) : "0.00");
                        itemMap.put("hasOffers", totalOffers > 0);

                        return itemMap;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> model = new HashMap<>();
            model.put("title", "Technoready Collectibles - Available Items");
            model.put("items", itemsWithStats);
            model.put("hasItems", !itemsWithStats.isEmpty());

            return new ModelAndView(model, "index.mustache");

        } catch (Exception e) {
            log.error("Error rendering home page", e);
            Map<String, Object> errorModel = new HashMap<>();
            errorModel.put("error", "Error loading items: " + e.getMessage());
            return new ModelAndView(errorModel, "error.mustache");
        }
    }

    /**
     * GET /items - List all items (same as home but different template)
     */
    public ModelAndView getItemsPage(Request request, Response response) {
        log.info("GET /items - Rendering items list");
        return getHomePage(request, response);
    }

    /**
     * GET /item/:id - Item detail page with offers
     */
    public ModelAndView getItemDetail(Request request, Response response) {
        String idParam = request.params(":id");
        log.info("GET /item/{} - Rendering item detail", idParam);
        response.type("text/html; charset=utf-8");

        try {
            Long itemId = Long.parseLong(idParam);

            // Get item
            Item item = itemService.getItemById(itemId)
                    .orElseThrow(() -> new IllegalArgumentException("Item not found"));

            // Get item stats
            int totalOffers = itemService.getOfferCountForItem(itemId);
            BigDecimal highestOffer = itemService.getHighestOfferForItem(itemId);

            // Get all offers with details
            List<OfferWithDetails> offers = offerService.getOffersByItemIdWithDetails(itemId);

            List<Map<String, Object>> offersData = offers.stream()
                    .map(offer -> {
                        Map<String, Object> offerMap = new HashMap<>();
                        offerMap.put("id", offer.getId());
                        offerMap.put("offerAmount", String.format("%.2f", offer.getOfferAmount()));
                        offerMap.put("status", offer.getStatus().name());
                        offerMap.put("message", offer.getMessage());
                        offerMap.put("createdAt", offer.getCreatedAt().toString());
                        offerMap.put("username", offer.getUsername());
                        offerMap.put("userFullName", offer.getUserFullName());

                        // Status styling
                        offerMap.put("isPending", "PENDING".equals(offer.getStatus().name()));
                        offerMap.put("isAccepted", "ACCEPTED".equals(offer.getStatus().name()));
                        offerMap.put("isRejected", "REJECTED".equals(offer.getStatus().name()));
                        offerMap.put("isOutbid", "OUTBID".equals(offer.getStatus().name()));

                        return offerMap;
                    })
                    .collect(Collectors.toList());

            // Get all users for dropdown
            List<User> users = userService.getAllUsers();
            List<Map<String, Object>> usersData = users.stream()
                    .map(user -> {
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("id", user.getId());
                        userMap.put("username", user.getUsername());
                        userMap.put("fullName", user.getFullName());
                        return userMap;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> model = new HashMap<>();
            model.put("title", item.getName() + " - Item Details");

            // Item data
            Map<String, Object> itemData = new HashMap<>();
            itemData.put("id", item.getId());
            itemData.put("name", item.getName());
            itemData.put("description", item.getDescription());
            itemData.put("price", String.format("%.2f", item.getPrice()));
            itemData.put("currentPrice", String.format("%.2f", item.getCurrentPrice()));
            itemData.put("originalPrice", String.format("%.2f", item.getOriginalPrice()));
            itemData.put("isAvailable", item.isAvailable());
            itemData.put("totalOffers", totalOffers);
            itemData.put("highestOffer", highestOffer != null ? String.format("%.2f", highestOffer) : "0.00");

            model.put("item", itemData);
            model.put("offers", offersData);
            model.put("hasOffers", !offersData.isEmpty());
            model.put("users", usersData);
            model.put("canMakeOffer", item.isAvailable());

            return new ModelAndView(model, "item-detail.mustache");

        } catch (NumberFormatException e) {
            log.error("Invalid item id: {}", idParam);
            Map<String, Object> errorModel = new HashMap<>();
            errorModel.put("error", "Invalid item ID");
            return new ModelAndView(errorModel, "error.mustache");

        } catch (Exception e) {
            log.error("Error rendering item detail", e);
            Map<String, Object> errorModel = new HashMap<>();
            errorModel.put("error", "Error loading item: " + e.getMessage());
            return new ModelAndView(errorModel, "error.mustache");
        }
    }

    /**
     * GET /my-offers/:userId - User's offers page
     */
    public ModelAndView getMyOffersPage(Request request, Response response) {
        String userIdParam = request.params(":userId");
        log.info("GET /my-offers/{} - Rendering user offers", userIdParam);
        response.type("text/html; charset=utf-8");

        try {
            Long userId = Long.parseLong(userIdParam);

            // Get user
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Get user's offers with details
            List<OfferWithDetails> offers = offerService.getOffersByUserIdWithDetails(userId);

            List<Map<String, Object>> offersData = offers.stream()
                    .map(offer -> {
                        Map<String, Object> offerMap = new HashMap<>();
                        offerMap.put("id", offer.getId());
                        offerMap.put("offerAmount", String.format("%.2f", offer.getOfferAmount()));
                        offerMap.put("status", offer.getStatus().name());
                        offerMap.put("message", offer.getMessage());
                        offerMap.put("createdAt", offer.getCreatedAt().toString());

                        // Item info
                        offerMap.put("itemId", offer.getItemId());
                        offerMap.put("itemName", offer.getItemName());
                        offerMap.put("itemCurrentPrice", String.format("%.2f", offer.getItemCurrentPrice()));
                        offerMap.put("itemIsAvailable", offer.getItemIsAvailable());

                        // Status flags
                        offerMap.put("isPending", "PENDING".equals(offer.getStatus().name()));
                        offerMap.put("isAccepted", "ACCEPTED".equals(offer.getStatus().name()));
                        offerMap.put("isRejected", "REJECTED".equals(offer.getStatus().name()));
                        offerMap.put("isOutbid", "OUTBID".equals(offer.getStatus().name()));

                        return offerMap;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> model = new HashMap<>();
            model.put("title", "My Offers - " + user.getUsername());
            model.put("user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "fullName", user.getFullName()
            ));
            model.put("offers", offersData);
            model.put("hasOffers", !offersData.isEmpty());

            return new ModelAndView(model, "my-offers.mustache");

        } catch (Exception e) {
            log.error("Error rendering user offers", e);
            Map<String, Object> errorModel = new HashMap<>();
            errorModel.put("error", "Error loading offers: " + e.getMessage());
            return new ModelAndView(errorModel, "error.mustache");
        }
    }

    /**
     * GET /users - List all users
     */
    public ModelAndView getUsersPage(Request request, Response response) {
        log.info("GET /users - Rendering users list");
        response.type("text/html; charset=utf-8");

        try {
            List<User> users = userService.getAllUsers();

            List<Map<String, Object>> usersData = users.stream()
                    .map(user -> {
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("id", user.getId());
                        userMap.put("username", user.getUsername());
                        userMap.put("email", user.getEmail());
                        userMap.put("fullName", user.getFullName());
                        return userMap;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> model = new HashMap<>();
            model.put("title", "Users - Select Your Profile");
            model.put("users", usersData);
            model.put("hasUsers", !usersData.isEmpty());

            return new ModelAndView(model, "users.mustache");

        } catch (Exception e) {
            log.error("Error rendering users page", e);
            Map<String, Object> errorModel = new HashMap<>();
            errorModel.put("error", "Error loading users: " + e.getMessage());
            return new ModelAndView(errorModel, "error.mustache");
        }
    }
}
