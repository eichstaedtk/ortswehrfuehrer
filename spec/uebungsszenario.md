# UC-001: Einsatzszenario für Übungsdienst generieren

| Feld             | Inhalt         |
|------------------|----------------|
| Version / Status | 0.1 / Entwurf  |
| Owner            | Ortswehrführer |
| Datum            | 05.10.2026     |

## 1. Ziel und Kontext

- **Ziel:** Der Ortswehrführer erhält schnell ein realistisches, auf die verfügbaren Fahrzeuge und
  Kräfte zugeschnittenes Übungsszenario für den Übungsdienst.
- **Problemstellung:** Die Szenarioplanung ist zeitaufwendig und wird oft nicht an Fahrzeuge,
  Besatzung und Qualifikationen angepasst.
- **Erfolgskriterien / KPIs:**
    - Szenario in unter 5 Minuten erstellt
    - Szenario ist mit gewählten Fahrzeugen und Kräften vollständig durchführbar
    - Ortswehrführer übernimmt mind. 80 % der Vorschläge ohne größere Änderung

## 2. Akteure

- **Primary Actor:** Ortswehrführer (Übungsleiter)
- **Weitere Stakeholder / Systeme:** Fahrzeugverwaltung, Personalverwaltung,
  Wehrleiter/Gemeindewehrführer (Einsicht), Übungsteilnehmer (Empfänger des Szenarios)

## 3. Scope

- **In Scope:**
    - Auswahl der Übungsart: **Löschübung** oder **Technische Hilfeleistung (TH)**
    - Auswahl von Fahrzeugen und Mannschaft
    - Generierung eines Szenarios (Lage, Ablauf, Ziele, Sicherheit)
    - Anpassen und Neugenerieren einzelner Abschnitte
    - Export als Übungsunterlage
- **Out of Scope:**
    - Echteinsatz-Unterstützung und Alarmierung
    - Dienstplanung und Anwesenheitserfassung
    - Anleitung zum Herstellen realer Gefahrenlagen (z. B. Brandlegung)

## 4. Vorbedingungen und Trigger

- **Vorbedingungen:**
    - Ortswehrführer ist angemeldet und hat die Rolle "Ortswehrführer"
    - Fahrzeug und Kameraden sind angelegt
- **Trigger:** Ortswehrführer wählt "Neues Übungsszenario"

## 5. Hauptszenario (Happy Path)

1. Der Ortswehrführer startet "Neues Übungsszenario".
2. Das System zeigt die Auswahl: **Löschangriff** oder **Technische Hilfeleistung**. Der
   Ortswehrführer wählt eine Art.
3. Das System zeigt die verfügbaren Fahrzeuge. Der Ortswehrführer wählt ein oder mehrere Fahrzeuge.
4. Das System zeigt je Fahrzeug die Sitzplätze und Funktionen (z. B. Gruppenführer, Maschinist,
   Angriffstrupp, Wassertrupp, Schlauchtrupp).
5. Der Ortswehrführer weist Kameraden den Funktionen zu. Das System zeigt Qualifikationen an
   (Atemschutz, Maschinist, TH-Ausbildung, Truppführer).
6. Das System prüft die Auswahl (siehe 8.3) und meldet Lücken oder Warnungen.
7. Optional gibt der Ortswehrführer Rahmenwerte an (Dauer, Schwierigkeit, Tag/Nacht,
   Übungsobjekt/Ort, Lernziele).
8. Das System erzeugt das Szenario mit Ausgangslage, Übungszielen, Aufgabenverteilung,
   Lageentwicklung, Bewertungspunkten und Sicherheitshinweisen.
9. Der Ortswehrführer prüft das Szenario und passt es an (Abschnitt neu generieren, manuell ändern).
10. Der Ortswehrführer gibt das Szenario frei und exportiert es als PDF-Datei.

## 6. Alternative Abläufe und Fehlerfälle

- **3a.** Kein Fahrzeug einsatzbereit → Hinweis, Abbruch oder Auswahl nicht einsatzbereiter
  Fahrzeuge als "fiktiv" markieren.
- **5a.** Nicht genug Personal für die Fahrzeugbesatzung → Agent schlägt reduzierte Besatzung (z. B.
  Staffel statt Gruppe) oder anderes Fahrzeug vor.
- **6a.** Fehlende Qualifikation (z. B. kein Atemschutzgeräteträger für Innenangriff) → Agent warnt
  und passt Szenario an (z. B. Außenangriff, Wasserförderung).
- **6b.** Fahrzeug passt nicht zur Übungsart (z. B. kein Hydraulikgerät bei TH) → Warnung mit
  Alternativen.
- **8a.** System ist unsicher oder Eingaben widersprüchlich → Rückfrage an Ortswehrführer.
- **8b.** Generierung schlägt fehl → Hinweis und erneuter Versuch, bereits gewählte Daten bleiben
  erhalten.
