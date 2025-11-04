package org.technoready.routes;

import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.technoready.controller.WebViewController;
import org.technoready.service.ItemService;
import org.technoready.service.OfferService;
import org.technoready.service.UserService;
import org.technoready.service.impl.ItemServiceImpl;
import org.technoready.service.impl.OfferServiceImpl;
import org.technoready.service.impl.UserServiceImpl;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.*;

/**
 * Web Routes Configuration
 * Configures all web (Mustache) routes
 * @version 1.2.0
 */
@Slf4j
public class WebRoutes {

    private final WebViewController webViewController;
    private final MustacheTemplateEngine engine;

    public WebRoutes(Jdbi jdbi) {
        ItemService itemService = new ItemServiceImpl(jdbi);
        OfferService offerService = new OfferServiceImpl(jdbi);
        UserService userService = new UserServiceImpl(jdbi);

        this.webViewController = new WebViewController(itemService, offerService, userService);
        this.engine = new MustacheTemplateEngine();
    }

    public void configure() {
        log.info("Configuring Web Routes (Mustache templates)...");

        // ====================
        // WEB PAGES (Mustache Templates)
        // ====================

        // Home page - List all available items
        get("/", webViewController::getHomePage, engine);
        get("/home", webViewController::getHomePage, engine);

        // Items list (alternative route)
        get("/items", webViewController::getItemsPage, engine);

        // Item detail page with offers and offer form
        get("/item/:id", webViewController::getItemDetail, engine);

        // User's offers page
        get("/my-offers/:userId", webViewController::getMyOffersPage, engine);

        // Users list (for selecting user profile)
        get("/users", webViewController::getUsersPage, engine);

        // ====================
        // ERROR HANDLING
        // ====================

        notFound((req, res) -> {
            res.type("text/html");
            return "<html><body><h1>404 - Page Not Found</h1>" +
                    "<p>The page you're looking for doesn't exist.</p>" +
                    "<a href='/'>Go to Home</a></body></html>";
        });

        log.info("Web routes configured successfully");
        log.info("Available pages:");
        log.info("  - /               (Home - Items list)");
        log.info("  - /item/:id       (Item detail with offers)");
        log.info("  - /my-offers/:userId (User's offers)");
        log.info("  - /users          (Users list)");
    }
}

