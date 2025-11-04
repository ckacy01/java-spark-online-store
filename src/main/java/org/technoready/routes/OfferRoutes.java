package org.technoready.routes;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.technoready.config.GsonConfig;
import org.technoready.controller.OfferController;
import org.technoready.service.OfferService;
import org.technoready.service.impl.OfferServiceImpl;

import static spark.Spark.*;

@Slf4j
public class OfferRoutes {

    private final OfferController offerController;

    public OfferRoutes(Jdbi jdbi) {
        OfferService offerService = new OfferServiceImpl(jdbi);
        Gson gson = GsonConfig.getGson();
        this.offerController = new OfferController(offerService, gson);
    }

    /**
     * Configure all offer routes
     */
    public void configure() {
        log.info("Configuring offer routes...");

        path("api/v1/offers", () -> {

            before("/*", (request, response) -> {
                response.type("application/json");
            });

            // GET /offers - Retrieve all offers (with optional ?details=true)
            get("", offerController::getAllOffers);

            // GET /offers/:id - Retrieve offer by ID (with optional ?details=true)
            get("/:id", offerController::getOfferById);

            // GET /offers/item/:itemId - Get offers by item (with optional ?details=true&status=PENDING)
            get("/item/:itemId", offerController::getOffersByItemId);

            // GET /offers/user/:userId - Get offers by user (with optional ?details=true&status=PENDING)
            get("/user/:userId", offerController::getOffersByUserId);

            // POST /offers - Create new offer
            post("", offerController::createOffer);

            // PUT /offers/:id/accept - Accept offer
            put("/:id/accept", offerController::acceptOffer);

            // PUT /offers/:id/reject - Reject offer
            put("/:id/reject", offerController::rejectOffer);

            // DELETE /offers/:id - Delete offer
            delete("/:id", offerController::deleteOffer);

            after("/*", (request, response) -> {
                response.type("application/json");
            });
        });

        log.info("Offer routes configured successfully");
    }
}
