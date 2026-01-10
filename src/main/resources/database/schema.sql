-- ============================================
-- MiniShop Database Schema
-- MySQL 8.0+
-- ============================================

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS minishop
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE minishop;

-- ============================================
-- Drop tables if they exist (for clean setup)
-- ============================================
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS cart_items;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;

-- ============================================
-- Table: users
-- Stores user account information
-- ============================================
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    is_active BOOLEAN DEFAULT TRUE,

    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Table: products
-- Stores product catalog
-- ============================================
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price_cents BIGINT NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    image_url VARCHAR(500),
    category VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_name (name),
    INDEX idx_category (category),
    INDEX idx_price (price_cents),
    INDEX idx_is_active (is_active),
    INDEX idx_created_at (created_at),

    CONSTRAINT chk_price_positive CHECK (price_cents >= 0),
    CONSTRAINT chk_stock_non_negative CHECK (stock >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Table: cart_items
-- Stores shopping cart items for users
-- ============================================
CREATE TABLE cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,

    UNIQUE KEY unique_user_product (user_id, product_id),
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id),

    CONSTRAINT chk_quantity_positive CHECK (quantity > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Table: orders
-- Stores customer orders
-- ============================================
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    total_amount_cents BIGINT NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    shipping_address TEXT NOT NULL,
    payment_method VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

    INDEX idx_user_id (user_id),
    INDEX idx_order_number (order_number),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),

    CONSTRAINT chk_total_amount_positive CHECK (total_amount_cents >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Table: order_items
-- Stores individual items in orders
-- ============================================
CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    price_cents BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT,

    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id),

    CONSTRAINT chk_quantity_positive_order CHECK (quantity > 0),
    CONSTRAINT chk_price_positive_order CHECK (price_cents >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Sample Data: Users
-- Password for all users: "password123"
-- (In production, use bcrypt or similar hashing)
-- ============================================
INSERT INTO users (email, password_hash, full_name, role) VALUES
('admin@minishop.com', 'e10adc3949ba59abbe56e057f20f883e', 'Admin User', 'ADMIN'),
('john.doe@example.com', 'e10adc3949ba59abbe56e057f20f883e', 'John Doe', 'USER'),
('jane.smith@example.com', 'e10adc3949ba59abbe56e057f20f883e', 'Jane Smith', 'USER');

-- ============================================
-- Sample Data: Products
-- Prices are in cents (e.g., 9999 = 99.99€)
-- ============================================
INSERT INTO products (name, description, price_cents, stock, category, image_url) VALUES
-- Electronics
('Laptop Dell XPS 13', 'Ordinateur portable ultraléger avec écran tactile 13 pouces, processeur Intel Core i7, 16GB RAM, SSD 512GB', 129900, 15, 'Électronique', 'https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=500'),
('iPhone 15 Pro', 'Smartphone Apple avec puce A17 Pro, appareil photo 48MP, écran Super Retina XDR 6.1 pouces', 119900, 25, 'Électronique', 'https://images.unsplash.com/photo-1592286927505-a5e4d32e5a2c?w=500'),
('Samsung Galaxy S24', 'Smartphone Android haut de gamme, écran AMOLED 6.2 pouces, 256GB stockage', 89900, 20, 'Électronique', 'https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=500'),
('AirPods Pro 2', 'Écouteurs sans fil avec réduction de bruit active, son spatial, résistants à l\'eau', 24900, 50, 'Audio', 'https://images.unsplash.com/photo-1606841837239-c5a1a4a07af7?w=500'),
('Sony WH-1000XM5', 'Casque audio premium avec suppression de bruit, autonomie 30h, qualité audio exceptionnelle', 39900, 30, 'Audio', 'https://images.unsplash.com/photo-1546435770-a3e426bf472b?w=500'),

-- Computers & Accessories
('iPad Pro 12.9', 'Tablette Apple avec puce M2, écran Liquid Retina XDR, compatible Apple Pencil', 139900, 12, 'Tablettes', 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=500'),
('MacBook Air M2', 'Ordinateur portable Apple ultra-fin, puce M2, écran Retina 13.6 pouces, 256GB SSD', 119900, 10, 'Ordinateurs', 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=500'),
('Logitech MX Master 3S', 'Souris sans fil ergonomique pour professionnels, 8000 DPI, batterie longue durée', 9900, 45, 'Accessoires', 'https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=500'),
('Clavier Mécanique RGB', 'Clavier gaming mécanique avec rétroéclairage RGB, switches Cherry MX, repose-poignet', 14900, 35, 'Accessoires', 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=500'),

-- Wearables
('Apple Watch Series 9', 'Montre connectée avec GPS, surveillance santé, écran Always-On Retina', 44900, 40, 'Montres', 'https://images.unsplash.com/photo-1434494878577-86c23bcb06b9?w=500'),
('Fitbit Charge 6', 'Bracelet de fitness avec suivi de fréquence cardiaque, GPS intégré, étanche', 14900, 55, 'Fitness', 'https://images.unsplash.com/photo-1575311373937-040b8e1fd5b6?w=500'),

-- Gaming
('PlayStation 5', 'Console de jeu nouvelle génération avec SSD ultra-rapide, ray tracing, 4K/120Hz', 54900, 8, 'Gaming', 'https://images.unsplash.com/photo-1606144042614-b2417e99c4e3?w=500'),
('Xbox Series X', 'Console de jeu Microsoft 4K, 1TB SSD, compatibilité rétrocompatible', 49900, 10, 'Gaming', 'https://images.unsplash.com/photo-1621259182978-fbf93132d53d?w=500'),
('Nintendo Switch OLED', 'Console de jeu hybride avec écran OLED 7 pouces, mode portable et TV', 34900, 22, 'Gaming', 'https://images.unsplash.com/photo-1578303512597-81e6cc155b3e?w=500'),

-- Home & Living
('Smart TV Samsung 55"', 'Téléviseur QLED 4K avec intelligence artificielle, HDR10+, smart hub', 79900, 6, 'TV & Vidéo', 'https://images.unsplash.com/photo-1593359677879-a4bb92f829d1?w=500'),
('Amazon Echo Dot 5', 'Enceinte intelligente avec Alexa, son amélioré, contrôle maison connectée', 5900, 100, 'Maison connectée', 'https://images.unsplash.com/photo-1543512214-318c7553f230?w=500'),

-- Cameras
('Canon EOS R6', 'Appareil photo hybride 20MP, stabilisation 5 axes, vidéo 4K 60fps', 249900, 5, 'Photo', 'https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=500'),
('GoPro Hero 12', 'Caméra d\'action 5.3K, stabilisation HyperSmooth, étanche 10m', 39900, 18, 'Caméras', 'https://images.unsplash.com/photo-1585909695284-32d2985ac9c0?w=500'),

-- Accessories
('Anker PowerBank 20000mAh', 'Batterie externe haute capacité avec charge rapide USB-C PD', 4900, 75, 'Accessoires', 'https://images.unsplash.com/photo-1609091839311-d5365f9ff1c5?w=500'),
('SanDisk SSD Externe 1TB', 'Disque dur externe SSD portable, vitesse lecture 1050 MB/s, USB-C', 11900, 28, 'Stockage', 'https://images.unsplash.com/photo-1597872200969-2b65d56bd16b?w=500');

-- ============================================
-- Indexes optimization for queries
-- ============================================

-- Composite index for product filtering
CREATE INDEX idx_products_active_category ON products(is_active, category);

-- Composite index for user orders
CREATE INDEX idx_orders_user_status ON orders(user_id, status);

-- Full-text search index for product search
CREATE FULLTEXT INDEX idx_products_search ON products(name, description);

-- ============================================
-- Views (optional, for complex queries)
-- ============================================

-- View: Active products with formatted price
CREATE OR REPLACE VIEW v_active_products AS
SELECT
    id,
    name,
    description,
    price_cents,
    CONCAT(FORMAT(price_cents / 100, 2), ' €') AS formatted_price,
    stock,
    category,
    image_url,
    created_at
FROM products
WHERE is_active = TRUE;

-- View: User order summary
CREATE OR REPLACE VIEW v_user_order_summary AS
SELECT
    u.id AS user_id,
    u.full_name,
    COUNT(DISTINCT o.id) AS total_orders,
    SUM(o.total_amount_cents) AS total_spent_cents,
    MAX(o.created_at) AS last_order_date
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
GROUP BY u.id, u.full_name;

-- ============================================
-- Database initialization complete
-- ============================================
SELECT 'Database schema created successfully!' AS status;
