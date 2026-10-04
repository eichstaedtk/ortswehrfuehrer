# Spezifikation: Anwendungsfälle Aggregate Root `Wehr`

Dieses Dokument spezifiziert die fachlichen Anwendungsfälle (Use Cases), Invarianten und
Akzeptanzkriterien für das Aggregate Root `Wehr` im System **Ortswehrführer**.

---

## 1. Übersicht der Domänenentität

Das Aggregat `Wehr` bildet das zentrale Aggregate Root für eine Feuerwehreinheit (z. B. Ortswehr
oder Freiwillige Feuerwehr). Es verwaltet den Lebenszyklus der Wehr sowie deren zugeordnete
Entitäten (`Gebaeude`, `Kamerad`, `Abteilung`) und Wertobjekte (`Adresse`).

### Fachliche Attribute

- **ID (`id`)**: Eindeutiger fachlicher/technischer Identifikator (UUID-Format).
- **Name (`name`)**: Vollständige Bezeichnung der Wehr (z. B. "Freiwillige Feuerwehr Göttlin").
- **Gründungsdatum (`gruendungsdatum`)**: Historisches Datum der Gründung (`LocalDate`).
- **Gebäude (`gebaeude`)**: Liste der zugeordneten Liegenschaften/Gerätehäuser (`List<Gebaeude>`).
- **Fahrzeuge (`fahrzeugIds` / `getFahrzeugIds()`)**: Menge der zugeordneten Einsatzfahrzeug-IDs (`Set<String>`) nach dem DDD-Prinzip *Reference by ID*.
- **Kameraden (`getKameraden()`)**: Dynamisch aggregierte Menge aller Mitglieder/Kameraden aus den
  Einsatzabteilungen (`Set<Kamerad>`).
- **Jugendabteilung (`jugendabteilung`)**: Einsatzabteilung für Kameraden bis 16 Jahre
  (`Abteilung`).
- **Einsatzabteilung (`einsatzabteilung`)**: Einsatzabteilung für Kameraden im Alter von 17 bis 65
  Jahren (`Abteilung`).
- **Alters- und Ehrenabteilung (`altersUndEhrenabteilung`)**: Einsatzabteilung für Kameraden über 65
  Jahre (`Abteilung`).

---

## 2. Anwendungsfälle (Use Cases)

### UC-01: Wehr gründen

- **Ziel / Nutzen**: Eine neue Feuerwehreinheit mit eindeutiger Identifikation, Namen und
  historischem Gründungsdatum offiziell im System anlegen.
- **Akteur**: Ortswehrführer / Verbandsverwaltung.
- **Vorbedingung**: Eine Instanz der Klasse `Wehr` wurde initialisiert.
- **Eingabeparameter**:
    - `name` (String): Bezeichnung der Feuerwehr.
    - `gruendungsdatum` (LocalDate): Tag der offiziellen Gründung.
- **Nachbedingung**:
    - Die Wehr besitzt eine gültige, zufallsgenerierte UUID als Identifikator.
    - Name und Gründungsdatum sind persistent gesetzt.
    - Die Methode gibt die modifizierte `Wehr`-Instanz zurück (Fluent API).

#### Szenario: Erfolgreiche Gründung einer Wehr

```gherkin
Gegeben sei eine neu initialisierte Wehr
Wenn die Wehr mit dem Namen "Freiwillige Feuerwehr Göttlin" und dem Gründungsdatum "01.05.1924" gegründet wird
Dann hat die Wehr eine gültige UUID als ID
Und der Name der Wehr lautet "Freiwillige Feuerwehr Göttlin"
Und das Gründungsdatum ist der 01.05.1924
Und als Rückgabewert wird die gegründete Wehr-Instanz geliefert
```

---

### UC-02: Gebäude zur Wehr hinzufügen

- **Ziel / Nutzen**: Liegenschaften (z. B. Gerätehaus, Bootshaus) mit vollständiger Anschrift der
  zuständigen Wehr zuordnen.
- **Akteur**: Ortswehrführer / Liegenschaftsverwaltung.
- **Vorbedingung**: Das Aggregate Root `Wehr` existiert. Eine `Gebaeude`-Entität mit gültiger
  `Adresse` liegt vor.
- **Eingabeparameter**:
    - `gebaeude` (Gebaeude): Das hinzuzufügende Gebäude mit Bezeichnung und Adresse
      (`record Adresse`).
- **Nachbedingung**:
    - Das Gebäude ist in der Gebäudeliste der Wehr enthalten.
    - Die Methode gibt die aktualisierte `Wehr`-Instanz zurück (Fluent API).

#### Szenario: Gebäude mit Adresse erfolgreich zuweisen

