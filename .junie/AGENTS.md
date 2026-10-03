# Richtlinien für Ortswehrführer

In dieser Datei können Richtlinien und Konventionen für die Codeerstellung und Architektur im
Projekt definiert werden.

## Architektur & Design

- **Domain-Driven Design (DDD)**: Domänenmodelle werden mit jMolecules annotiert (z. B.
  `@AggregateRoot`, `@Identity`, `@Entity`, `@ValueObject`).
- **Value Objects**: Value Objects werden ausnahmslos als Java `record` implementiert (z. B.
  `@ValueObject public record Adresse(...) {}`).
- **Architekturregeln**: ArchUnit-Tests in `ArchitectureTest` stellen die Einhaltung der Regeln
  sicher.
- **CleanCode**: Code sollte lesbar, wartbar und effizient sein.
- ** Die Architektur des Projektes ist Clean Architecture **

## Code-Stil & Java-Standards

- **Java-Version**: Java 21
- **Framework**: Quarkus
- **Namenskonventionen**: Deutsche Fachbegriffe in der Domäne (z. B. `Wehr`, `gruenden`), technische
  Komponenten nach Standard-Java-Konventionen.

## Testing & Qualitätssicherung

- **Unit-Tests**: JUnit 5 für Domänenlogik und Services.
- **Architektur-Tests**: ArchUnit / jMolecules-ArchUnit für Konsistenzprüfungen.
- **Testausführung**: Tests werden über `./mvnw test` ausgeführt.
