CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_created_at ON users(created_at);

-- Insert sample data for testing
INSERT INTO users (username, email, full_name, created_at, updated_at) VALUES ('rafael', 'rafael@example.com', 'Rafael García', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                              ('sofia', 'sofia@example.com', 'Sofía Martínez', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                              ('ramon', 'ramon@example.com', 'Ramón Collector', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (username) DO NOTHING;