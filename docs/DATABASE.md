# Database Documentation

# Database Documentation

## Overview

The Order Management System uses PostgreSQL as its relational database. The database is automatically initialized on application startup using the `schema.sql` script.

## Database Schema

### Users Table

**Table Name:** `users`

**Description:** Stores user information for the e-commerce platform.

| Column     | Type       | Constraints        | Default           | Description              |
|------------|-----------|------------------|-----------------|--------------------------|
| id         | BIGSERIAL | PRIMARY KEY       | AUTO             | Unique user identifier    |
| username   | VARCHAR(50)| NOT NULL, UNIQUE | -                | User's unique username    |
| email      | VARCHAR(100)| NOT NULL        | -                | User's email address      |
| full_name  | VARCHAR(100)| NOT NULL        | -                | User's full name          |
| created_at | TIMESTAMP | NOT NULL          | CURRENT_TIMESTAMP| Record creation timestamp |
| updated_at | TIMESTAMP | NOT NULL          | CURRENT_TIMESTAMP| Last update timestamp     |

#### Users Table Indexes

1. **idx_users_username**
    - Column: `username`
    - Type: B-tree
    - Purpose: Fast username lookups and uniqueness enforcement

2. **idx_users_email**
    - Column: `email`
    - Type: B-tree
    - Purpose: Fast email searches

3. **idx_users_created_at**
    - Column: `created_at`
    - Type: B-tree
    - Purpose: Efficient chronological queries and sorting

---

### Items Table

**Table Name:** `items`

**Description:** Stores collectible items available in the online store.

| Column        | Type        | Constraints        | Default           | Description                        |
|---------------|------------|------------------|-----------------|------------------------------------|
| id            | BIGSERIAL  | PRIMARY KEY       | AUTO             | Unique item identifier              |
| name          | VARCHAR(50)| NOT NULL, UNIQUE | -                | Item name                           |
| description   | TEXT       | NULL              | -                | Detailed item description           |
| price         | DECIMAL(10,2) | NOT NULL       | -                | Original price of the item          |
| current_price | DECIMAL(10,2) | NOT NULL       | -                | Current price (may change with offers) |
| original_price| DECIMAL(10,2) | NOT NULL       | -                | Stores original price before changes |
| is_available  | BOOLEAN    | NOT NULL          | TRUE             | Availability of the item            |
| created_at    | TIMESTAMP  | NOT NULL          | CURRENT_TIMESTAMP| Record creation timestamp           |
| updated_at    | TIMESTAMP  | NOT NULL          | CURRENT_TIMESTAMP| Last update timestamp               |
| total_offers  | INTEGER    | NULL              | 0                | Number of offers associated         |
| highest_offer | DECIMAL(10,2) | NULL           | 0                | Highest pending offer amount        |

#### Items Table Indexes

1. **idx_items_name**
    - Column: `name`
    - Type: B-tree
    - Purpose: Fast name lookups and search operations

2. **idx_items_description**
    - Column: `description`
    - Type: B-tree
    - Purpose: Full-text search capabilities

3. **idx_items_price**
    - Column: `price`
    - Type: B-tree
    - Purpose: Price-based queries and sorting

---

### Offers Table (NEW in v1.2.0)

**Table Name:** `offers`

**Description:** Stores offers made by users on items.

| Column       | Type         | Constraints        | Default           | Description                         |
|--------------|-------------|------------------|-----------------|-------------------------------------|
| id           | BIGSERIAL   | PRIMARY KEY       | AUTO             | Unique offer identifier              |
| item_id      | BIGINT      | NOT NULL, FK      | -                | Foreign key referencing `items(id)` |
| user_id      | BIGINT      | NOT NULL, FK      | -                | Foreign key referencing `users(id)` |
| offer_amount | DECIMAL(10,2)| NOT NULL         | -                | Amount offered by the user           |
| status       | VARCHAR(20) | NOT NULL          | 'PENDING'        | Offer status: PENDING, ACCEPTED, REJECTED |
| created_at   | TIMESTAMP   | NOT NULL          | CURRENT_TIMESTAMP| Record creation timestamp            |
| updated_at   | TIMESTAMP   | NOT NULL          | CURRENT_TIMESTAMP| Last update timestamp                |

#### Offers Table Indexes

1. **idx_offers_item_id**
    - Column: `item_id`
    - Type: B-tree
    - Purpose: Fast lookup of offers for a specific item

2. **idx_offers_user_id**
    - Column: `user_id`
    - Type: B-tree
    - Purpose: Retrieve all offers by a specific user

3. **idx_offers_status**
    - Column: `status`
    - Type: B-tree
    - Purpose: Fast filtering by offer status

