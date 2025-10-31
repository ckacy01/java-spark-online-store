package org.technoready.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


/**
 * Environment Configuration Manager
 * Loads and provides access to environment variables from .env file
 * Follows Single Responsibility Principle (SRP)
 */
@Slf4j
@Getter
public class EnvConfig {

    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;
    private final String dbDriver;
    private final int hikariMaximumPoolSize;
    private final int hikariMinimumIdle;
    private final long hikariConnectionTimeout;
    private final long hikariIdleTimeout;
    private final long hikariMaxLifetime;
    private final int serverPort;

    private EnvConfig(Dotenv dotenv) {
        // Database Configuration
        this.dbUrl = dotenv.get("DB_URL");
        this.dbUsername = dotenv.get("DB_USER");
        this.dbPassword = dotenv.get("DB_PASSWORD");
        this.dbDriver = dotenv.get("DB_DRIVER");

        // HikariCP configuration
        this.hikariMaximumPoolSize = Integer.parseInt(
                dotenv.get("HikariCP_MaxPoolSize", "10"));
        this.hikariMinimumIdle = Integer.parseInt(
                dotenv.get("HikariCP_MinPoolSize", "5"));
        this.hikariConnectionTimeout = Long.parseLong(
                dotenv.get("HikariCP_IdleTimeout", "30000"));
        this.hikariIdleTimeout = Long.parseLong(
                dotenv.get("HikariCP_ConnectionTimeout", "155000"));
        this.hikariMaxLifetime = Long.parseLong(
                dotenv.get("HikariCP_MaxLifetime", "1800000"));

        // Server Configuration
        this.serverPort = Integer.parseInt(
                dotenv.get("SERVER_PORT"));
    }

    public static EnvConfig load(Dotenv dotenv) {
        log.info("Loading application configuration...");
        if(dotenv == null){
            throw new IllegalStateException("Environment variables cannot be null.");
        }
        EnvConfig envConfig = new EnvConfig(dotenv);
        log.info("Loaded application configuration.");

        return envConfig;
    }
}
