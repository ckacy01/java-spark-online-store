package org.technoready.routes;


import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.technoready.config.GsonConfig;
import org.technoready.controller.ItemController;
import org.technoready.service.ItemService;
import org.technoready.service.impl.ItemServiceImpl;

import static spark.Spark.*;

/**
 * Items routes configuration
 * Defines and configures all items routes
 */

@Slf4j
public class ItemsRoutes {

    private final ItemController itemController;

    public ItemsRoutes(Jdbi jdbi) {
        ItemService itemService = new ItemServiceImpl(jdbi);
        Gson gson = GsonConfig.getGson();
        this.itemController = new ItemController(itemService, gson);
    }

    /**
     * Configure all item routes
     */
    public void configure() {
        log.info("Configuring ItemRoutes");
        before((request, response) -> response.type("application/json"));

        path("api/v1/items", () -> {
            get("", itemController::getAllItems);

            get("/search", itemController::getItemByName);

            get("/availability", itemController::getAllAvailableItems);

            get("/:id", itemController::getItemById);

            post("", itemController::createItem);

            delete("/:id", itemController::deleteItem);

            put("/:id", itemController::updateItem);

            patch("/availability/:id", itemController::updateAvailability);
        });

        after((request, response) -> response.type("application/json"));
    }
}
