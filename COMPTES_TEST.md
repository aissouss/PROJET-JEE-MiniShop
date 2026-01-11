# 🔐 COMPTES DE TEST - MINISHOP

## 📋 Tous les comptes

**Mot de passe universel** : `password123`

**Hash SHA-256** : `ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f`

---

## 👤 Comptes disponibles

### 🔴 ADMINISTRATEUR

| Champ | Valeur |
|-------|--------|
| **Email** | admin@minishop.com |
| **Mot de passe** | password123 |
| **Nom complet** | Admin MiniShop |
| **Rôle** | ADMIN |
| **Usage** | Tests administration (futur) |

---

### 🟢 UTILISATEURS STANDARDS

#### Utilisateur 1 (recommandé pour les tests)
| Champ | Valeur |
|-------|--------|
| **Email** | user@minishop.com |
| **Mot de passe** | password123 |
| **Nom complet** | Utilisateur Test |
| **Rôle** | USER |
| **Usage** | Tests principaux |

#### Utilisateur 2
| Champ | Valeur |
|-------|--------|
| **Email** | john.doe@example.com |
| **Mot de passe** | password123 |
| **Nom complet** | John Doe |
| **Rôle** | USER |
| **Usage** | Tests secondaires |

#### Utilisateur 3
| Champ | Valeur |
|-------|--------|
| **Email** | marie.dupont@example.com |
| **Mot de passe** | password123 |
| **Nom complet** | Marie Dupont |
| **Rôle** | USER |
| **Usage** | Tests multi-utilisateurs |

---

## 🧪 Scénarios de test

### Scénario 1 : Authentification simple
```
1. Aller sur http://localhost:8080/minishop/login
2. Saisir :
   - Email : user@minishop.com
   - Mot de passe : password123
3. Cliquer sur "Se connecter"
4. ✅ Vous devez être redirigé vers /products
5. ✅ Le menu doit afficher "Utilisateur Test"
```

### Scénario 2 : Panier invité + Fusion
```
1. NE PAS SE CONNECTER
2. Aller sur http://localhost:8080/minishop/products
3. Choisir un produit → "Voir le détail"
4. Cliquer sur "Ajouter au panier"
5. ✅ Notification "Produit ajouté à votre panier invité"
6. ✅ Badge panier affiche "1"

7. Ouvrir DevTools (F12)
8. Application → Local Storage → http://localhost:8080
9. ✅ Voir "minishop_cart" avec le produit

10. Cliquer sur "Connexion"
11. Saisir : user@minishop.com / password123
12. Cliquer sur "Panier" dans le menu
13. ✅ Le produit du panier invité doit être présent
14. ✅ Dans DevTools, "minishop_cart" doit être vide
```

### Scénario 3 : Test AuthFilter
```
1. NE PAS SE CONNECTER
2. Tenter d'accéder directement à :
   http://localhost:8080/minishop/app/cart
3. ✅ Vous devez être redirigé vers /login
4. Se connecter avec john.doe@example.com / password123
5. ✅ Vous devez être redirigé vers /app/cart
```

### Scénario 4 : Multi-utilisateurs (sessions indépendantes)
```
Navigateur 1 (Chrome) :
1. Se connecter avec user@minishop.com
2. Ajouter "Laptop Dell XPS 13" au panier

Navigateur 2 (Firefox) :
1. Se connecter avec john.doe@example.com
2. Ajouter "iPhone 15 Pro" au panier

✅ Chaque utilisateur doit avoir son propre panier
✅ Les paniers ne doivent PAS se mélanger
```

### Scénario 5 : Déconnexion
```
1. Se connecter avec marie.dupont@example.com
2. Ajouter des produits au panier
3. Cliquer sur "Déconnexion"
4. ✅ Redirection vers /home
5. ✅ Menu affiche "Connexion" (pas "Marie Dupont")
6. Tenter d'accéder à /app/cart
7. ✅ Redirection vers /login
```

---

## 🗄️ Vérification en base de données

Pour vérifier que les comptes existent bien :

```sql
USE minishop;

-- Lister tous les utilisateurs
SELECT id, email, full_name, role FROM users;

-- Résultat attendu :
-- 1  | admin@minishop.com       | Admin MiniShop    | ADMIN
-- 2  | user@minishop.com        | Utilisateur Test  | USER
-- 3  | john.doe@example.com     | John Doe          | USER
-- 4  | marie.dupont@example.com | Marie Dupont      | USER

-- Vérifier le hash SHA-256
SELECT email, password_hash FROM users WHERE email = 'user@minishop.com';

-- Résultat attendu :
-- user@minishop.com | ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f
```

---

## 🔧 Générer un nouveau hash SHA-256

Si vous devez créer un nouveau compte avec un mot de passe différent :

### Méthode 1 : Online (rapide)
1. Aller sur https://emn178.github.io/online-tools/sha256.html
2. Saisir le mot de passe
3. Copier le hash généré

### Méthode 2 : Java (via le projet)
```java
import com.minishop.util.PasswordUtil;

public class TestPassword {
    public static void main(String[] args) {
        String password = "monMotDePasse";
        String hash = PasswordUtil.sha256(password);
        System.out.println("Hash SHA-256: " + hash);
    }
}
```

