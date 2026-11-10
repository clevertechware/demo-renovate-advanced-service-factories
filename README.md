# demo-renovate-advanced-service-factories

Service Spring Boot (Java 21, Maven) qui expose le catalogue des entrepôts. Il sert de dépôt de démonstration à la série
d'articles sur Renovate : les versions des dépendances, du wrapper Maven, du `Dockerfile` et des actions GitHub sont
anciennes volontairement, pour que Renovate propose leur mise à jour.

| Fichier | Version figée |
|---|---|
| `pom.xml` | `spring-boot-starter-parent` 3.4.0, `springdoc-openapi-starter-webmvc-ui` 2.6.0, `jacoco-maven-plugin` 0.8.11 |
| `.mvn/wrapper/maven-wrapper.properties` | Maven 3.9.6, wrapper 3.2.0 |
| `Dockerfile` | `eclipse-temurin:21-jre-alpine` |
| `.github/workflows/ci.yml` | `actions/checkout@v4`, `actions/setup-java@v4`, Java `21` |

## Prérequis

- JDK 21
- Docker (les tests d'intégration démarrent un Postgres avec Testcontainers)
- Pour lancer le service : l'instance Postgres du dépôt infra (base `factories`) et, pour `/warehouses/{code}/plan`,
  `service-plan` sur le port 8081

## Build et tests

```bash
./mvnw verify
```

Avec un Docker Engine 29 ou plus récent, la version de Testcontainers gérée par Spring Boot 3.4.0 (1.20.4) négocie une
API Docker trop ancienne et ne trouve pas le démon. Le workflow CI passe `-Dapi.version=1.44` pour l'éviter ; la même
option fonctionne en local : `./mvnw -Dapi.version=1.44 verify`.

## Lancement

```bash
./mvnw spring-boot:run
```

Variables d'environnement : `DB_URL` (défaut `jdbc:postgresql://localhost:5432/factories`), `DB_USER` et `DB_PASSWORD`
(défaut `factories`), `PLAN_SERVICE_URL` (défaut `http://localhost:8081`). Les identifiants du dépôt infra
(`postgres`/`postgres`) ne servent qu'à la démo locale.

Image Docker :

```bash
./mvnw -DskipTests package
docker build -t service-factories .
```

## API

| Route | Réponse |
|---|---|
| `GET /warehouses` | liste JSON des entrepôts (`LYS-01`, `NTE-02`, `LIL-03`) |
| `GET /warehouses/{code}` | entrepôt en JSON, 404 s'il n'existe pas |
| `GET /warehouses/{code}/plan` | SVG du plan (`image/svg+xml`) obtenu auprès de `service-plan`, 404 si l'entrepôt ou le plan n'existe pas, 502 si `service-plan` est indisponible |

La documentation OpenAPI est servie sur `/swagger-ui.html`.

## Renovate

`.github/renovate.json` étend le preset `java-service` du dépôt `demo-renovate-advanced-config`. Ce dépôt ne contient
pas de workflow Renovate : l'exécution est centralisée dans `demo-renovate-advanced-config`.
