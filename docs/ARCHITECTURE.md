# Architecture

This project follows **Clean Architecture** principles with clear separation of concerns:
```
┌─────────────────────────────────────────────────────────┐
│                    HTTP Layer (Spark)                   │
│                  routes/ + controller/                  │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│                   Business Logic                        │
│                      service/                           │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│                   Data Access Layer                     │
│                        dao/                             │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│            Database (PostgreSQL + HikariCP)             │
└─────────────────────────────────────────────────────────┘
```

### SOLID Principles Applied

1. **Single Responsibility Principle (SRP)**
    - `UserController`: HTTP layer only
    - `UserService`: Business logic only
    - `UserDao`: Data access only
    - `UserMapper`: Object mapping only

2. **Open/Closed Principle (OCP)**
    - Services use interfaces → open for extension
    - Easy to add implementations without modifying existing code

3. **Liskov Substitution Principle (LSP)**
    - Any `UserService` implementation can replace another
    - Abstractions properly designed for substitutability

4. **Interface Segregation Principle (ISP)**
    - `UserDao` contains only methods clients need
    - DTOs separate API contracts from domain models

5. **Dependency Inversion Principle (DIP)**
    - Controllers depend on service interfaces
    - Services depend on DAO abstractions
    - Easy to mock for testing