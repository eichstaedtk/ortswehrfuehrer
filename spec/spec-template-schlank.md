# Spezifikation: <Feature-Name>

Status: Entwurf | Autor: <Name> | Datum: <JJJJ-MM-TT>

## 1. Ziel
<Ein bis drei Sätze: Was soll erreicht werden und warum?>

**Nicht-Ziele:** <Was ausdrücklich nicht gemacht wird>

## 2. Anforderungen
- FR-1: Das System **muss** ...
- FR-2: Das System **soll** ...
- NFR-1: <z. B. Performance, Sicherheit>

## 3. Akzeptanzkriterien
- **AC-1 (FR-1):** Gegeben <Ausgangslage>, wenn <Aktion>, dann <Ergebnis>.
- **AC-2 (FR-2):** Gegeben ..., wenn ..., dann ...
- **Randfälle:** <leere Eingabe, Fehler, Dienst nicht erreichbar, ...>

## 4. Aufgaben
| ID | Aufgabe | Bezug | Bereich (Dateien/Module) | Abhängig von |
|---|---|---|---|---|
| T-1 | <Aufgabe> | FR-1, AC-1 | `src/a/` | - |
| T-2 | <Aufgabe> | FR-2, AC-2 | `src/b/` | T-1 |

Parallel möglich: <z. B. T-1 und T-3>

## 5. Regeln für den Agenten
- Nur Dateien im Bereich der Aufgabe ändern.
- Nicht von dieser Spezifikation abweichen. Bei Lücken nachfragen oder Annahme dokumentieren.
- Tests zu den betroffenen Akzeptanzkriterien schreiben, Build und Tests ausführen.
- Keine neuen Abhängigkeiten ohne Begründung.
- Am Ende melden: Änderungen, geänderte Dateien, Testergebnis, offene Punkte.
