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
|   |  - WehrFactory (Factory / GoF Pattern)                | |
|   |  - Einsatzfahrzeug (AggregateRoot)                     | |
|   |  - Gebaeude (Entity)                                  | |
|   |  - Abteilung (Entity)                                 | |
|   |  - Kamerad (Entity)                                   | |
|   |  - Fahrzeugtyp (Enum)                                 | |
|   |  - Adresse (ValueObject / Record)                     | |
|   |  - Abmessungen (ValueObject / Record)                 | |
|   +-------------------------------------------------------+ |
|                                                             |
|   +-------------------------------------------------------+ |
|   | application (Use Cases & Application Services)        | |
|   |  - WehrApplicationService (Orchestrierung & Logik)    | |
|   +-------------------------------------------------------+ |
|                                                             |
|   +-------------------------------------------------------+ |
|   | adapter.web (Quarkus Qute & HTMX Web UI)              | |
|   |  - IndexWebResource (Web Controller / Dashboard)      | |
|   |  - KameradWebResource (Web Controller / Kameraden)    | |
|   |  - templates/ (base.html, index.html)                 | |
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

### 6.2 Szenario 2: Hinzufügen von Gebäuden, Fahrzeugen und Kameraden

1. Erstellung der Entität `Gebaeude`, `Einsatzfahrzeug` bzw. `Kamerad` mit eindeutiger ID.
2. Zuweisung über das Aggregate Root (`gebaeudeHinzufuegen`, `fahrzeugHinzufuegen`, `kameradHinzufuegen`).
3. Automatische Zuordnung von Kameraden zu den Einsatzabteilungen (Jugendabteilung, Einsatzabteilung, Alters- und Ehrenabteilung) basierend auf dem Alter.

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
- **Logging**: SLF4J (`org.slf4j.Logger`) als einheitliche Logging-Facade über alle Anwendungsschichten mit strukturierter Dateiprotokollierung in `logs/ortswehrfuehrer.log` im Projektverzeichnis (Log-Rotation bei 10 MB).
- **Architekturvalidierung**: Prüfung via ArchUnit in jedem Build-Zyklus (`./mvnw test`).

---

## 9. Architekturentscheidungen

| ADR    | Titel                                      | Status     | Begründung & Nutzen                                                                   |
|--------|--------------------------------------------|------------|---------------------------------------------------------------------------------------|
| ADR-01 | Einsatz von Quarkus & Java 21              | Angenommen | Hohe Ausführungsgeschwindigkeit, geringer Speicherverbrauch, moderne Sprachmittel.    |
| ADR-02 | DDD-Modellierung mit jMolecules            | Angenommen | Strukturierte Domänentrennung und automatische Erkennung von Architekturverletzungen. |
| ADR-03 | Value Objects als Java Records             | Angenommen | Garantierte Unveränderlichkeit, kein Boilerplate-Code, maximale Fehlervermeidung.     |
| ADR-04 | Aggregate-Referenzierung via ID            | Angenommen | Entkopplung von Aggregate Roots (`Wehr` & `Einsatzfahrzeug`) zur Wahrung von Transaktionsgrenzen nach DDD. |
| ADR-05 | Web-UI-Architektur (Quarkus Qute & HTMX)   | Angenommen | Server-Side Rendering (SSR) mit typsicheren Templates (`quarkus-qute`), minimalem Ressourcenbedarf, nativer Build-Unterstützung und reaktiver Dynamik via HTMX. |

### 9.1 Detailansicht ADR-05: Auswahl der UI-Technologie

* **Kontext**:
  Die Anwendung *Ortswehrführer* benötigt eine intuitive, benutzerfreundliche Weboberfläche zur Verwaltung von Wehren, Liegenschaften, Einsatzfahrzeugen und Kameraden. Die Oberfläche muss leichtgewichtig, ressourcenschonend und nahtlos in den Quarkus-Stack sowie die Clean-Architecture-Schichten integrierbar sein.
* **Betrachtete Alternativen**:
  1. *Quarkus Qute + HTMX / TailwindCSS / Bootstrap*: Typsichere Server-Side-Rendering-Engine mit voller Quarkus- und GraalVM-Native-Unterstützung. HTMX ermöglicht moderne Single-Page-App-Interaktivität ohne komplexen JavaScript-Build-Stack (Node/NPM).
  2. *Single Page Application (SPA, z. B. React / Vue / Angular / Svelte) mit Quarkus REST API*: Hohe Flexibilität und clientseitige Entkopplung, erfordert jedoch zusätzliche Build-Pipelines, Tooling (Node, npm/vite), separates State-Management und doppelten DTO-Synchronisationsaufwand.
  3. *Vaadin Flow (Java-basierte Komponenten)*: Reine Java-Entwicklung für UI-Komponenten, jedoch höherer Speicherverbrauch und sessionbasierter Server-State, was der Cloud-Native-/Stateless-Ausrichtung von Quarkus entgegensteht.
