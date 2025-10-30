package org.technoready.routes;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.technoready.config.GsonConfig;
import org.technoready.controller.UserController;
import org.technoready.service.UserService;
import org.technoready.service.impl.UserServiceImpl;

import static spark.Spark.*;

/**

 User Routes Configuration
 Defines and configures all user-related routes
 Follows Single Responsibility Principle - only handles route configuration
 Acts as composition root for dependency injection */
@Slf4j
public class UserRoutes {

    private final UserController userController;

    public UserRoutes(Jdbi jdbi) {
        // Dependency injection - compose dependencies here
        UserService userService = new UserServiceImpl(jdbi);
        Gson gson = GsonConfig.getGson();
        this.userController = new UserController(userService, gson);
    }

    /**
     * Configure all user routes
     */
    public void configure() {
        log.info("Configuring user routes...");
        before((req, res) -> res.type("application/json"));
        // Base path for all user routes
        path("/users", () -> {

            // GET /users - Retrieve all users
            get("", userController::getAllUsers);

            // GET /users/:id - Retrieve user by ID
            get("/:id", userController::getUserById);

            // POST /users - Create new user
            post("", userController::createUser);

        });

        after("/*", (request, response) -> response.type("application/json"));

        log.info("User routes configured successfully");
    }

}