```gherkin
Gegeben sei eine Wehr
Und ein Gebäude "Gerätehaus Göttlin" mit der Adresse "Hauptstraße 12a, 14712 Göttlin"
Wenn das Gebäude zur Wehr hinzugefügt wird
Dann enthält die Gebäudeliste der Wehr genau 1 Gebäude
Und das Gebäude besitzt die Bezeichnung "Gerätehaus Göttlin"
Und die Adresse des Gebäudes stimmt mit den Angaben überein
Und als Rückgabewert wird die Wehr-Instanz geliefert
```

---

### UC-03: Kamerad zur Wehr hinzufügen

- **Ziel / Nutzen**: Ein aktives Mitglied (Kamerad) mit persönlicher Kennung, Namen, Geburtsdatum
  und Anschrift der Wehr zuordnen.
- **Akteur**: Ortswehrführer / Personalverwalter.
- **Vorbedingung**: Das Aggregate Root `Wehr` existiert. Eine `Kamerad`-Entität liegt vor.
- **Eingabeparameter**:
    - `kamerad` (Kamerad): Der hinzuzufügende Kamerad mit Vorname, Nachname, Geburtsdatum,
      Telefonnummer, E-Mail-Adresse (`LocalDate`) und Adresse (`record Adresse`) (inkl.
      automatischer ID-Generierung).
- **Nachbedingung**:
    - Der Kamerad ist in der Mitgliederliste der Wehr erfasst.
    - Die Methode gibt die aktualisierte `Wehr`-Instanz zurück (Fluent API).

#### Szenario: Kameraden erfolgreich zur Wehr hinzufügen

```gherkin
Gegeben sei eine Wehr
Und ein Kamerad "Max Mustermann" mit dem Geburtsdatum "20.05.1990" und der Adresse "Dorfstraße 12, 14712 Göttlin, Telefonummer01738884932, E-Mail-Adresse:max.mustermann@example.com"
Wenn der Kamerad zur Wehr hinzugefügt wird
Dann enthält die Mitgliederliste der Wehr genau 1 Mitglied
Und der Kamerad besitzt den Vornamen "Max" und den Nachnamen "Mustermann"
Und das Geburtsdatum des Kameraden ist der 20.05.1990
Und die Adresse des Kameraden stimmt mit den Angaben überein
Und die Telefonummer des Kameraden stimmt mit den Angaben überein
Und die E-Mail-Adresse des Kameraden stimmt mit den Angaben überein
Und als Rückgabewert wird die Wehr-Instanz geliefert
```

---

### UC-04: Initialzustand und Aggregatintegrität

- **Ziel / Nutzen**: Sicherstellen, dass ein neu erzeugtes Aggregat deterministische Nullwerte für
  Stammdaten aufweist, aber initialisierte leere Kollektionen bereitstellt, um
  `NullPointerException`s zu verhindern.
- **Nachbedingung**:
    - `id`, `name` und `gruendungsdatum` sind `null`.
    - `gebaeude` ist eine leere, nicht-null Liste und `getKameraden()` liefert eine leere Menge.

#### Szenario: Aggregat im Initialzustand prüfen

```gherkin
Gegeben sei eine neu instanziierte Wehr
Dann ist die ID nicht gesetzt (null)
Und der Name ist nicht gesetzt (null)
Und das Gründungsdatum ist nicht gesetzt (null)
Und die Gebäudeliste ist nicht null und leer
Und die aggregierte Kameradenmenge ist nicht null und leer
```

### UC-05: Einsatzabteilungen der Wehr

- **Ziel / Nutzen**: Jeder Kamerad muss eine Einsatzabteilung zugeordnet werden. Dabei gibt es
  folgende drei Einsatzabteilungen: Jugendabteilung, Einsatzabteilung, Alters- und Ehrenabteilung
- **Nachbedingung**:
    - Jeder Kamerad ist in einer Einsatzabteilung enthalten
    - Die Wehr hat genau eine Jugendabteilung
    - Die Wehr hat genau eine Einsatzabteilung
    - Die Wehr hat genau eine Alters- und Ehrenabteilung

#### Szenario: Aggregat prüfen

```gherkin
Gegeben sei eine neu instanziierte Wehr
Die Wehr hat genau eine Jugendabteilung
Die Wehr hat genau eine Einsatzabteilung
Die Wehr hat genau eine Alters- und Ehrenabteilung
Wenn ein Kamerad zur Wehr hinzugefügt wird
Dann ist der Kamerad in einer der Abteilungen enthalten.  Ein Kamerad im Alter bis 16 ist in der Jugendwehr, ein Kamerad im Alter von 17 bis 65 ist in der Einsatzabteilung, ein Kamerad im Alter über 65 ist in der Alters- und Ehrenabteilung.
```

