# Spezifikation: Anwendungsfälle Aggregate Root `Einsatzfahrzeug`

Dieses Dokument spezifiziert die fachlichen Anwendungsfälle (Use Cases), Invarianten und
Akzeptanzkriterien für das Aggregate Root `Einsatzfahrzeug` im System **Ortswehrführer**.

# 1. Übersicht der Domänenentität

Das Aggregat `Einsatzfahrzeug` bildet ein weiteres zentrales Aggregate Root für eine
Feuerwehreinheit (z. B. Ortswehr
oder Freiwillige Feuerwehr). Es verwaltet den Lebenszyklus des Fahrzeugs sowie deren zugeordnete
Entitäten (`Fahrtenbuch`, `Wartung`) und dem Werteobjekt FahrzeugTyp.

### Fachliche Attribute

- **ID (`id`)**: Eindeutiger fachlicher/technischer Identifikator (UUID-Format).
- **Name (`bezeichnung`)**: Vollständige Bezeichnung des Fahrzeugs (z. B. "TSF").
- **Kennung (`kennung`)**: Vollständige Kennung des Fahrzeugs (z. B. "HVL 5/47/2").
- **Kennung (`kennung`)**: Vollständige Kennung des Fahrzeugs (z. B. "HVL 5/47/2").
- **FahrzeugTyp (`fahrzeugtyp`)**: Enum Fahrzeugtyp (`Enum`) z.B. TSF,TLF2000,HLF10,LF1000.

---