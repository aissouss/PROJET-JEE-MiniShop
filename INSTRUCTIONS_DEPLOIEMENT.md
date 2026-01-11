# 🚀 INSTRUCTIONS DE DÉPLOIEMENT - MiniShop

## ✅ CORRECTIONS EFFECTUÉES

Le projet a été corrigé pour respecter **STRICTEMENT** l'énoncé :

### 1. Base de données (schema.sql) ✅
- ❌ **SUPPRIMÉ** : Tables `cart_items`, `orders`, `order_items` (NON DEMANDÉES)
- ❌ **SUPPRIMÉ** : Colonnes `image_url`, `category`, `is_active`, `updated_at`, `last_login`
- ✅ **CORRIGÉ** : Utilisation de SHA-256 au lieu de MD5 pour les mots de passe
- ✅ **CORRIGÉ** : Colonnes strictement conformes à l'énoncé (users + products uniquement)
- ✅ **AJOUTÉ** : Contraintes CHECK sur price_cents et stock

### 2. Configuration Java ✅
- ✅ **DbConfig.java** : Ajout du chargement explicite du driver MySQL
- ✅ **AppConstants.java** : URL MySQL avec paramètres optimaux (useSSL=false, timezone, etc.)
- ✅ **Namespace** : Utilisation exclusive de `jakarta.servlet.*` (Tomcat 11 compatible)

### 3. Architecture ✅
- ✅ Tous les packages conformes à l'énoncé
- ✅ Toutes les classes demandées présentes et fonctionnelles
- ✅ Aucune dépendance non autorisée (pas de Spring, JPA, Hibernate)

### 4. Fonctionnalités ✅
- ✅ Palier 0 : Page d'accueil + navigation
- ✅ Palier 1 : Login/Logout + AuthFilter
- ✅ Palier 2 : Catalogue produits + détail
- ✅ Palier 3 : Panier session
- ✅ Palier 4 : LocalStorage + fusion panier

---

## 📋 PRÉREQUIS

### Logiciels nécessaires

1. **JDK 17** (minimum)
   - Télécharger : https://www.oracle.com/java/technologies/downloads/#java17
   - Vérifier : `java -version`

2. **Apache Tomcat 11**
   - Télécharger : https://tomcat.apache.org/download-11.cgi
   - Extraire dans un dossier (ex: `C:\tomcat11` ou `/opt/tomcat11`)

