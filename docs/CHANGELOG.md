# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.1.0] - 2025-10-29

### Added
- **Items Management System**
    - Complete CRUD operations for collectible items
    - Item entity with name, description, and price fields
    - BigDecimal support for precise price handling
    - ItemDao, ItemService, and ItemController layers
    - Item DTOs (CreateItemRequest, UpdateItemRequest, ItemResponse)
    - ItemMapper utility for entity/DTO conversion

- **Search Functionality**
    - Search items by name (case-insensitive, partial match)
    - GET /items/search endpoint with query parameters

- **Database Enhancements**
    - New `items` table with proper indexing
    - Sample data with 7 authentic celebrity collectibles
    - Indexes on name, description, and price columns
    - Conditional inserts to prevent duplicate data

- **API Endpoints** (Items)
    - GET /items - Retrieve all items
    - GET /items/:id - Get item by ID
    - GET /items/search?name=xxx - Search items
    - POST /items - Create new item
    - PUT /items/:id - Update item
    - DELETE /items/:id - Delete item
    - OPTIONS /items/:id - Check item existence

### Changed
- Updated Application.java to version 1.1.0
- Enhanced database schema with items table
- Updated API documentation with items endpoints
- Improved database documentation with items schema

### Technical Details
- Items table uses DECIMAL(10,2) for precise price storage
- Automatic price validation (must be > 0, max 2 decimals)
- Name validation (required, max 50 characters)
- Enhanced error handling for item operations

## [1.0.0] - 2025-10-28

### Added
- **Initial Release**
- RESTful API with Spark Framework
- User management system (CRUD operations)
- PostgreSQL database integration
- HikariCP connection pooling
- Environment configuration via .env files
- JDBI 3 for database operations
- Gson JSON serialization with LocalDateTime support
- Automatic camelCase to snake_case mapping
- Comprehensive logging with Logback
- CORS support
- Global exception handling
- SOLID principles architecture
- Layered architecture (Controller → Service → DAO)

- **User Endpoints**
    - GET /users - Retrieve all users
    - GET /users/:id - Get user by ID
    - POST /users - Create new user
    - PUT /users/:id - Update user
    - DELETE /users/:id - Delete user
    - OPTIONS /users/:id - Check user existence

- **Database**
    - Users table with proper indexing
    - Automatic schema initialization
    - Sample user data

- **Documentation**
    - Comprehensive README.md
    - API documentation
    - Database schema documentation
    - JavaDoc comments

### Technical Stack
- Java 17+
- Spark Framework 2.9.4
- PostgreSQL 12+
- JDBI 3.41.3
- HikariCP 5.1.0
- Lombok 1.18.30
- Gson 2.10.1
- Logback 1.4.14
- dotenv-java 3.0.0