---

## Entity-Relationship Diagram

```
┌─────────────────────────────┐
│          USERS              │
├─────────────────────────────┤
│ PK  id (BIGSERIAL)          │
│ U   username (VARCHAR(50))  │
│     email (VARCHAR(100))    │
│     full_name (VARCHAR(100))│
│     created_at (TIMESTAMP)  │
│     updated_at (TIMESTAMP)  │
└─────────────────────────────┘
┌─────────────────────────────┐
│ ITEMS                       │
├─────────────────────────────┤
│ PK id (BIGSERIAL)           │
│ name (VARCHAR(50))          │
│ description (TEXT)          │
│ price (DECIMAL(10,2))       │
│ current_price (DECIMAL)     │
│ original_price (DECIMAL)    │
│ is_available (BOOLEAN)      │
│ created_at (TIMESTAMP)      │
│ updated_at (TIMESTAMP)      │
│ total_offers (INTEGER)      │
│ highest_offer (DECIMAL)     │
└─────────────────────────────┘

┌─────────────────────────────┐
│ OFFERS                      │
├─────────────────────────────┤
│ PK id (BIGSERIAL)           │
│ FK item_id (BIGINT)         │
│ FK user_id (BIGINT)         │
│ offer_amount (DECIMAL)      │
│ status (VARCHAR(20))        │
│ created_at (TIMESTAMP)      │
│ updated_at (TIMESTAMP)      │
└─────────────────────────────┘

```

## Data Types Mapping

### Java to PostgreSQL

| Java Type | PostgreSQL Type | Notes |
|-----------|-----------------|-------|
| Long | BIGSERIAL / BIGINT | Primary keys use BIGSERIAL |
| String | VARCHAR(n) | Variable length strings |
| LocalDateTime | TIMESTAMP | Date and time without timezone |

### PostgreSQL to Java

| PostgreSQL Type | Java Type | JDBI Mapping |
|-----------------|-----------|--------------|
| BIGSERIAL | Long | Automatic |
| VARCHAR(n) | String | Automatic |
| TIMESTAMP | LocalDateTime | Via PostgresPlugin |

## Column Naming Convention

The application uses automatic mapping between Java camelCase and PostgreSQL snake_case:

| Java (camelCase) | PostgreSQL (snake_case) |
|------------------|-------------------------|
| id | id |
| username | username |
| email | email |
| fullName | full_name |
| createdAt | created_at |
| updatedAt | updated_at |

This mapping is configured in `DatabaseConfig.java`:
```java
jdbi.getConfig(ReflectionMappers.class)
    .setCaseChange(CaseStrategy.LOWER_SNAKE_CASE);
```

## Sample Data

The `schema.sql` script includes sample data for testing:
```sql
INSERT INTO users (username, email, full_name) VALUES
    ('rafael', 'rafael@example.com', 'Rafael García'),
    ('sofia', 'sofia@example.com', 'Sofía Martínez'),
    ('ramon', 'ramon@example.com', 'Ramón Collector');
...
```

## Database Operations

### Create Database
```bash
psql -U postgres
CREATE DATABASE onlinestore;
\q
```

### Initialize Schema
```bash
psql -U postgres -d onlinestore -f src/main/resources/db/schema.sql
```

### Verify Tables
```sql
-- Connect to database
psql -U postgres -d onlinestore

-- List tables
\dt

-- Describe users table
\d users

-- View indexes
\di

-- View data
SELECT * FROM users;
```

### Backup and Restore

**Backup:**
```bash
pg_dump -U postgres onlinestore > backup_$(date +%Y%m%d).sql
```

**Restore:**
```bash
psql -U postgres -d onlinestore < backup_20251029.sql
```

### Useful Queries

**Count users:**
```sql
SELECT COUNT(*) FROM users;
```

**Find user by username:**
```sql
SELECT * FROM users WHERE username = 'johndoe';
```

**Recent users:**
```sql
SELECT * FROM users 
ORDER BY created_at DESC 
LIMIT 10;
```

**Update user email:**
```sql
UPDATE users 
SET email = 'newemail@example.com', updated_at = CURRENT_TIMESTAMP 
WHERE id = 1;
```

## Performance Considerations

### Connection Pooling

The application uses HikariCP for connection pooling:
- Maximum pool size: 10 connections
- Minimum idle: 5 connections
- Connection timeout: 30 seconds

### Query Optimization

1. **Indexes** are created on frequently queried columns
2. **SERIAL types** auto-generate IDs efficiently
3. **Prepared statements** prevent SQL injection and improve performance

## Future Enhancements

Nothing planned yet.
