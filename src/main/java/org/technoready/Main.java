package org.technoready;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.technoready.config.DatabaseConfig;
import org.technoready.config.EnvConfig;
import org.technoready.routes.ItemsRoutes;
import org.technoready.routes.UserRoutes;

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

            // Load configuration
            EnvConfig config = EnvConfig.load(dotenv);
            log.info("Configuration loaded successfully");

            // Initialize database
            Jdbi jdbi = DatabaseConfig.initialize(config);
            log.info("Database initialized successfully");

            DatabaseConfig.runSchema(jdbi);

            // Configure Spark
            port(config.getServerPort());

            // Configure routes
            UserRoutes userRoutes = new UserRoutes(jdbi);
            userRoutes.configure();
            ItemsRoutes itemsRoutes = new ItemsRoutes(jdbi);
            itemsRoutes.configure();

            log.info("Application started successfully on port {}", config.getServerPort());

        } catch (Exception e) {
            log.error("Failed to start application", e);
            System.exit(1);
        }
    }
    }