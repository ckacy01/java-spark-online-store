package org.technoready.exception;

import com.google.gson.Gson;
import org.technoready.dto.response.ApiResponse;
import org.technoready.exception.BadRequestException;
import org.technoready.exception.NotFoundException;
import org.technoready.exception.ConflictException;

import static spark.Spark.exception;

public class GlobalExceptionHandler {

    private static final Gson gson = new Gson();

    public static void register() {

        exception(NotFoundException.class, (ex, req, res) -> {
            res.status(404);
            res.type("application/json");
            res.body(gson.toJson(ApiResponse.error(ex.getMessage())));
        });


        exception(BadRequestException.class, (ex, req, res) -> {
            res.status(400);
            res.type("application/json");
            res.body(gson.toJson(ApiResponse.error(ex.getMessage())));
        });

        exception(ConflictException.class, (ex, req, res) -> {
            res.status(409);
            res.type("application/json");
            res.body(gson.toJson(ApiResponse.error(ex.getMessage())));
        });

        exception(Exception.class, (ex, req, res) -> {
            res.status(500);
            res.type("application/json");
            res.body(gson.toJson(ApiResponse.error("Internal server error: " + ex.getMessage())));
            ex.printStackTrace();
        });
    }
}