### Méthode 3 : Terminal Linux/Mac
```bash
echo -n "password123" | sha256sum
```

### Méthode 4 : PowerShell Windows
```powershell
$password = "password123"
$bytes = [System.Text.Encoding]::UTF8.GetBytes($password)
$hash = [System.Security.Cryptography.SHA256]::Create().ComputeHash($bytes)
[BitConverter]::ToString($hash).Replace("-", "").ToLower()
```

---

## 📊 Produits de test disponibles

Le projet inclut **20 produits de test** dans différentes catégories :

### Électronique (5 produits)
- Laptop Dell XPS 13 (1299.00 €)
- iPhone 15 Pro (1199.00 €)
- Samsung Galaxy S24 (899.00 €)
- iPad Pro 12.9 (1399.00 €)
- MacBook Air M2 (1199.00 €)

### Audio (2 produits)
- AirPods Pro 2 (249.00 €)
- Sony WH-1000XM5 (399.00 €)

### Accessoires (4 produits)
- Logitech MX Master 3S (99.00 €)
- Clavier Mécanique RGB (149.00 €)
- Anker PowerBank 20000mAh (49.00 €)
- SanDisk SSD Externe 1TB (119.00 €)

### Montres et Fitness (2 produits)
- Apple Watch Series 9 (449.00 €)
- Fitbit Charge 6 (149.00 €)

### Gaming (3 produits)
- PlayStation 5 (549.00 €)
- Xbox Series X (499.00 €)
- Nintendo Switch OLED (349.00 €)

### TV et Maison (2 produits)
- Smart TV Samsung 55" (799.00 €)
- Amazon Echo Dot 5 (59.00 €)

### Photo et Vidéo (2 produits)
- Canon EOS R6 (2499.00 €)
- GoPro Hero 12 (399.00 €)

---

## 🎯 Tests de charge (bonus)

Pour tester plusieurs utilisateurs simultanés :

```bash
# Terminal 1
curl -c cookies1.txt -d "email=user@minishop.com&password=password123" \
  http://localhost:8080/minishop/login

# Terminal 2
curl -c cookies2.txt -d "email=john.doe@example.com&password=password123" \
  http://localhost:8080/minishop/login

# Terminal 3
curl -c cookies3.txt -d "email=marie.dupont@example.com&password=password123" \
  http://localhost:8080/minishop/login
```

Chaque session doit être indépendante avec son propre panier.

---

## ⚠️ Notes importantes

### Sécurité (en production)
- ⚠️ `password123` est un mot de passe **de test uniquement**
- ⚠️ En production, utiliser des mots de passe forts
- ⚠️ Ajouter un salt au hashage SHA-256
- ⚠️ Utiliser bcrypt ou Argon2 plutôt que SHA-256 simple

### Sessions
- Durée par défaut : 30 minutes
- Peut être étendue avec "Se souvenir de moi" (30 jours)
- Sessions stockées en mémoire Tomcat (non persistantes)

### LocalStorage
- Limité à 5-10 MB selon le navigateur
- Accessible uniquement sur le même domaine
- Peut être vidé par l'utilisateur (Effacer les données)
- Non synchronisé entre navigateurs/appareils

---

## 🆘 Dépannage

### "Email ou mot de passe incorrect"

**Vérifications** :
```sql
-- 1. Vérifier que l'utilisateur existe
SELECT * FROM users WHERE email = 'user@minishop.com';

-- 2. Vérifier le hash
SELECT password_hash FROM users WHERE email = 'user@minishop.com';
-- Doit être : ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f

-- 3. Si le hash est différent, le corriger
UPDATE users 
SET password_hash = 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f'
WHERE email = 'user@minishop.com';
```

### Panier ne se fusionne pas

**Vérifications** :
1. Ouvrir DevTools → Console
2. Chercher des erreurs JavaScript
3. Vérifier que les fichiers JS sont chargés :
   - `assets/js/cart-local.js`
   - `assets/js/cart-merge.js`
4. Vérifier Local Storage :
   - F12 → Application → Local Storage
   - `minishop_cart` doit contenir un tableau JSON

---

## ✅ Checklist de test

Avant de considérer les tests comme réussis :

- [ ] Connexion réussie avec les 4 comptes
- [ ] Déconnexion fonctionne
- [ ] AuthFilter redirige vers login si non connecté
- [ ] Catalogue affiche 20 produits
- [ ] Détail produit affiche prix et stock
- [ ] Ajout au panier (connecté) fonctionne
- [ ] Suppression du panier fonctionne
- [ ] Total panier calculé correctement
- [ ] Panier invité (LocalStorage) fonctionne
- [ ] Badge panier mis à jour
- [ ] Fusion panier après login fonctionne
- [ ] LocalStorage vidé après fusion
- [ ] Sessions indépendantes (multi-utilisateurs)
- [ ] Messages flash affichés correctement
- [ ] Pages d'erreur 404/500 accessibles

---

**📝 Document créé le** : 11 janvier 2026
**🎯 Pour le projet** : PROJET JEE MiniShop
**✅ Tous les comptes testés et fonctionnels**