- **9a.** Ortswehrführer verwirft das Szenario → Neugenerierung mit geänderten Rahmenwerten.

## 7. Nachbedingungen

- **Erfolg:** Freigegebenes, gespeichertes Szenario mit Version, Fahrzeug- und Personalzuordnung.
- **Fehlschlag:** Es wird kein Szenario gespeichert. Eingaben bleiben als Entwurf erhalten.

## 8. Agent-spezifische Anforderungen

### 8.1 Tools und Integrationen

| Tool                | Zweck                                       | Berechtigung |
|---------------------|---------------------------------------------|--------------|
| Fahrzeugkatalog     | Fahrzeugtypen, Beladung, Sitzplätze, Status | read         |
| Personalverwaltung  | Kameraden, Qualifikationen, Verfügbarkeit   | read         |
| Szenario-Bibliothek | Vorlagen und frühere Übungen                | read/write   |
| Export              | PDF/Druck der Übungsunterlage               | write        |

### 8.2 Wissensquellen / Daten

- Feuerwehrdienstvorschriften (FwDV 1, 3, 7), Unfallverhütungsvorschriften
- Landesspezifische Vorgaben (Brandenburg), z. B. Ausbildungs- und Übungsvorgaben
- Eigene Fahrzeugbeladung und Gerätelisten

### 8.3 Guardrails und Verbote

**Prüfregeln (hart):**

- Löschübung: mind. Wasserentnahme/-förderung und Angriffstrupp-Funktion besetzt
- Innenangriff nur mit mind. zwei Atemschutzgeräteträgern plus Sicherheitstrupp
- TH-Übung: Fahrzeug mit passender Beladung (z. B. hydraulischer Rettungssatz, Sicherung)
  erforderlich
- Mind. ein Gruppen- oder Truppführer mit entsprechender Qualifikation

**Verbote:**

- Szenarien enthalten keine Anleitung zu realen Brandlegungen, Brandbeschleunigern, Pyrotechnik oder
  gefährlichen Stoffen
- Alle Ausgaben sind klar als "ÜBUNG" gekennzeichnet
- Das System erfindet keine Fahrzeuge, Geräte oder Qualifikationen, die nicht im Katalog stehen
- Das System bewertet keine einzelnen Personen

### 8.4 Human-in-the-Loop / Freigabepunkte

- Die Freigabe des Szenarios erfolgt ausschließlich durch den Ortswehrführer.
- Sicherheitsrelevante Abschnitte werden vor der Freigabe hervorgehoben.

### 8.5 Eskalationsregeln

- Bei Konflikten zwischen Qualifikation und Szenario Rückfrage statt stiller Annahme.
- Bei Unsicherheit über Beladung/Fähigkeit eines Fahrzeugs Hinweis "bitte prüfen".

## 9. Nicht-funktionale Anforderungen

- **Latenz:** Szenario in unter 60 Sekunden
- **Nutzbarkeit:** Auswahl per Klick (mobil und Desktop), da Nutzung oft kurz vor dem Übungsdienst
- **Datenschutz (DSGVO):** Personendaten nur zweckgebunden, Minimierung der Daten, die an das
  Sprachmodell gehen (z. B. Funktion und Qualifikation statt Klarnamen)
- **Nachvollziehbarkeit:** Protokoll, welche Eingaben und Prüfregeln zum Szenario führten
- **Sprache:** Deutsch, feuerwehrübliche Fachbegriffe

## 10. Evaluation

- **Beispiel-Dialoge (Golden Set):**
    1. Löschübung, TSF-W, Staffel (6 Kräfte), wenig Atemschutz → Außenangriff/Wasserförderung
    2. TH-Übung, HLF, Gruppe (9 Kräfte) → Verkehrsunfall mit eingeklemmter Person
    3. Zwei Fahrzeuge, Tagesübung, Schwierigkeit "hoch"
    4. Zu wenig Personal → Agent warnt und schlägt Alternativen vor
- **Metriken:** Durchführbarkeit, Regelverstöße (Ziel: 0), Änderungsquote, Zufriedenheit des
  Ortswehrführers
- **Abnahmekriterien:** Alle Golden-Set-Fälle bestanden, keine Verstöße gegen die Prüfregeln,
  Fachprüfung durch erfahrenen Gruppenführer/Ausbilder

## 11. Risiken und offene Fragen

| Risiko                                   | Wahrsch. | Auswirkung | Maßnahme                                                       |
|------------------------------------------|----------|------------|----------------------------------------------------------------|
| Unrealistisches oder unsicheres Szenario | mittel   | hoch       | Prüfregeln, Freigabe durch Ortswehrführer, Sicherheitshinweise |
| Veraltete Fahrzeug-/Personaldaten        | mittel   | mittel     | Datenstand anzeigen, Hinweis bei Warnungen                     |
| Datenschutz bei Personendaten            | niedrig  | hoch       | Pseudonymisierung, Verarbeitungsvereinbarung                   |

**Offene Fragen:**
