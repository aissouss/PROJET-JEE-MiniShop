-- ============================================
-- MiniShop Database Schema
-- MySQL 8.0+
-- STRICTEMENT conforme à l'énoncé du projet
-- 2 TABLES UNIQUEMENT : users, products
-- Panier géré en SESSION (pas en base)
-- ============================================

-- Drop database if exists (clean setup)
DROP DATABASE IF EXISTS minishop;

-- Create database
CREATE DATABASE minishop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE minishop;

-- ============================================
-- Table: users
-- Champs : id, email, password_hash, full_name, role, created_at
-- ============================================
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(190) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  full_name VARCHAR(120) NOT NULL,
  role ENUM('USER','ADMIN') NOT NULL DEFAULT 'USER',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Table: products
-- Champs : id, name, description, price_cents, stock, created_at
-- ============================================
CREATE TABLE products (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(140) NOT NULL,
  description TEXT NULL,
  price_cents INT NOT NULL,
  stock INT NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT chk_products_price CHECK (price_cents >= 0),
  CONSTRAINT chk_products_stock CHECK (stock >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Index pour recherche produits
CREATE INDEX idx_products_name ON products(name);

-- ============================================
-- Données de test - Utilisateurs
-- Tous les mots de passe : "password123"
-- Hash SHA-256 de "password123" = ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f
-- ============================================
INSERT INTO users (email, password_hash, full_name, role) VALUES
('admin@minishop.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Admin MiniShop', 'ADMIN'),
('user@minishop.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Utilisateur Test', 'USER'),
('john.doe@example.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'John Doe', 'USER'),
('marie.dupont@example.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Marie Dupont', 'USER');

-- ============================================
-- Données de test - Produits
-- Prices en centimes (ex: 9999 = 99.99€)
-- ============================================
INSERT INTO products (name, description, price_cents, stock) VALUES
-- Électronique
('Laptop Dell XPS 13', 'Ordinateur portable ultraléger avec écran tactile 13 pouces, processeur Intel Core i7, 16GB RAM, SSD 512GB', 129900, 15),
('iPhone 15 Pro', 'Smartphone Apple avec puce A17 Pro, appareil photo 48MP, écran Super Retina XDR 6.1 pouces', 119900, 25),
('Samsung Galaxy S24', 'Smartphone Android haut de gamme, écran AMOLED 6.2 pouces, 256GB stockage', 89900, 20),
('iPad Pro 12.9', 'Tablette Apple avec puce M2, écran Liquid Retina XDR, compatible Apple Pencil', 139900, 12),
('MacBook Air M2', 'Ordinateur portable Apple ultra-fin, puce M2, écran Retina 13.6 pouces, 256GB SSD', 119900, 10),

-- Audio
('AirPods Pro 2', 'Écouteurs sans fil avec réduction de bruit active, son spatial, résistants à l eau', 24900, 50),
('Sony WH-1000XM5', 'Casque audio premium avec suppression de bruit, autonomie 30h, qualité audio exceptionnelle', 39900, 30),

-- Accessoires
('Logitech MX Master 3S', 'Souris sans fil ergonomique pour professionnels, 8000 DPI, batterie longue durée', 9900, 45),
('Clavier Mécanique RGB', 'Clavier gaming mécanique avec rétroéclairage RGB, switches Cherry MX, repose-poignet', 14900, 35),
('Anker PowerBank 20000mAh', 'Batterie externe haute capacité avec charge rapide USB-C PD', 4900, 75),
('SanDisk SSD Externe 1TB', 'Disque dur externe SSD portable, vitesse lecture 1050 MB/s, USB-C', 11900, 28),

-- Montres et Fitness
('Apple Watch Series 9', 'Montre connectée avec GPS, surveillance santé, écran Always-On Retina', 44900, 40),
('Fitbit Charge 6', 'Bracelet de fitness avec suivi de fréquence cardiaque, GPS intégré, étanche', 14900, 55),

-- Gaming
('PlayStation 5', 'Console de jeu nouvelle génération avec SSD ultra-rapide, ray tracing, 4K/120Hz', 54900, 8),
('Xbox Series X', 'Console de jeu Microsoft 4K, 1TB SSD, compatibilité rétrocompatible', 49900, 10),
('Nintendo Switch OLED', 'Console de jeu hybride avec écran OLED 7 pouces, mode portable et TV', 34900, 22),

-- TV et Maison
('Smart TV Samsung 55"', 'Téléviseur QLED 4K avec intelligence artificielle, HDR10+, smart hub', 79900, 6),
('Amazon Echo Dot 5', 'Enceinte intelligente avec Alexa, son amélioré, contrôle maison connectée', 5900, 100),

-- Photo et Vidéo
('Canon EOS R6', 'Appareil photo hybride 20MP, stabilisation 5 axes, vidéo 4K 60fps', 249900, 5),
('GoPro Hero 12', 'Caméra d action 5.3K, stabilisation HyperSmooth, étanche 10m', 39900, 18);

-- ============================================
-- Vérification
-- ============================================
SELECT 'Base de données MiniShop créée avec succès!' AS status;
SELECT COUNT(*) AS nb_users FROM users;
SELECT COUNT(*) AS nb_products FROM products;
