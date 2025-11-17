# Test Technique Cosium - Optimisation Flux Ordonnance & Commande

## Contexte

Vous intervenez sur un service SaaS santé qui gère des ordonnances optiques et des commandes de verres correcteurs. Trois problèmes critiques ont été remontés par les équipes de production :

1. **Bug métier** : Des ordonnances valides sont incorrectement marquées comme non éligibles
2. **Problème de performance** : L'endpoint de liste des commandes est très lent (850ms pour 100 commandes)
3. **Bug UI** : Le total de la commande affiche `NaN` dans certaines conditions

Votre mission est de corriger ces trois problèmes, d'écrire des tests pour valider vos corrections, et de documenter votre approche.

## Stack Technique

### Backend
- **Java 17** avec **Spring Boot 3.2**
- **JPA / Hibernate 6** pour la persistence
- **PostgreSQL 14** comme base de données
- **Maven** pour la gestion des dépendances
- **JUnit 5** pour les tests

### Frontend
- **Angular 15** avec **TypeScript 4.8**
- **Reactive Forms** pour la gestion des formulaires
- **Jasmine/Karma** pour les tests

## Architecture du Projet

```
cosium-prescription-optimizer/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/cosium/prescription/
│   │   │   │   ├── model/          # Entités JPA
│   │   │   │   ├── repository/     # Repositories Spring Data
│   │   │   │   ├── service/        # Logique métier
│   │   │   │   ├── controller/     # Endpoints REST
│   │   │   │   └── dto/            # Data Transfer Objects
│   │   │   └── resources/
│   │   │       ├── application.yaml
│   │   │       └── data.sql        # Données de test
│   │   └── test/
│   │       └── java/com/cosium/prescription/
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   └── app/
│   │       ├── checkout/           # Composant formulaire
│   │       └── models/             # Interfaces TypeScript
│   ├── package.json
│   └── angular.json
└── docker-compose.yml
```

## Installation et Démarrage

### Prérequis

- **Java 17** ou supérieur
- **Node.js 16+** et **npm**
- **Docker** et **Docker Compose**
- **Maven 3.8+**

### 1. Démarrer la Base de Données

```bash
# Depuis la racine du projet
docker-compose up -d

# Vérifier que PostgreSQL est démarré
docker-compose ps
```

La base de données sera accessible sur `localhost:5432` avec les credentials :
- Database: `prescription_db`
- Username: `prescription_user`
- Password: `prescription_pass`

### 2. Démarrer le Backend

```bash
cd backend

# Compiler et lancer les tests
mvn clean test

# Démarrer l'application
mvn spring-boot:run
```

Le backend sera accessible sur `http://localhost:8080`

### 3. Démarrer le Frontend

```bash
cd frontend

# Installer les dépendances
npm install

# Démarrer le serveur de développement
npm start
```

Le frontend sera accessible sur `http://localhost:4200`

### 4. Lancer les Tests

**Backend :**
```bash
cd backend
mvn test
```

**Frontend :**
```bash
cd frontend
npm test
```

## Les 3 Problèmes à Résoudre

### 🐛 Problème 1 : Bug d'Éligibilité d'Ordonnance (Backend)

**Endpoint concerné :** `GET /api/prescriptions/{id}/eligibility`

**Symptôme :** Certaines ordonnances valides créées "hier soir" retournent `covered=false` alors qu'elles devraient être valides.

**Fichier à corriger :** `backend/src/main/java/com/cosium/prescription/service/EligibilityService.java`

**Comportement attendu :**
- Une ordonnance est éligible si la date actuelle (fuseau Europe/Paris) est entre `issuedAt` et `expiresAt` (bornes **inclusives**)
- Le patient doit avoir `active=true`

