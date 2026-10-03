# arc42 Softwarearchitektur-Dokumentation: Ortswehrführer

Dieses Dokument strukturiert die Architekturbeschreibung nach dem standardisierten arc42-Template,
um ein Höchstmaß an Transparenz, Verständlichkeit und Effizienz für alle Beteiligten zu
gewährleisten.

---

## 1. Einführung und Ziele

### 1.1 Aufgabenstellung

Ziel dieser Anwendung soll es sein, dass ehrenamtliche Feuerwehren einfach und komfortable Ihre
Ortswehr verwalten können.
Dazu soll die Anwendung die grundlegenden Funktionen einer Feuerwehrverwaltung bieten. Zu diesen
Funktionen gehört es:

* Gründung einer Ortswehr zu unterstützen
* Verwaltung von Gebäuden und Kameraden
* Unterstützung bei der Ausbildung auf Basis der Lehrgänge und Feuerwehrdienstvorschriften
* Unterstützen bei den regelmäßigen Belehrungen und Einsatzübungen
* Unterstützung bei der Gerätewartung

### 1.2 Qualitätsziele

*Priorisierte Übersicht der wichtigsten Qualitätsziele (z. B. Zuverlässigkeit, Wartbarkeit,
Performance, Usability).*

| Priorität | Qualitätsziel                 | Motivation & Nutzen                                                                 |
|-----------|-------------------------------|-------------------------------------------------------------------------------------|
| 1         | Wartbarkeit & Erweiterbarkeit | Saubere DDD-Struktur und ArchUnit-Validierung minimieren künftige Wartungsaufwände. |
| 2         | Robustheit & Korrektheit      | Fehlerfreie Erfassung und Verwaltung von Wehren, Gebäuden und Kameraden.            |
| 3         | Performance & Ressourcen      | Schlanke Laufzeit und schnelle Startzeiten durch Quarkus und Java 21.               |

### 1.3 Stakeholder

*Übersicht über Rollen und Personen, die ein Interesse am System haben.*

| Rolle                             | Erwartungshaltung & Nutzen                                                      |
|-----------------------------------|---------------------------------------------------------------------------------|
| Ortswehrführer / Feuerwehrleitung | Einfache, verlässliche Verwaltung von Einheiten, Gebäuden und Mitgliedern.      |
| Entwickler / Architekten          | Klare Architekturvorgaben, sauberes Domänenmodell, automatisierte Tests.        |
| Betrieb / DevOps                  | Wartungsarmer, ressourcenschonender Betrieb als Cloud-Native / Quarkus-Service. |

---

## 2. Randbedingungen

### 2.1 Technische Randbedingungen

- **Sprache & Laufzeitumgebung**: Java 21
- **Framework**: Quarkus
- **Architektur-Frameworks**: jMolecules (DDD-Annotationen), ArchUnit (Architekturprüfung)
- **Build-Tool**: Apache Maven (`./mvnw`)

### 2.2 Organisatorische Randbedingungen

- Entwicklung nach Clean-Code- und Domain-Driven-Design-Prinzipien.
- Richtlinien und Konventionen sind in `.junie/AGENTS.md` verankert.

### 2.3 Konventionen

- Deutsche Fachbegriffe in der Fachdomäne (z. B. `Wehr`, `Gebaeude`, `Kamerad`, `gruenden`).
- Value Objects werden ausnahmslos als Java `record` modelliert.
- Automatisierte Einhaltung aller Vorgaben via ArchUnit (`ArchitectureTest`).

---

## 3. Kontext und Abgrenzung

### 3.1 Fachlicher Kontext

*Fachliche Schnittstellen, externe Systeme, Benutzergruppen und Eingabe-/Ausgabedaten.*

```text
+-------------------+        +--------------------+        +---------------------+
| Ortswehrführer /  | -----> |  ortswehrfuehrer   | -----> | Externe Systeme /   |
| Sachbearbeiter    |        |  (Kernanwendung)   |        | Verbandssysteme     |
+-------------------+        +--------------------+        +---------------------+
```

### 3.2 Technischer Kontext

*Kommunikationsprotokolle, Transportmedien, Datenformate und technische Schnittstellen.*

---

## 4. Lösungsstrategie

- **Domain-Driven Design (DDD)**: Klare Trennung von Domänenlogik und technischen Komponenten.
  Aggregate Roots kontrollieren Invarianten.
- **Microservice / Cloud-Native**: Einsatz von Quarkus für optimierten Ressourcenverbrauch und hohe
  Ausführungsgeschwindigkeit.
- **Testgetriebene Qualitätssicherung**: Unittests mit JUnit 5 und automatisierte
  Architekturüberwachung mit ArchUnit / jMolecules.

---

## 5. Bausteinsicht

### 5.1 Whitebox Gesamtsystem (Ebene 1)

