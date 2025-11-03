-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- Items table with availability status
CREATE TABLE IF NOT EXISTS items (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    current_price DECIMAL(10,2) NOT NULL, -- Current bidding price
    original_price DECIMAL(10,2) NOT NULL, -- Original listing price
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- Offers table (bids)
CREATE TABLE IF NOT EXISTS offers (
    id BIGSERIAL PRIMARY KEY,
    item_id BIGINT NOT NULL REFERENCES items(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    offer_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- PENDING, ACCEPTED, REJECTED, OUTBID
    message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_offer_amount_positive CHECK (offer_amount > 0),
    CONSTRAINT chk_status_valid CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED', 'OUTBID'))
    );

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_created_at ON users(created_at);
CREATE INDEX IF NOT EXISTS idx_items_name ON items(name);
CREATE INDEX IF NOT EXISTS idx_items_price ON items(price);
CREATE INDEX IF NOT EXISTS idx_items_current_price ON items(current_price);
CREATE INDEX IF NOT EXISTS idx_items_is_available ON items(is_available);
CREATE INDEX IF NOT EXISTS idx_items_created_at ON items(created_at);
CREATE INDEX IF NOT EXISTS idx_offers_item_id ON offers(item_id);
CREATE INDEX IF NOT EXISTS idx_offers_user_id ON offers(user_id);
CREATE INDEX IF NOT EXISTS idx_offers_status ON offers(status);
CREATE INDEX IF NOT EXISTS idx_offers_created_at ON offers(created_at);

-- Composite indexes for common queries
CREATE INDEX IF NOT EXISTS idx_offers_item_status ON offers(item_id, status);
CREATE INDEX IF NOT EXISTS idx_offers_user_status ON offers(user_id, status);

-- Insert sample data
DO $$
BEGIN
    -- Insert users
    IF NOT EXISTS (SELECT 1 FROM users) THEN
        INSERT INTO users (username, email, full_name, created_at, updated_at) VALUES
            ('rafael', 'rafael@example.com', 'Rafael García', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
            ('sofia', 'sofia@example.com', 'Sofía Martínez', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
            ('ramon', 'ramon@example.com', 'Ramón Collector', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
            ('arturo', 'arturo@example.com', 'Arturo Bandini', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
            ('maria', 'maria@example.com', 'María López', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
END IF;

    -- Insert items
    IF NOT EXISTS (SELECT 1 FROM items) THEN
        INSERT INTO items (name, description, price, current_price, original_price, is_available, created_at, updated_at) VALUES
            ('Gorra autografiada por Peso Pluma', 'Una gorra autografiada por el famoso Peso Pluma.', 621.34, 621.34, 621.34, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
            ('Casco autografiado por Rosalía', 'Un casco autografiado por la famosa cantante Rosalía, una verdadera MOTOMAMI!', 734.57, 734.57, 734.57, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
            ('Chamarra de Bad Bunny', 'Una chamarra de la marca favorita de Bad Bunny, autografiada por el propio artista.', 521.89, 521.89, 521.89, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
            ('Guitarra de Fernando Delgadillo', 'Una guitarra acústica de alta calidad utilizada por el famoso cantautor Fernando Delgadillo.', 823.12, 823.12, 823.12, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
            ('Jersey firmado por Snoop Dogg', 'Un jersey autografiado por el legendario rapero Snoop Dogg.', 355.67, 355.67, 355.67, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
            ('Prenda de Cardi B autografiada', 'Un crop-top usado y autografiado por la famosa rapera Cardi B. en su última visita a México', 674.23, 674.23, 674.23, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
            ('Guitarra autografiada por Coldplay', 'Una guitarra eléctrica autografiada por la popular banda británica Coldplay, un día antes de su concierto en Monterrey en 2022.', 458.91, 458.91, 458.91, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
END IF;

    -- Insert sample offers
    IF NOT EXISTS (SELECT 1 FROM offers) THEN
        INSERT INTO offers (item_id, user_id, offer_amount, status, message, created_at) VALUES
            (1, 1, 650.00, 'PENDING', 'Me encanta Peso Pluma, ofrezco más del precio inicial!', CURRENT_TIMESTAMP),
            (1, 2, 700.00, 'PENDING', 'Ofrezco más para llevarme esta gorra única!', CURRENT_TIMESTAMP),
            (2, 3, 750.00, 'PENDING', 'Fan de Rosalía, quiero este casco!', CURRENT_TIMESTAMP),
            (3, 1, 550.00, 'PENDING', 'Gran precio por esta chamarra de Bad Bunny', CURRENT_TIMESTAMP),
            (4, 4, 900.00, 'PENDING', 'Soy coleccionista de instrumentos musicales', CURRENT_TIMESTAMP);
END IF;
END $$;

-- Comments
COMMENT ON TABLE offers IS 'Stores user offers/bids for collectible items';
COMMENT ON COLUMN offers.status IS 'Offer status: PENDING, ACCEPTED, REJECTED, OUTBID';
COMMENT ON COLUMN items.current_price IS 'Current highest offer price or original price';
COMMENT ON COLUMN items.original_price IS 'Initial listing price';
COMMENT ON COLUMN items.is_available IS 'Whether item is still available for offers';