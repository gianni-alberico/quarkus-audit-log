# Quarkus Audit Log Extension

Extension Quarkus qui journalise automatiquement les requêtes/réponses HTTP entrantes via un filtre REST.

## Objectif

Cette extension sert à tracer l’activité HTTP de votre application pour :

- audit technique des appels API,
- diagnostic et debugging en environnement de test/recette/prod,
- suivi de performance basique (durée par requête),
- aide à l’investigation d’incidents.

Chaque requête reçoit un identifiant unique (`requestId`) présent dans le log de requête et de réponse.

## Fonctionnalités

- Log de la requête : méthode HTTP + chemin + (optionnellement) body.
- Log de la réponse : méthode HTTP + chemin + code HTTP + durée + (optionnellement) body.
- Activation/désactivation globale.
- Exclusion de chemins (avec `*`).
- Sérialisation des bodies de réponse via :
  - Jackson si disponible,
  - sinon JSON-B si disponible,
  - sinon `toString()`.

## Exemple de logs

```text
[4f5d5c9e-2b6f-4d9f-a110-9d4e9a2c6f41] Request GET /audit-log
[4f5d5c9e-2b6f-4d9f-a110-9d4e9a2c6f41] Response GET /audit-log status=200 duration=8ms
```

Avec `log-body` activé :

```text
[3c8b1db5-9de8-42b6-8a10-2c3277a2dbf6] Request POST orders body={"id":42}
[3c8b1db5-9de8-42b6-8a10-2c3277a2dbf6] Response POST orders status=201 duration=15ms body={"status":"CREATED"}
```

## Installation

### 1) Construire l’extension

Depuis ce repository :

```bash
mvn clean install
```

### 2) Ajouter la dépendance runtime dans votre application Quarkus

```xml
<dependency>
  <groupId>io.github.giannialberico</groupId>
  <artifactId>quarkus-audit-log</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

Notes :

- L’extension utilise un filtre REST (`quarkus-rest`).
- Java 17 est utilisé dans ce projet.

## Configuration

Propriétés disponibles (préfixe `quarkus.audit-log`) :

| Propriété | Type | Défaut | Description |
|---|---|---|---|
| `quarkus.audit-log.enabled` | `boolean` | `true` | Active/désactive complètement l’extension. |
| `quarkus.audit-log.log-body` | `boolean` | `false` | Log le body requête/réponse. |
| `quarkus.audit-log.excluded-paths` | `List<String>` | vide | Liste de chemins/patterns exclus des logs. |

Exemple :

```properties
# Active l’audit log
quarkus.audit-log.enabled=true

# Active le body (attention aux données sensibles)
quarkus.audit-log.log-body=false

# Exclut certains endpoints
quarkus.audit-log.excluded-paths=/q/health,/q/metrics,/internal/*
```

## Utilisation rapide

1. Ajouter la dépendance.
2. Configurer `application.properties`.
3. Démarrer l’application.
4. Appeler vos endpoints et observer les logs applicatifs.

Aucune annotation supplémentaire n’est nécessaire sur vos ressources REST.

## Points d’attention

- `log-body=true` peut exposer des données sensibles (PII, secrets, tokens).
- La lecture/sérialisation des bodies ajoute un coût CPU/mémoire.
- Le filtrage des chemins exclus est basé sur des patterns compilés en regex.
- En cas d’échec de sérialisation JSON (Jackson/JSON-B), une exception runtime de sérialisation peut être levée.

## Structure du projet

- `runtime/` : logique d’exécution (filtre, config, sérialisation).
- `deployment/` : enregistrement Quarkus (build steps et beans).