### UC-06: Fahrzeuge zur Wehr hinzufügen

- **Ziel / Nutzen**: Eine Wehr hat die Möglichkeit Fahrzeuge über deren Identifikator hinzuzufügen.
- **Nachbedingung**:
    - Die Wehr verwaltet die eindeutigen IDs der zugeordneten Einsatzfahrzeuge (`Set<String> fahrzeugIds`).

#### Szenario: Aggregat prüfen

```gherkin
Gegeben sei eine neu instanziierte Wehr
Die Wehr hat genau eine Jugendabteilung
Die Wehr hat genau eine Einsatzabteilung
Die Wehr hat genau eine Alters- und Ehrenabteilung
Wenn ein Kamerad zur Wehr hinzugefügt wird
Dann ist der Kamerad in einer der Abteilungen enthalten.  
Ein Kamerad im Alter bis 16 ist in der Jugendwehr, ein Kamerad im Alter von 17 bis 65 ist in der Einsatzabteilung, ein Kamerad im Alter über 65 ist in der Alters- und Ehrenabteilung.
Eine Wehr kann Fahrzeuge über deren ID oder Einsatzfahrzeug-Instanz zuordnen.
```

---

### UC-07: Erzeugung von Wehr-Instanzen über GoF Factory

- **Ziel / Nutzen**: Kapselung der komplexen Erzeugungs-, Rekonstruktions- und Initialisierungslogik
  für das Aggregate Root `Wehr` über ein GoF Factory Pattern (`WehrFactory`).
- **Akteur**: Anwendungslogik / Repositories / Test-Suites.
- **Nachbedingung**:
    - `WehrFactory` bietet typsichere Methoden zur Neuerzeugung (`erzeugeNeueWehr()`, `erzeugeWehr(name)`, `erzeugeWehr(name, gruendungsdatum)`).
    - `WehrFactory` bietet Rekonstruktionsmethoden (`rekonstruiereWehr(id, name, gruendungsdatum)`).
    - `WehrFactory` ermöglicht die vollständige Assemblierung mit Gebäuden, Fahrzeug-IDs und Kameraden.

#### Szenario: Erzeugung einer Wehr über die Factory

```gherkin
Gegeben sei eine WehrFactory
Wenn eine Wehr mit dem Namen "Freiwillige Feuerwehr Göttlin" und dem Gründungsdatum "01.05.1924" über die Factory erzeugt wird
Dann besitzt die erzeugte Wehr eine gültige UUID als ID
Und der Name lautet "Freiwillige Feuerwehr Göttlin"
Und das Gründungsdatum ist der 01.05.1924
Und alle Abteilungen und Kollektionen sind initialisiert
```

## UC-82: Kamerad loeschen

Es soll eine Möglichkeit geschaffen werden ein Kameraden zu entfernen.

**Nachbedingung**:

- Die Wehr verfügt über keine Kameraden mehr.

```gherkin
Gegeben sei eine WehrFactory
Wenn eine Wehr mit dem Namen "Freiwillige Feuerwehr Göttlin" und dem Gründungsdatum "01.05.1924" über die Factory erzeugt wird
Dann besitzt die erzeugte Wehr eine gültige UUID als ID
Und der Name lautet "Freiwillige Feuerwehr Göttlin"
Und das Gründungsdatum ist der 01.05.1924
Und alle Abteilungen und Kollektionen sind initialisiert
Die verfügt über ein Kamerad besitzt den Vornamen "Max" und den Nachnamen "Mustermann. 
Es wird die Funktion Kamerad entfernen aufgerufen. 
```

---

---

## 3. Architektur- und Qualitätsregeln

1. **Aggregatsgrenzen**: Alle Modifikationen an Gebäuden oder Kameraden erfolgen über die Methoden
   des Aggregate Roots `Wehr`.
2. **Aggregate-Referenzierung**: Separate Aggregate Roots (`Wehr` und `Einsatzfahrzeug`) referenzieren
   sich gemäß DDD-Regeln ausschließlich über ihre Identität (`Reference by ID`), um Transaktionsgrenzen
   zu wahren.
3. **Immutability von Wertobjekten**: Adressen und Abmessungen werden ausschließlich als unveränderliche Java
   `record`s modelliert.
4. **Fluent Interface**: Zustandsändernde Methoden des Aggregats geben zur Verbesserung der
   Aufrufbarkeit stets die Aggregat-Instanz `this` zurück.
5. **Validierung**: Alle spezifizierten Szenarien sind durch automatisierte Unittests in `WehrTest`
   und Architekturprüfungen in `ArchitectureTest` verifiziert.
