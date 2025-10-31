package org.technoready.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Database Configuration
 * Configures HikariCP connection pool and JDBI
 * Follows Dependency Inversion Principle (DIP) - depends on abstractions (Jdbi, DataSource)
 */
@Slf4j
public class DatabaseConfig {

    public static Jdbi initialize(EnvConfig config) {
        log.info("Initializing database configuration...");

        DataSource dataSource = createDataSource(config);
        Jdbi jdbi = Jdbi.create(dataSource);

        // Install plugins
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.installPlugin(new PostgresPlugin());

        log.info("JDBI configured with PostgreSQL plugin");

        log.info("JDBI configured with PostgreSQL plugin and snake_case mapping");

        return jdbi;
    }

    private static DataSource createDataSource(EnvConfig config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getDbUrl());
        hikariConfig.setUsername(config.getDbUsername());
        hikariConfig.setPassword(config.getDbPassword());
        hikariConfig.setDriverClassName(config.getDbDriver());
        hikariConfig.setMaximumPoolSize(config.getHikariMaximumPoolSize());
        hikariConfig.setMinimumIdle(config.getHikariMinimumIdle());
        hikariConfig.setConnectionTimeout(config.getHikariConnectionTimeout());
        hikariConfig.setIdleTimeout(config.getHikariIdleTimeout());
        hikariConfig.setMaxLifetime(config.getHikariMaxLifetime());
        hikariConfig.setPoolName("CollectiblesHikariPool");

        return new HikariDataSource(hikariConfig);
    }

    public static void runSchema(Jdbi jdbi) {
        log.info("Executing database schema...");

        try {
            // Read schema.sql from resources
            InputStream inputStream = DatabaseConfig.class
                    .getClassLoader()
                    .getResourceAsStream("db/schema.sql");

            if (inputStream == null) {
                log.warn("schema.sql file not found in resources/db/");
                return;
            }

            String schema = new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .collect(Collectors.joining("\n"));

            // Execute schema
            jdbi.useHandle(handle -> handle.execute(schema));

            log.info("Database schema executed successfully");

        } catch (Exception e) {
            log.error("Error executing database schema", e);
            throw new RuntimeException("Failed to execute database schema", e);
        }
    }
}