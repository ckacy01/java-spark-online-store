CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS items (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL
    );

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_created_at ON users(created_at);
CREATE INDEX IF NOT EXISTS idx_items_name ON items(name);
CREATE INDEX IF NOT EXISTS idx_items_description ON items(description);
CREATE INDEX IF NOT EXISTS idx_items_price ON items(price);



-- Insert sample data for testing
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM users) THEN
        INSERT INTO users (username, email, full_name, created_at, updated_at) VALUES
            ('rafael', 'rafael@example.com', 'Rafael García', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
            ('sofia', 'sofia@example.com', 'Sofía Martínez', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
            ('ramon', 'ramon@example.com', 'Ramón Collector', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
END IF;

    IF NOT EXISTS (SELECT 1 FROM items) THEN
        INSERT INTO items (name, description, price) VALUES
            ('Gorra autografiada por Peso Pluma', 'Una gorra autografiada por el famoso Peso Pluma.', 621.34),
            ('Casco autografiado por Rosalía', 'Un casco autografiado por la famosa cantante Rosalía, una verdadera MOTOMAMI!', 734.57),
            ('Chamarra de Bad Bunny', 'Una chamarra de la marca favorita de Bad Bunny, autografiada por el propio artista.', 521.89),
            ('Guitarra de Fernando Delgadillo', 'Una guitarra acústica de alta calidad utilizada por el famoso cantautor Fernando Delgadillo.', 823.12),
            ('Jersey firmado por Snoop Dogg', 'Un jersey autografiado por el legendario rapero Snoop Dogg.', 355.67),
            ('Prenda de Cardi B autografiada', 'Un crop-top usado y autografiado por la famosa rapera Cardi B. en su última visita a México', 674.23),
            ('Guitarra autografiada por Coldplay', 'Una guitarra eléctrica autografiada por la popular banda británica Coldplay, un día antes de su concierto en Monterrey en 2022.', 458.91);
END IF;
END $$;