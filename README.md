# java-spark-online-store
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spark Framework](https://img.shields.io/badge/Spark-2.5.4-blue.svg)](https://sparkjava.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-12+-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

> A robust REST API built with Java Spark Framework for managing e-commerce orders, featuring multi-environment configuration, connection pooling, and comprehensive error handling.

## Table of Contents

- [Features](#features)
- [Project Structure](#project-structure)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Installation](#Installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Development](#development)
- [License](#license)
- [DOCS](#DOCS)

## Features

### Release 1.0.0

> **! Actual Version**

#### Core Features
- **RESTful API** - Complete CRUD operations for user management
- **Database Integration** - PostgreSQL with automatic schema initialization
- **Connection Pooling** - HikariCP for high-performance database connections
- **JSON Serialization** - Gson with custom LocalDateTime adapters
- **Logging** - Comprehensive logging with Lombok (console)

#### Architecture Features
- **SOLID Principles** - Clean architecture with separation of concerns
- **Layered Architecture** - Controller → Service → DAO → Database
- **Dependency Injection** - Interface-based design for testability
- **DTO Pattern** - Separate request/response objects from entities
- **Mapper Pattern** - Utility classes for entity ↔ DTO conversion

#### Development Features
- **Lombok Integration** - Reduced boilerplate code
-  **Type-Safe Queries** - JDBI SQL Object API
- **Auto-Generated Keys** - Database ID generation
-  **Input Validation** - Request validation with meaningful error messages
-  **Pretty JSON** - Human-readable API responses


## Project Structure
```
java-spark-online-store/
├── docs/
│   ├── API.md                      # API endpoint documentation with examples
│   ├── DATABASE.md                 # Database schema and design
│   ├── ARCHITECTURE.md 
│   └── database/                    # Database backup example
│
├── src/main/
│   ├── java/org/techonready/
│   │   ├── config/                 # Configuration classes
│   │   │   ├── DatabaseConfig.java # HikariCP & JDBI setup
│   │   │   ├── EnvConfig.java      # Environment variables loader
│   │   │   └── GsonConfig.java     # JSON serialization config
│   │   │
│   │   ├── controller/             # HTTP request handlers
│   │   │   └── UserController.java # User endpoint logic
│   │   │
│   │   ├── dao/                    # Data Access Objects
│   │   │   └── UserDao.java        # JDBI interface for DB operations
│   │   │
│   │   ├── dto/                    # Data Transfer Objects
│   │   │   ├── request/            # API request models
│   │   │   │   ├── CreateUserRequest.java
│   │   │   │   └── UpdateUserRequest.java
│   │   │   └── response/           # API response models
│   │   │       ├── UserResponse.java
│   │   │       ├── ApiResponse.java
│   │   │       └── ErrorResponse.java
│   │   │
│   │   ├── entity/                 # Domain models
│   │   │   └── User.java           # User entity (database model)
│   │   │
│   │   ├── exception/              # Custom exceptions (future)
│   │   │
│   │   ├── routes/                 # Route configuration
│   │   │   └── UserRoutes.java     # User endpoint definitions
│   │   │
│   │   ├── service/                # Business logic layer
│   │   │   ├── UserService.java    # Service interface
│   │   │   └── impl/
│   │   │       └── UserServiceImpl.java
│   │   │
│   │   ├── util/                   # Utility classes
│   │   │   ├── UserMapper.java     # DTO ↔ Entity mapping
│   │   │   └── LocalDateTimeAdapter.java # Gson adapter
│   │   │
│   │   ├── web/                    # Web sockets (future)
│   │   │
│   │   └── Application.java        # Main entry point
│   │
│   └── resources/
│       ├── db/
│       │   └── schema.sql          # Database initialization script
│       └── frontend/               # Static resources (future)
│
├── .env.example                    # Environment template
├── .gitignore                      # Git ignore rules
├── pom.xml                         # Maven dependencies
├── LICENSE                         # Project license
└── README.md                       # This file
```

## Technologies

### Core Framework
- **Java 17+** - Programming language with modern features
- **Spark Framework 2.5.4** - Lightweight web framework for REST APIs
- **Maven** - Build and dependency management

### Database
- **PostgreSQL 12+** - Relational database
- **JDBI 3.45.1** - Fluent SQL object API for Java
- **HikariCP 6.0.0** - High-performance JDBC connection pool

### Libraries
- **Lombok 1.18.42** - Reduce boilerplate code (@Data, @Slf4j, @Builder)
- **Gson 2.10.1** - JSON serialization/deserialization
- **dotenv-java 3.2.0** - Environment variable management
- **Logback 1.5.13** - Logging framework
- **SLF4J 1.17.21** - Logging facade

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 17 or higher**
```bash
  java -version
  # Should show: java version "17" or higher
```

- **Apache Maven 3.6+**
```bash
  mvn -version
```

- **PostgreSQL 12+**
```bash
  psql --version
```

- **Git**
```bash
  git --version
```

## Installation

### 1. Clone the Repository
```bash
git clone https://github.com/ckacy01/java-spark-online-store
cd java-spark-online-store
```

### 2. Set Up PostgreSQL Database
```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE onlinestore;

# Exit psql
\q
```

### 3. Configure Environment Variables
```bash
# Copy environment template
cp .env.example .env

# Edit .env with your credentials
nano .env  # or use your preferred editor
```

**Required environment variables:**
```env
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/onlinestore
DB_USERNAME=postgres
DB_PASSWORD=your_password_here
DB_DRIVER=org.postgresql.Driver

# HikariCP Configuration
HIKARI_MAXIMUM_POOL_SIZE=10
HIKARI_MINIMUM_IDLE=5
HIKARI_CONNECTION_TIMEOUT=30000
HIKARI_IDLE_TIMEOUT=600000
HIKARI_MAX_LIFETIME=1800000

# Server Configuration
SERVER_PORT=4567
```

### 4. Build the Project
```bash
# Clean and compile
mvn clean compile

# Run tests (if available)
mvn test

# Package as JAR
mvn clean package
```

## Configuration

### Database Schema Auto-Initialization

The application automatically executes `schema.sql` on startup. To disable:
```java
// Comment out in Application.java
// DatabaseConfig.runSchema(jdbi);
```

### Manual Schema Execution
```bash
psql -U postgres -d onlinestore -f src/main/resources/db/schema.sql
```

## Usage

### Running the Application

```bash
mvn exec:java -Dexec.mainClass="org.technoready.Main"
```


### Verify the Application
```bash
# Health check
curl http://localhost:4567/users

# Should return:
# {
#   "success": true,
#   "message": null,
#   "data": [ ... users ... ]
# }
```

## API Documentation

For detailed API documentation with request/response examples, see **[docs/API.md](docs/API.md)**

### Quick Reference

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/users` | Get all users |
| GET | `/users/:id` | Get user by ID |
| POST | `/users` | Create new user |
| PUT | `/users/:id` | Update user |
| DELETE | `/users/:id` | Delete user |
| OPTIONS | `/users/:id` | Check if user exists |

### Example Request
```bash
# Create a new user
curl -X POST http://localhost:4567/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "email": "john@example.com",
    "fullName": "John Doe"
  }'
```

### Example Response
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com",
    "fullName": "John Doe",
    "createdAt": "2025-10-29T23:33:13",
    "updatedAt": "2025-10-29T23:33:13"
  }
}
```

## Database Schema

For detailed database documentation, see **[docs/DATABASE.md](docs/DATABASE.md)**

### Users Table

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGSERIAL | PRIMARY KEY | Auto-incrementing user ID |
| username | VARCHAR(50) | NOT NULL, UNIQUE | Unique username |
| email | VARCHAR(100) | NOT NULL | User email address |
| full_name | VARCHAR(100) | NOT NULL | User's full name |
| created_at | TIMESTAMP | NOT NULL, DEFAULT NOW() | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT NOW() | Last update timestamp |

### Indexes

- `idx_users_username` - Fast username lookups
- `idx_users_email` - Fast email searches
- `idx_users_created_at` - Chronological queries

## Development

### Code Style

This project follows:
- **Java Code Conventions**
- **Google Java Style Guide** (adapted)
- **SOLID Principles**
- **Clean Code principles**

### Generate JavaDoc 
> Future
```bash
mvn javadoc:javadoc

# Open in browser
open target/site/apidocs/index.html
```

### Logging

Logs are written to:
- **Console**: INFO level and above


### Database Utilities
```bash
# Connect to database
psql -U postgres -d onlinestore

# List all tables
\dt

# Describe users table
\d users

# View all users
SELECT * FROM users;

# Backup database
pg_dump -U postgres onlinestore > backup.sql

# Restore database
psql -U postgres onlinestore < backup.sql
```

### Common Commands
```bash
# Clean build
mvn clean

# Compile only
mvn compile

# Run tests
mvn test

# Package without tests
mvn package -DskipTests

# View dependency tree
mvn dependency:tree

# Update dependencies
mvn versions:display-dependency-updates
```

### Commit Convention

We follow [Conventional Commits](https://www.conventionalcommits.org/):

- `feat:` New feature
- `fix:` Bug fix
- `docs:` Documentation changes
- `style:` Code style changes
- `refactor:` Code refactoring
- `test:` Test additions or changes
- `chore:` Build process or auxiliary tool changes

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Authors

- **Jorge Armando Avila Carrillo** - *NAO: 3310* - [@ckacy01](https://github.com/ckacy01)

## DOCS
- For detailed database documentation, see **[docs/DATABASE.md](docs/DATABASE.md)**
- For detailed api documentation, see **[docs/API.md](docs/API.md)**
- For detailed architecture documentation, see **[docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)**


---


**Made with ☕ and Java**