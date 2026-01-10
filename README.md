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

3. Configurer les paramètres de connexion dans `src/main/java/com/minishop/config/AppConstants.java`:

```java
public static final String DB_URL = "jdbc:mysql://localhost:3306/minishop";
public static final String DB_USERNAME = "root";
public static final String DB_PASSWORD = "votre_mot_de_passe";
```

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

### Administrateur
- Email: `admin@minishop.com`
- Mot de passe: `password123`

### Utilisateur
- Email: `john.doe@example.com`
- Mot de passe: `password123`

## 🎯 Fonctionnalités (Palier 0)

- ✅ Page d'accueil avec design moderne
- ✅ Navigation responsive avec Bootstrap 5
- ✅ Connexion à la base de données MySQL
- ✅ Structure Maven complète
- ✅ Configuration Jakarta EE 6.0

## 📝 Prochaines étapes (Paliers suivants)

- [ ] Authentification utilisateur (login/logout)
- [ ] Inscription des utilisateurs
- [ ] Liste et détail des produits
- [ ] Gestion du panier
- [ ] Processus de commande
- [ ] Gestion des commandes utilisateur
- [ ] Panel d'administration
- [ ] Recherche et filtrage de produits

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
