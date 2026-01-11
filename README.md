# MiniShop - Application JEE E-Commerce

Application web de mini-boutique développée avec Jakarta EE, Tomcat 11 et MySQL.

## 🚀 Technologies

- **Backend**: Jakarta EE (Servlets, JSP, JSTL)
- **Serveur**: Apache Tomcat 11
- **Base de données**: MySQL 8.0+
- **Build**: Maven
- **Frontend**: Bootstrap 5, HTML5, CSS3
- **Java**: Version 17

## 📋 Prérequis

- JDK 17 ou supérieur
- Apache Tomcat 11
- MySQL 8.0 ou supérieur
- Maven 3.8+

## 🛠️ Installation

### 1. Cloner le projet

```bash
git clone <repository-url>
cd PROJET-JEE-MiniShop
```

### 2. Configurer la base de données

1. Créer la base de données MySQL:

```bash
mysql -u root -p
```

2. Exécuter le script SQL:

```bash
mysql -u root -p < src/main/resources/database/schema.sql
```

3. Vérifier/ajuster les paramètres de connexion dans `src/main/java/com/minishop/config/AppConstants.java`:

```java
public static final String DB_URL = "jdbc:mysql://localhost:3306/minishop?useSSL=false&serverTimezone=Europe/Paris&allowPublicKeyRetrieval=true";
public static final String DB_USERNAME = "root";
public static final String DB_PASSWORD = ""; // Votre mot de passe MySQL
```

**Note** : Par défaut, le mot de passe est vide pour XAMPP/WAMP. Ajustez selon votre configuration.

### 3. Compiler le projet

```bash
mvn clean package
```

### 4. Déployer sur Tomcat

1. Copier le fichier WAR généré vers le dossier webapps de Tomcat:

```bash
cp target/minishop.war $TOMCAT_HOME/webapps/
```

2. Démarrer Tomcat:

```bash
$TOMCAT_HOME/bin/startup.sh  # Linux/Mac
$TOMCAT_HOME/bin/startup.bat # Windows
```

3. Accéder à l'application:

```
http://localhost:8080/minishop/home
```

## 📁 Structure du projet

```
PROJET-JEE-MiniShop/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── minishop/
│   │   │           ├── config/          # Configuration (DB, constantes)
│   │   │           ├── dao/             # Data Access Objects
│   │   │           ├── dao/impl/        # Implémentations DAO
│   │   │           ├── model/           # Entités/Modèles
│   │   │           ├── service/         # Logique métier
│   │   │           ├── util/            # Utilitaires
│   │   │           └── web/
│   │   │               ├── servlet/     # Servlets
│   │   │               │   ├── auth/    # Authentification
│   │   │               │   ├── product/ # Produits
│   │   │               │   └── cart/    # Panier
│   │   │               └── filter/      # Filtres
│   │   ├── resources/
│   │   │   └── database/
│   │   │       └── schema.sql           # Script de création BD
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── web.xml              # Descripteur de déploiement
│   │       │   └── jsp/
│   │       │       ├── common/          # Fragments JSP (header, footer)
│   │       │       ├── public/          # Pages publiques
│   │       │       ├── auth/            # Pages d'authentification
│   │       │       ├── product/         # Pages produits
│   │       │       ├── cart/            # Pages panier
│   │       │       ├── user/            # Pages utilisateur
│   │       │       └── error/           # Pages d'erreur
│   │       └── assets/
│   │           └── css/
│   │               └── style.css        # Styles personnalisés
│   └── test/                            # Tests unitaires
├── pom.xml                              # Configuration Maven
└── README.md
```

## 🔐 Comptes de test

Tous les comptes utilisent le mot de passe : **`password123`** (hashé en SHA-256)

### Administrateur
- Email: `admin@minishop.com`
- Rôle: ADMIN

### Utilisateurs
- Email: `user@minishop.com`
- Email: `john.doe@example.com`
- Email: `marie.dupont@example.com`
- Rôle: USER

## 🎯 Fonctionnalités complètes (Tous les paliers)

### ✅ Palier 0 - Démarrage
- ✅ Page d'accueil avec design moderne
- ✅ Navigation responsive avec Bootstrap 5
- ✅ Connexion à la base de données MySQL
- ✅ Structure Maven complète
- ✅ Configuration Jakarta EE 6.0

### ✅ Palier 1 - Authentification
- ✅ Système de connexion (LoginServlet)
- ✅ Système de déconnexion (LogoutServlet)
- ✅ Filtre d'authentification (AuthFilter) protégeant /app/*
- ✅ Gestion des sessions utilisateur
- ✅ Hashage des mots de passe avec SHA-256

### ✅ Palier 2 - Catalogue produits
- ✅ Liste complète des produits (ProductListServlet)
- ✅ Page détail produit (ProductDetailServlet)
- ✅ Affichage du stock et des prix
- ✅ DAO JDBC pour les produits

### ✅ Palier 3 - Panier côté serveur
- ✅ Gestion du panier en session (CartService)
- ✅ Ajout de produits au panier (CartAddServlet)
- ✅ Suppression de produits (CartRemoveServlet)
- ✅ Visualisation du panier (CartViewServlet)
- ✅ Calcul automatique du total

### ✅ Palier 4 - LocalStorage et fusion panier
- ✅ Panier invité dans LocalStorage (cart-local.js)
- ✅ Gestion automatique du badge panier
- ✅ Fusion automatique après connexion (cart-merge.js)
- ✅ Servlet de fusion (CartMergeServlet)
- ✅ Gestion des thèmes avec LocalStorage (theme.js)

## 📊 Base de données

Le projet utilise **STRICTEMENT** 2 tables comme spécifié dans l'énoncé :

### Table `users`
- id (BIGINT, PK, AUTO_INCREMENT)
- email (VARCHAR(190), UNIQUE)
- password_hash (VARCHAR(255)) - SHA-256
- full_name (VARCHAR(120))
- role (ENUM: 'USER', 'ADMIN')
- created_at (TIMESTAMP)

### Table `products`
- id (BIGINT, PK, AUTO_INCREMENT)
- name (VARCHAR(140))
- description (TEXT)
- price_cents (INT) - Prix en centimes
- stock (INT)
- created_at (TIMESTAMP)

**⚠️ IMPORTANT** : Le panier n'est **PAS** stocké en base de données.
- Pour les utilisateurs connectés : panier en **SESSION**
- Pour les invités : panier en **LocalStorage** (navigateur)

## 🤝 Contribution

1. Fork le projet
2. Créer une branche pour votre fonctionnalité (`git checkout -b feature/AmazingFeature`)
3. Commit vos changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

## 📄 Licence

Ce projet est développé à des fins éducatives.

## 👥 Auteurs

- Projet JEE - MiniShop

## 📞 Contact

Pour toute question ou suggestion, n'hésitez pas à ouvrir une issue.
