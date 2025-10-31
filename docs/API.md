<!-- docs/API.md -->
# API Documentation

## Base URL
```
http://localhost:4567
```

## Authentication

Currently, the API does not require authentication. This will be added in future releases.

## Response Format

All endpoints return responses in the following format:

### Success Response
```json
{
  "success": true,
  "message": "Optional success message",
  "data": { /* Response data */ }
}
```

### Error Response
```json
{
  "success": false,
  "message": "Error description",
  "data": null
}
```

## Endpoints

### Users

#### Get All Users

**Endpoint:** `GET /users`

**Description:** Retrieve a list of all users.

**Response:**
```json
{
  "success": true,
  "message": null,
  "data": [
    {
      "id": 1,
      "username": "johndoe",
      "email": "john@example.com",
      "fullName": "John Doe",
      "createdAt": "2025-10-29T10:30:00",
      "updatedAt": "2025-10-29T10:30:00"
    }
  ]
}
```

**Example:**
```bash
curl -X GET http://localhost:4567/users
```

---

#### Get User by ID

**Endpoint:** `GET /users/:id`

**Description:** Retrieve a specific user by their ID.

**Parameters:**
- `id` (path parameter) - User ID

**Success Response (200):**
```json
{
  "success": true,
  "message": null,
  "data": {
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com",
    "fullName": "John Doe",
    "createdAt": "2025-10-29T10:30:00",
    "updatedAt": "2025-10-29T10:30:00"
  }
}
```

**Error Response (404):**
```json
{
  "success": false,
  "message": "User not found with id: 999",
  "data": null
}
```

**Example:**
```bash
curl -X GET http://localhost:4567/users/1
```

---

#### Create User

**Endpoint:** `POST /users`

**Description:** Create a new user.

**Request Body:**
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "fullName": "John Doe"
}
```

**Validation Rules:**
- `username`: Required, 3-50 characters, unique
- `email`: Required, valid email format
- `fullName`: Required, max 100 characters

**Success Response (201):**
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "id": 5,
    "username": "johndoe",
    "email": "john@example.com",
    "fullName": "John Doe",
    "createdAt": "2025-10-29T15:45:00",
    "updatedAt": "2025-10-29T15:45:00"
  }
}
```

**Error Response (400):**
```json
{
  "success": false,
  "message": "Username is required",
  "data": null
}
```

**Example:**
```bash
curl -X POST http://localhost:4567/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "email": "john@example.com",
    "fullName": "John Doe"
  }'
```

---

#### Update User

**Endpoint:** `PUT /users/:id`

**Description:** Update an existing user.

**Parameters:**
- `id` (path parameter) - User ID

**Request Body:**
```json
{
  "email": "newemail@example.com",
  "fullName": "Updated Name"
}
```

**Note:** Username cannot be updated. Only email and fullName are updatable.

**Success Response (200):**
```json
{
  "success": true,
  "message": "User updated successfully",
  "data": {
    "id": 1,
    "username": "johndoe",
    "email": "newemail@example.com",
    "fullName": "Updated Name",
    "createdAt": "2025-10-29T10:30:00",
    "updatedAt": "2025-10-29T16:00:00"
  }
}
```

**Example:**
```bash
curl -X PUT http://localhost:4567/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newemail@example.com",
    "fullName": "Updated Name"
  }'
```

---

#### Delete User

**Endpoint:** `DELETE /users/:id`

**Description:** Delete a user by ID.

**Parameters:**
- `id` (path parameter) - User ID

**Success Response (200):**
```json
{
  "success": true,
  "message": "User deleted successfully",
  "data": null
}
```

**Error Response (404):**
```json
{
  "success": false,
  "message": "User not found with id: 999",
  "data": null
}
```

**Example:**
```bash
curl -X DELETE http://localhost:4567/users/1
```

---

#### Check User Exists

**Endpoint:** `OPTIONS /users/:id`

**Description:** Check if a user exists without retrieving full data.

**Parameters:**
- `id` (path parameter) - User ID

**Success Response (200):**
```json
{
  "success": true,
  "message": "User exists",
  "data": true
}
```

**Not Found Response (404):**
```json
{
  "success": true,
  "message": "User does not exist",
  "data": false
}
```

**Example:**
```bash
curl -X OPTIONS http://localhost:4567/users/1
```

---

## Error Codes

| Status Code | Meaning |
|-------------|---------|
| 200 | OK - Request succeeded |
| 201 | Created - Resource created successfully |
| 400 | Bad Request - Invalid input or validation error |
| 404 | Not Found - Resource not found |
| 500 | Internal Server Error - Server encountered an error |

## Rate Limiting

Currently, there is no rate limiting. This will be added in future releases.

## Pagination

Pagination is not yet implemented. All GET requests return full result sets. This will be added in future releases.