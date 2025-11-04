package org.technoready;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.technoready.config.DatabaseConfig;
import org.technoready.config.EnvConfig;
import org.technoready.exception.GlobalExceptionHandler;
import org.technoready.routes.ItemsRoutes;
import org.technoready.routes.OfferRoutes;
import org.technoready.routes.UserRoutes;
import org.technoready.routes.WebRoutes;

import static spark.Spark.*;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("Starting Online Store Application...");

        try {
            // Load environment variables
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();
            // Serve static files (CSS, JS, images)
            staticFiles.location("/public");
            staticFiles.expireTime(600); // 10 minutes cache
            // Load configuration
            EnvConfig config = EnvConfig.load(dotenv);
            log.info("Configuration loaded successfully");

            // Initialize database
            Jdbi jdbi = DatabaseConfig.initialize(config);
            log.info("Database initialized successfully");

            DatabaseConfig.runSchema(jdbi);

            // Initialize Exceptions
            GlobalExceptionHandler.register();

            // Configure Spark
            port(config.getServerPort());

            // Configure routes
            UserRoutes userRoutes = new UserRoutes(jdbi);
            userRoutes.configure();
            ItemsRoutes itemsRoutes = new ItemsRoutes(jdbi);
            itemsRoutes.configure();
            OfferRoutes offerRoutes = new OfferRoutes(jdbi);
            offerRoutes.configure();

            WebRoutes webRoutes = new WebRoutes(jdbi);
            webRoutes.configure();

            before((req, res) -> {
                if (req.pathInfo().startsWith("/api/")) {
                    res.type("application/json");
                } else {
                    res.type("text/html; charset=utf-8");
                }
            });

            log.info("Application started successfully on port {}", config.getServerPort());

        } catch (Exception e) {
            log.error("Failed to start application", e);
            System.exit(1);
        }
    }
}
