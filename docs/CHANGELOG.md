# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.4.0] - 2025-11-07

### Added
- **Web Sockets**: Implementing web sockets to have real-time communication between the views and the backend.
- **Filtering**: Adding price filtering (Max and Min).
- **Offers status**: Now the offers status updates automatically.

### Notes
- This version introduces real-time communication using web sockets, now the views updates automatically.

## [1.3.0] — 2025-11-03

### Added
- Created Mustache views to connect with the backend.
- Added JS scripts to communicate with API endpoints.
- Added `WebViewController` and `WebRoutes` to manage web views.
- Included `main.css` for improved UI styling.

### Notes
- This version introduces the first visual frontend integration with the backend REST API.

## [1.2.0] - 2025-11-03

### Added
- **Offers Management System**
    - Full CRUD support for offers (create, read, update, delete)
    - Introduced the `Offer` entity with complete data structure
    - Added service, DAO, controller, and DTO layers:
        - `CreateOfferRequest`, `OfferResponse`, `OfferWithDetailsResponse`, `AcceptOfferRequest`
    - `BigDecimal` used for precise monetary value handling
    - Integrated offer acceptance logic within `OfferService`

- **Item Enhancements**
    - `Item` entities now include live aggregated data:
        - `totalOffers`: total number of offers
        - `highestOffer`: highest pending offer amount
    - DAO queries now fetch these values directly from the database using optimized SQL with `LEFT JOIN` and aggregation
    - `ItemDao` methods `findAll()`, `findAllAvailable()`, and `findById()` now return enriched item data

- **Search Functionality**
    - Implemented item search by name with case-insensitive partial matching
    - Added endpoint: `GET /items/search?name=xxx`

- **Database Enhancements**
    - Added `offers` table and improved `items` table indexing
    - Added sample data with 7 authentic celebrity collectibles
    - Indexes on `name`, `description`, and `price` columns
    - Conditional inserts to prevent duplicate seed data

- **API Endpoints (Offers)**
  - `GET /offers` — Retrieve all offers
  - `GET /offers/:id` — Get offer by ID
  - `GET /offers/item/:itemId` — Get all offers for a specific item
  - `POST /offers` — Create a new offer
  - `PUT /offers/:id` — Update an existing offer
  - `DELETE /offers/:id` — Delete an offer
  - `POST /offers/accept` — Accept an offer using `AcceptOfferRequest` payload
---

### Changed
- **Application**
    - Bumped version to `1.2.0`

- **ItemService & ItemDao**
    - Removed redundant service-level calls to `getOfferCountForItem()` and `getHighestOfferForItem()`
    - Simplified business logic — now relies on DAO-level SQL aggregation
    - Improved logging with standardized `log.debug` and `log.info` messages
    - Added consistent exception handling (`ConflictException`, `NotFoundException`)

- **Database Schema**
    - Added `offers` table with `status` column (e.g., `PENDING`, `ACCEPTED`, `REJECTED`)
    - Updated `items` table mappings to support `totalOffers` and `highestOffer`
    - Improved relationships between `items` and `offers`

- **API Documentation**
    - Updated `docs/API.md` with new offer endpoints and item search examples
    - Expanded `docs/DATABASE.md` with updated schema diagrams and relationships

- **Validation Rules**
    - Price validation: must be greater than 0, maximum 2 decimal places (`DECIMAL(10,2)`)
    - Name validation: required, maximum length of 50 characters

---

### Technical Details
- `ItemDao` now performs SQL aggregation directly:
    - `COUNT(o.id)` for `total_offers`
    - `COALESCE(MAX(o.offer_amount), 0)` for `highest_offer`
- Automatic mapping between SQL aliases (`total_offers`, `highest_offer`) and Java fields (`totalOffers`, `highestOffer`) via JDBI
- Optimized performance by avoiding N+1 query patterns when loading items
- Queries tested for compatibility with **PostgreSQL** and **MySQL**
- Improved exception consistency and error handling throughout service and DAO layers
- Prepared unit tests for offer and item integration (create, read, accept, etc.)

---

### Deprecated
- Direct usage of `ItemService.getOfferCountForItem()` and `getHighestOfferForItem()` is now deprecated  
  (methods remain available for isolated use if needed)
- The endpoint `OPTIONS /items/:id` remains available but is now superseded by standard `HEAD` or `GET` checks

---

## [1.1.0] - 2025-11-02

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

## [1.0.0] - 2025-10-31

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
