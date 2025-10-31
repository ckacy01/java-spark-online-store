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

-- Insert sample data for testing
INSERT INTO users (username, email, full_name, created_at, updated_at) VALUES ('rafael', 'rafael@example.com', 'Rafael García', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                              ('sofia', 'sofia@example.com', 'Sofía Martínez', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                              ('ramon', 'ramon@example.com', 'Ramón Collector', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (username) DO NOTHING;

INSERT INTO items (id, name, description, price) VALUES
                                                     (1, 'Gorra autografiada por Peso Pluma', 'Una gorra autografiada por el famoso Peso Pluma.', 621.34),
                                                     (2, 'Casco autografiado por Rosalía', 'Un casco autografiado por la famosa cantante Rosalía, una verdadera MOTOMAMI!', 734.57),
                                                     (3, 'Chamarra de Bad Bunny', 'Una chamarra de la marca favorita de Bad Bunny, autografiada por el propio artista.', 521.89),
                                                     (4, 'Guitarra de Fernando Delgadillo', 'Una guitarra acústica de alta calidad utilizada por el famoso cantautor Fernando Delgadillo.', 823.12),
                                                     (5, 'Jersey firmado por Snoop Dogg', 'Un jersey autografiado por el legendario rapero Snoop Dogg.', 355.67),
                                                     (6, 'Prenda de Cardi B autografiada', 'Un crop-top usado y autografiado por la famosa rapera Cardi B. en su última visita a México', 674.23),
                                                     (7, 'Guitarra autografiada por Coldplay', 'Una guitarra eléctrica autografiada por la popular banda británica Coldplay, un día antes de su concierto en Monterrey en 2022.', 458.91)  ON CONFLICT (id) DO NOTHING;