```text
+-------------------------------------------------------------+
| de.eichstaedt.ortswehrfuehrer                               |
|                                                             |
|   +-------------------------------------------------------+ |
|   | domain (DDD Aggregate, Entities, Value Objects)       | |
|   |  - Wehr (AggregateRoot)                               | |
|   |  - Gebaeude (Entity)                                  | |
|   |  - Kamerad (Entity)                                   | |
|   |  - Adresse (ValueObject / Record)                     | |
|   +-------------------------------------------------------+ |
|                                                             |
|   +-------------------------------------------------------+ |
|   | application / service / rest (Quarkus-Infrastruktur)  | |
|   +-------------------------------------------------------+ |
+-------------------------------------------------------------+
```

### 5.2 Bausteine Ebene 2

*Detailliertere Beschreibung der Subsysteme und Komponenten.*

---

## 6. Laufzeitsicht

### 6.1 Szenario 1: Gründung einer Wehr

1. Initialisierung des Aggregate Roots `Wehr`.
2. Aufruf von `gruenden(name, gruendungsdatum)`.
3. Vergabe einer eindeutigen UUID und Setzen des Gründungsdatums.

### 6.2 Szenario 2: Hinzufügen von Gebäuden und Kameraden

1. Erstellung der Entität `Gebaeude` bzw. `Kamerad` mit eindeutiger ID.
2. Zuweisung über das Aggregate Root (`gebaeudeHinzufuegen`, `kameradHinzufuegen`).

---

## 7. Verteilungssicht

### 7.1 Infrastruktur & Ausführung

- **Entwicklungsmodus**:
  ```shell
  ./mvnw quarkus:dev
  ```
- **Paketierung (Standard / Fast-Jar)**:
  ```shell
  ./mvnw package
  java -jar target/quarkus-app/quarkus-run.jar
  ```
- **Paketierung (Über-Jar)**:
  ```shell
  ./mvnw package -Dquarkus.package.jar.type=uber-jar
  java -jar target/*-runner.jar
  ```
- **Natives Executable**:
  ```shell
  ./mvnw package -Dnative
  ./target/ortswehrfuehrer-1.0-SNAPSHOT-runner
  ```

---

## 8. Querschnittliche Konzepte

- **Immutability**: Konsequente Verwendung von Records für Value Objects zur Vermeidung
  unerwünschter Seiteneffekte.
- **Fluent APIs**: Konstruktive Methodenrückgaben zur Erleichterung von Aufrufketten und lesbarem
  Code.
- **Architekturvalidierung**: Prüfung via ArchUnit in jedem Build-Zyklus (`./mvnw test`).

---

## 9. Architekturentscheidungen

| ADR    | Titel                           | Status     | Begründung & Nutzen                                                                   |
|--------|---------------------------------|------------|---------------------------------------------------------------------------------------|
| ADR-01 | Einsatz von Quarkus & Java 21   | Angenommen | Hohe Ausführungsgeschwindigkeit, geringer Speicherverbrauch, moderne Sprachmittel.    |
| ADR-02 | DDD-Modellierung mit jMolecules | Angenommen | Strukturierte Domänentrennung und automatische Erkennung von Architekturverletzungen. |
| ADR-03 | Value Objects als Java Records  | Angenommen | Garantierte Unveränderlichkeit, kein Boilerplate-Code, maximale Fehlervermeidung.     |

---

## 10. Qualitätsanforderungen

*Konkrete Szenarien zur Bewertung der Qualitätsziele aus Kapitel 1.2.*

| ID  | Qualitätsmerkmal | Szenario                              | Erwartete Reaktion                        |
|-----|------------------|---------------------------------------|-------------------------------------------|
| Q-1 | Wartbarkeit      | Verletzung von DDD-Regeln im Code     | Build schlägt in `ArchitectureTest` fehl. |
| Q-2 | Robustheit       | Wehr-Erstellung mit ungültigen Werten | Saubere Fehlerbehandlung / Validierung.   |

---

## 11. Risiken und technische Schulden

| Risiko / Technische Schuld                | Auswirkung          | Gegenmaßnahme                                  |
|-------------------------------------------|---------------------|------------------------------------------------|
| Zunehmende Komplexität der Domäne         | Unübersichtlichkeit | Strikte Aggregatgrenzen und ArchUnit-Prüfungen |
| Mangelnde Testabdeckung bei Erweiterungen | Regressionen        | Hohe Testabdeckung für jede Domänenänderung    |

---

## 12. Glossar

| Begriff        | Definition                                                                                             |
|----------------|--------------------------------------------------------------------------------------------------------|
| **Wehr**       | Aggregate Root, repräsentiert eine Feuerwehr-Einheit mit Name, Gründungsdatum, Gebäuden und Kameraden. |
| **Gebäude**    | Entity innerhalb des Wehr-Aggregats zur Verwaltung von Liegenschaften.                                 |
| **Kamerad**    | Entity innerhalb des Wehr-Aggregats zur Verwaltung von Feuerwehrmitgliedern.                           |
| **Adresse**    | Value Object (Java Record) bestehend aus Straße, Hausnummer, PLZ und Ort.                              |
| **jMolecules** | Bibliothek zur expliziten Annotation von DDD-Konzepten im Quellcode.                                   |
| **ArchUnit**   | Testwerkzeug zur automatisierten Absicherung von Architektur- und Designregeln.                        |