* **Entscheidung**:
  Einsatz von **Quarkus Qute in Kombination mit HTMX und einem schlanken CSS-Framework (z. B. Bootstrap 5 oder TailwindCSS)** als primäre UI-Technologie.
* **Konsequenzen / Nutzen**:
  - **Zero-Node-Overhead**: Kein separater Frontend-Build-Prozess notwendig; Standard-Maven-Workflow (`./mvnw quarkus:dev`) genügt für Frontend und Backend.
  - **Typsicherheit**: Qute validiert Templates zur Compile-Zeit (`@CheckedTemplate`), wodurch Template-Laufzeitfehler vermieden werden.
  - **Performance & Footprint**: Perfekte Unterstützung für Native-Image-Kompilierung mit minimalen Startzeiten und geringem RAM-Verbrauch.
  - **Schichtenarchitektur**: UI-Controller/Ressourcen nutzen Application Services oder Aggregate direkt über Adapter/View-Models, ohne die Domäne zu verunreinigen.

---

## 10. Qualitätsanforderungen

*Konkrete Szenarien zur Bewertung der Qualitätsziele aus Kapitel 1.2 sowie aktuelle Qualitätsmetriken.*

### 10.1 Aktuelle Testabdeckung & Metriken

Die Testabdeckung wird automatisiert über JaCoCo bei jedem Build (`./mvnw test`) ermittelt:

- **Gesamtergebnis Tests**: 112 Tests (100 % erfolgreich, 0 Fehler, 0 Fehlschläge)
- **Zeilenabdeckung (Line Coverage)**: **97,44 %** (570 von 585 Zeilen abgedeckt)
- **Instruktionsabdeckung (Instruction Coverage)**: **97,32 %** (2.471 von 2.539 Instruktionen abgedeckt)
- **Methodenabdeckung (Method Coverage)**: **96,88 %** (217 von 224 Methoden abgedeckt)
- **Zweigabdeckung (Branch Coverage)**: **78,40 %** (167 von 213 Branches abgedeckt)

### 10.2 Qualitätsszenarien

| ID  | Qualitätsmerkmal | Szenario                              | Erwartete Reaktion                        |
|-----|------------------|---------------------------------------|-------------------------------------------|
| Q-1 | Wartbarkeit      | Verletzung von DDD-Regeln im Code     | Build schlägt in `ArchitectureTest` fehl. |
| Q-2 | Robustheit       | Wehr-Erstellung mit ungültigen Werten | Saubere Fehlerbehandlung / Validierung.   |
| Q-3 | Testabdeckung    | Ausführung der Testsuite (`./mvnw test`) | Automatische Erstellung des JaCoCo-Reports mit > 90 % Zeilenabdeckung. |

---

## 11. Risiken und technische Schulden

| Risiko / Technische Schuld                | Auswirkung          | Gegenmaßnahme                                  |
|-------------------------------------------|---------------------|------------------------------------------------|
| Zunehmende Komplexität der Domäne         | Unübersichtlichkeit | Strikte Aggregatgrenzen und ArchUnit-Prüfungen |
| Mangelnde Testabdeckung bei Erweiterungen | Regressionen        | Hohe Testabdeckung für jede Domänenänderung    |

---

## 12. Glossar

| Begriff            | Definition                                                                                                           |
|--------------------|----------------------------------------------------------------------------------------------------------------------|
| **Wehr**           | Aggregate Root, repräsentiert eine Feuerwehr-Einheit mit Name, Gründungsdatum, Gebäuden, Fahrzeug-IDs und Kameraden.  |
| **WehrFactory**    | GoF Factory (`@Factory`) zur Erzeugung, Rekonstruktion und Standardinitialisierung von `Wehr`-Aggregaten.           |
| **Einsatzfahrzeug**| Aggregate Root, repräsentiert ein Feuerwehr-Fahrzeug mit ID, Bezeichnung, Kennung und DIN-14530-Attributen.         |
| **Gebäude**        | Entity innerhalb des Wehr-Aggregats zur Verwaltung von Liegenschaften.                                               |
| **Kamerad**        | Entity innerhalb des Wehr-Aggregats zur Verwaltung von Feuerwehrmitgliedern.                                         |
| **Abteilung**      | Entity innerhalb des Wehr-Aggregats zur Verwaltung von Einsatzabteilungen (Jugend, Einsatz, Ehren).                  |
| **Fahrzeugtyp**    | Enum der genormten Feuerwehrfahrzeugtypen nach DIN 14530 (z. B. KLF, TSF, TSF-W, MLF, LF 10, HLF 10, LF 20, TLF).   |
| **Adresse**        | Value Object (Java Record) bestehend aus Straße, Hausnummer, PLZ und Ort.                                            |
| **Abmessungen**    | Value Object (Java Record) für maximale Fahrzeugabmessungen (Länge × Breite × Höhe in Metern).                      |
| **jMolecules**     | Bibliothek zur expliziten Annotation von DDD-Konzepten im Quellcode.                                                 |
| **ArchUnit**       | Testwerkzeug zur automatisierten Absicherung von Architektur- und Designregeln.                                      |
