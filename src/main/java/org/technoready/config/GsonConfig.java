package org.technoready.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.technoready.util.LocalDateTimeAdapter;

import java.time.LocalDateTime;

/**
 * Gson Configuration
 * Centralized Gson instance configuration with custom adapters
 * Follows Single Responsibility Principle
 */
public class GsonConfig {

    private static Gson gson;

    /**
     * Get configured Gson instance (Singleton pattern)
     */
    public static Gson getGson() {
        if (gson == null) {
            gson = createGson();
        }
        return gson;
    }

    /**
     * Create and configure Gson instance with custom adapters
     */
    private static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    /**
     * Create a new Gson instance (for testing or special cases)
     */
    public static Gson createNewGson() {
        return createGson();
    }
}