**Livrables attendus :**
- ✅ Corriger la logique de comparaison de dates avec gestion correcte du fuseau horaire
- ✅ Écrire au moins 2 tests unitaires JUnit 5 couvrant les cas limites :
  - Ordonnance émise hier à 23h30 (doit être valide aujourd'hui)
  - Ordonnance émise aujourd'hui à 00h15 (doit être valide)
- ✅ S'assurer que les dates sont cohérentes en présentation (ZonedDateTime/LocalDate)

**Exemple de test à créer :**
```bash
curl http://localhost:8080/api/prescriptions/1/eligibility
```

### ⚡ Problème 2 : Performance N+1 sur Liste des Commandes (Backend)

**Endpoint concerné :** `GET /api/orders?from=...&to=...`

**Symptôme :** Temps de réponse de ~850ms pour 100 commandes (attendu < 100ms)

**Cause identifiée :** 
- Problème N+1 : 201 requêtes SQL au lieu de 1-2
- Filtrage par période fait en mémoire Java au lieu de SQL
- Pas de `JOIN FETCH` sur `patient` et `orderItems`

**Fichiers à corriger :**
- `backend/src/main/java/com/cosium/prescription/repository/OrderRepository.java`
- `backend/src/main/java/com/cosium/prescription/service/OrderService.java`

**Livrables attendus :**
- ✅ Réécrire la requête avec `JOIN FETCH` pour charger `patient` et `orderItems` en une seule requête
- ✅ Déplacer le filtrage par période dans la clause `WHERE` SQL
- ✅ Écrire un test (slice `@DataJpaTest` ou unitaire avec mocks) qui vérifie l'absence de N+1
- ✅ Proposer en commentaire l'index PostgreSQL optimal (ex: sur `created_at` et/ou `patient_id`)

**Indice :** Vous pouvez utiliser `@Query` avec JPQL ou créer une méthode de query dérivée optimisée.

**Exemple de test à créer :**
```bash
curl "http://localhost:8080/api/orders?from=2024-11-10T00:00:00Z&to=2024-11-18T23:59:59Z"
```

### 🎨 Problème 3 : Bug NaN dans le Formulaire Angular (Frontend)

**Composant concerné :** `frontend/src/app/checkout/checkout.component.ts`

**Symptôme :** Le total affiche `NaN` quand l'utilisateur efface le code de réduction ou perd le focus.

**Cause :** La méthode `calculateTotal()` ne gère pas le cas où `discountCode` est vide ou invalide.

**Livrables attendus :**
- ✅ Corriger le calcul pour gérer les codes de réduction vides/invalides (nullish coalescing, valeur par défaut)
- ✅ Écrire au moins 2 tests unitaires Jasmine :
  - Calcul avec code de réduction valide
  - Calcul avec code de réduction vide (ne doit pas produire NaN)
  - Calcul avec code de réduction invalide
- ✅ **Bonus (5 min)** : Corriger la fuite de souscription observable (utiliser `takeUntil` avec le `Subject` déjà présent)

**Test manuel :**
1. Sélectionner un type de verre
2. Entrer un code de réduction valide → Le total doit être correct
3. Effacer le code de réduction → Le total ne doit PAS afficher NaN

## Critères d'Évaluation

Votre travail sera évalué sur :

### Correction Fonctionnelle (40%)
- ✅ Les 3 bugs sont corrigés
- ✅ Les comportements attendus sont respectés
- ✅ Aucune régression introduite

### Tests (30%)
- ✅ Tests unitaires pertinents et exhaustifs
- ✅ Cas limites couverts
- ✅ Tests maintenables et lisibles

### Qualité du Code (20%)
- ✅ Code propre et idiomatique (Java/TypeScript)
- ✅ Respect des conventions Spring Boot / Angular
- ✅ Commentaires utiles (pas évidents)
- ✅ Gestion d'erreurs appropriée

### Documentation (10%)
- ✅ Explication claire des corrections
- ✅ Justification des choix techniques
- ✅ Index PostgreSQL proposé et justifié

## Temps Estimé

**60 minutes** réparties approximativement :
- 30 min : Backend (bug éligibilité + tests + perf N+1)
- 20 min : Performance requêtes + tests
- 10 min : Frontend (bug NaN + tests)

## Livrables Attendus

À la fin du test, vous devez fournir :

1. **Code corrigé** dans les fichiers concernés
2. **Tests unitaires** fonctionnels
3. **Fichier NOTES.md** (≤ 200 mots) expliquant :
   - Le problème de fuseau horaire et la solution
   - La stratégie anti-N+1 choisie (JOIN FETCH vs batch)
   - L'index PostgreSQL proposé avec justification
   - Le garde-fou côté Angular pour NaN

4. **(Optionnel)** Exemples de commandes curl/HTTPie pour tester les endpoints

## Données de Test Disponibles

Le fichier `data.sql` contient des données pré-chargées :
- 5 patients (dont certains inactifs)
- 5 ordonnances (avec différents scénarios de dates)
- 7 commandes avec items
- Codes de réduction : `SUMMER2024`, `VIP10`, `WELCOME`

## Endpoints API Disponibles

### Backend REST API

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/api/prescriptions/{id}/eligibility` | Vérifier l'éligibilité d'une ordonnance |
| `GET` | `/api/orders?from={ISO8601}&to={ISO8601}` | Liste des commandes par période |

### Exemples

```bash
# Vérifier éligibilité
curl http://localhost:8080/api/prescriptions/1/eligibility

# Liste des commandes (derniers 30 jours)
curl "http://localhost:8080/api/orders?from=2024-10-18T00:00:00Z&to=2024-11-18T23:59:59Z"
```

## Conseils

1. **Lisez d'abord le code existant** pour comprendre la logique métier
2. **Commencez par les tests** pour comprendre le comportement attendu
3. **N'hésitez pas à activer les logs SQL** pour voir les requêtes générées (`show-sql: true` déjà actif)
4. **Utilisez les outils de votre IDE** (debugger, profiler) pour diagnostiquer
5. **Restez simple** : pas besoin de sur-ingénierie, des corrections ciblées suffisent

## Support

Si vous avez des questions sur l'énoncé ou des problèmes techniques d'installation, contactez :
- Email : contact@huntlab.fr
- Ne cherchez pas à résoudre un problème d'installation pendant plus de 10 minutes

## Ressources

- [Spring Data JPA - Query Methods](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods)
- [Hibernate - Fetch Strategies](https://docs.jboss.org/hibernate/orm/6.0/userguide/html_single/Hibernate_User_Guide.html#fetching)
- [Angular Reactive Forms](https://angular.io/guide/reactive-forms)
- [RxJS takeUntil](https://rxjs.dev/api/operators/takeUntil)

---

**Bon courage ! 🚀**