3. **MySQL 8.0+**
   - Windows : XAMPP (https://www.apachefriends.org/)
   - Mac : MAMP ou Homebrew (`brew install mysql`)
   - Linux : `sudo apt install mysql-server`

4. **Maven 3.8+**
   - Télécharger : https://maven.apache.org/download.cgi
   - Ajouter au PATH
   - Vérifier : `mvn -version`

5. **IntelliJ IDEA** (recommandé) ou Eclipse

---

## 🗄️ ÉTAPE 1 : CONFIGURER LA BASE DE DONNÉES

### Option A : Ligne de commande MySQL

```bash
# 1. Se connecter à MySQL
mysql -u root -p

# 2. Exécuter le script (dans MySQL)
SOURCE C:/chemin/vers/PROJET-JEE-MiniShop/src/main/resources/database/schema.sql;

# Ou directement depuis le terminal
mysql -u root -p < src/main/resources/database/schema.sql
```

### Option B : XAMPP/PhpMyAdmin

1. Démarrer XAMPP et lancer MySQL
2. Ouvrir phpMyAdmin : http://localhost/phpmyadmin
3. Créer une nouvelle base `minishop`
4. Onglet "SQL" → Copier/coller le contenu de `schema.sql`
5. Cliquer sur "Exécuter"

### Vérification

```sql
USE minishop;
SHOW TABLES;
-- Doit afficher : users, products

SELECT COUNT(*) FROM users;
-- Doit afficher : 4 utilisateurs

SELECT COUNT(*) FROM products;
-- Doit afficher : 20 produits
```

---

## ⚙️ ÉTAPE 2 : CONFIGURER LE PROJET

### 1. Vérifier les paramètres de connexion MySQL

Ouvrir : `src/main/java/com/minishop/config/AppConstants.java`

```java
// Si vous utilisez XAMPP/WAMP (mot de passe vide)
public static final String DB_PASSWORD = "";

// Si vous avez défini un mot de passe MySQL
public static final String DB_PASSWORD = "votre_mot_de_passe";

// Si MySQL n'est pas sur localhost:3306
public static final String DB_URL = "jdbc:mysql://VOTRE_HOST:PORT/minishop?useSSL=false&serverTimezone=Europe/Paris&allowPublicKeyRetrieval=true";
```

### 2. Compiler le projet

```bash
# Se placer à la racine du projet
cd PROJET-JEE-MiniShop

# Compiler et créer le WAR
mvn clean package

# Le fichier minishop.war sera créé dans target/
```

En cas d'erreur Maven, vérifier :
- JDK 17 est bien installé : `java -version`
- Maven est bien configuré : `mvn -version`
- Le fichier `pom.xml` est présent

---

## 🚀 ÉTAPE 3 : DÉPLOYER SUR TOMCAT 11

### Méthode A : Copie manuelle du WAR

```bash
# 1. Copier le WAR dans Tomcat
cp target/minishop.war $TOMCAT_HOME/webapps/

# Exemple Windows
copy target\minishop.war C:\tomcat11\webapps\

# Exemple Mac/Linux
cp target/minishop.war /opt/tomcat11/webapps/
```

### Méthode B : Via IntelliJ IDEA

1. **Configurer Tomcat dans IntelliJ**
   - `Run` → `Edit Configurations`
   - Cliquer sur `+` → `Tomcat Server` → `Local`
   - Application server : Pointer vers votre Tomcat 11
   - Deployment : Ajouter `minishop:war exploded`
   - Application context : `/minishop`

2. **Lancer le serveur**
   - Cliquer sur le bouton ▶️ Run
   - Ou `Shift+F10`

### Méthode C : Ligne de commande Tomcat

```bash
# Démarrer Tomcat
# Windows
C:\tomcat11\bin\startup.bat

# Mac/Linux
/opt/tomcat11/bin/startup.sh

# Arrêter Tomcat
# Windows
C:\tomcat11\bin\shutdown.bat

# Mac/Linux
/opt/tomcat11/bin/shutdown.sh
```

---

## 🌐 ÉTAPE 4 : ACCÉDER À L'APPLICATION

### URLs principales

- **Page d'accueil** : http://localhost:8080/minishop/
- **Catalogue** : http://localhost:8080/minishop/products
- **Connexion** : http://localhost:8080/minishop/login

### Comptes de test

**Tous les comptes utilisent le mot de passe : `password123`**

| Email | Rôle | Usage |
|-------|------|-------|
| admin@minishop.com | ADMIN | Administration |
| user@minishop.com | USER | Utilisateur classique |
| john.doe@example.com | USER | Tests |
| marie.dupont@example.com | USER | Tests |

---

## 🧪 ÉTAPE 5 : TESTER LES FONCTIONNALITÉS

### Test du Palier 1 - Authentification

1. Accéder à http://localhost:8080/minishop/login
2. Saisir : `user@minishop.com` / `password123`
3. ✅ Vous devez être redirigé vers `/products`
4. ✅ Le menu doit afficher "Utilisateur Test" et "Panier"
5. Cliquer sur "Déconnexion"
6. ✅ Vous devez être redirigé vers `/home`

### Test du Palier 2 - Catalogue

1. Accéder à http://localhost:8080/minishop/products
2. ✅ Vous devez voir 20 produits
3. Cliquer sur "Voir le détail" d'un produit
4. ✅ Page détail avec prix, stock, description

### Test du Palier 3 - Panier Session

1. Se connecter avec `user@minishop.com`
2. Aller sur un produit → Ajouter au panier
3. Cliquer sur "Panier" dans le menu
4. ✅ Le produit doit apparaître
5. ✅ Le total doit être calculé
6. Supprimer le produit → ✅ Panier vide

### Test du Palier 4 - LocalStorage + Fusion

1. **SANS être connecté** :
   - Aller sur un produit
   - Cliquer sur "Ajouter au panier" (bouton bleu)
   - ✅ Notification "Produit ajouté à votre panier invité"
   - ✅ Badge panier doit afficher "1"

2. **Vérifier LocalStorage** :
   - Ouvrir DevTools (F12)
   - Onglet "Application" → "Local Storage"
   - ✅ Vérifier `minishop_cart` contient le produit

3. **Se connecter** :
   - Cliquer sur "Connexion"
   - Saisir `user@minishop.com` / `password123`
   - ✅ Après connexion, aller sur "Panier"
   - ✅ Le produit du panier invité doit être présent

4. **Vérifier fusion** :
   - Ouvrir DevTools → Local Storage
   - ✅ `minishop_cart` doit être vide (fusionné en session)

---

## 🔧 DÉPANNAGE

### Erreur : "Driver MySQL non trouvé"

**Cause** : Dépendance MySQL manquante

**Solution** :
```bash
mvn dependency:purge-local-repository
mvn clean install
```

### Erreur : "Cannot connect to database"

**Causes possibles** :
1. MySQL n'est pas démarré
2. Mauvais mot de passe dans `AppConstants.java`
3. Base `minishop` n'existe pas

**Solutions** :
```bash
# Vérifier si MySQL tourne
# Windows (XAMPP)
Panneau XAMPP → Démarrer MySQL

# Linux
sudo systemctl status mysql

# Tester la connexion
mysql -u root -p
USE minishop;
```

### Erreur : "404 Not Found"

**Causes** :
1. Le WAR n'est pas déployé
2. Tomcat n'est pas démarré
3. Mauvaise URL

**Vérifier** :
- Le fichier `minishop.war` est dans `$TOMCAT_HOME/webapps/`
- Tomcat affiche "Deployment of web application archive ... has finished"
- URL correcte : `http://localhost:8080/minishop/home` (pas `/minishop.war/`)

### Erreur : "500 Internal Server Error"

**Étapes** :
1. Ouvrir `$TOMCAT_HOME/logs/catalina.out`
2. Chercher l'exception Java
3. Vérifier :
   - Connexion MySQL OK
   - Toutes les tables créées
   - Données insérées

### Les JSP ne s'affichent pas correctement

**Cause** : Dépendance JSTL manquante

**Solution** :
```bash
mvn clean package -U
```

### LocalStorage ne fonctionne pas

**Vérifier** :
1. DevTools → Console → Erreurs JavaScript ?
2. Les fichiers JS sont bien chargés ?
   - `assets/js/theme.js`
   - `assets/js/cart-local.js`
   - `assets/js/cart-merge.js`
3. Navigateur en mode privé ? (LocalStorage désactivé)

---

## 📊 STRUCTURE DE LA BASE DE DONNÉES

```
minishop
├── users
│   ├── id (BIGINT, PK)
│   ├── email (VARCHAR(190), UNIQUE)
│   ├── password_hash (VARCHAR(255)) [SHA-256]
│   ├── full_name (VARCHAR(120))
│   ├── role (ENUM: USER, ADMIN)
│   └── created_at (TIMESTAMP)
│
└── products
    ├── id (BIGINT, PK)
    ├── name (VARCHAR(140))
    ├── description (TEXT)
    ├── price_cents (INT) [en centimes]
    ├── stock (INT)
    └── created_at (TIMESTAMP)
```

**⚠️ RAPPEL IMPORTANT** :
- Le panier n'est **JAMAIS** en base de données
- Utilisateurs connectés → Panier en **SESSION**
- Invités → Panier en **LocalStorage**

---

## 📝 CONFORMITÉ À L'ÉNONCÉ

✅ **Technologies strictement respectées** :
- Servlets + JSP + JSTL + JDBC
- Tomcat 11 (Jakarta EE)
- MySQL 8.0+
- Sessions + LocalStorage

✅ **Aucune technologie interdite** :
- ❌ Pas de Spring
- ❌ Pas de JPA/Hibernate
- ❌ Pas de framework MVC
- ❌ Pas d'API REST additionnelle

✅ **Base de données minimale** :
- 2 tables uniquement (users, products)
- Aucune table cart/orders/etc.
- Panier géré en session

✅ **Tous les paliers implémentés** :
- Palier 0 : Démarrage ✅
- Palier 1 : Authentification ✅
- Palier 2 : Catalogue ✅
- Palier 3 : Panier session ✅
- Palier 4 : LocalStorage + fusion ✅

---

## 🎯 LIVRABLES ATTENDUS

1. ✅ **Code source complet** (Maven WAR)
2. ✅ **Script SQL exécuté** (`schema.sql`)
3. 📸 **Captures d'écran à fournir** :
   - Local Storage (DevTools → Application → Local Storage)
   - Redirection AuthFilter (tentative d'accès `/app/cart` non connecté)
   - Panier en session (après connexion avec produits)
   - Fusion panier (avant/après connexion)

---

## 📞 SUPPORT

En cas de problème persistant :

1. Vérifier les logs Tomcat : `$TOMCAT_HOME/logs/catalina.out`
2. Vérifier la console navigateur (F12 → Console)
3. Tester la connexion MySQL : `mysql -u root -p`
4. Recompiler complètement : `mvn clean package`
5. Redémarrer Tomcat

---

## ✅ CHECKLIST FINALE

- [ ] JDK 17 installé et configuré
- [ ] Tomcat 11 téléchargé et configuré
- [ ] MySQL démarré
- [ ] Base `minishop` créée avec le script SQL
- [ ] 4 utilisateurs présents dans la table `users`
- [ ] 20 produits présents dans la table `products`
- [ ] Mot de passe MySQL configuré dans `AppConstants.java`
- [ ] Projet compilé : `mvn clean package` ✅
- [ ] WAR déployé dans `webapps/`
- [ ] Tomcat démarré
- [ ] Application accessible : http://localhost:8080/minishop/
- [ ] Test connexion réussi
- [ ] Test panier réussi
- [ ] Test LocalStorage réussi
- [ ] Test fusion panier réussi

---

**🎉 Votre application MiniShop est maintenant opérationnelle !**

