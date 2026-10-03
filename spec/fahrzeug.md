# Spezifikation: Anwendungsfälle Aggregate Root `Einsatzfahrzeug` & `Fahrzeugtyp` (DIN 14530)

Dieses Dokument spezifiziert die fachlichen Anwendungsfälle (Use Cases), Invarianten und
Akzeptanzkriterien für das Aggregate Root `Einsatzfahrzeug`, das Werteobjekt `Abmessungen` und den
`Fahrzeugtyp` nach **DIN 14530** im System **Ortswehrführer**.

# 1. Übersicht der Domänenentität

Das Aggregat `Einsatzfahrzeug` bildet ein zentrales Aggregate Root für eine Feuerwehreinheit (z. B. Ortswehr
oder Freiwillige Feuerwehr). Es verwaltet den Lebenszyklus des Fahrzeugs, dessen Typ (`Fahrzeugtyp`) sowie
dessen spezifische und genormte Kenndaten nach der Normenreihe DIN 14530.

### Fachliche Attribute

- **ID (`id`)**: Eindeutiger fachlicher/technischer Identifikator (UUID-Format).
- **Bezeichnung (`bezeichnung`)**: Vollständige Bezeichnung des Fahrzeugs (z. B. "Tragkraftspritzenfahrzeug").
- **Kennung (`kennung`)**: Funkrufkennung des Fahrzeugs (z. B. "Florian Havelland 5/47/2").
- **Fahrzeugtyp (`fahrzeugtyp`)**: Enum `Fahrzeugtyp` mit den genormten Typen nach DIN 14530 (z. B. KLF, TSF, TSF-W, MLF, LF 10, HLF 10, LF 20, HLF 20, LF 20 KatS, TLF 2000, TLF 3000, TLF 4000).
- **DIN-Norm (`dinNorm` / `din14530`)**: Zugehöriger Teil der Normenreihe (z. B. "Teil 16" bzw. "DIN 14530 Teil 16").
- **Masseklasse (`masseklasse`)**: Einstufung nach FNFW (z. B. "L I", "L II", "M II", "M III").
- **Zulässige Gesamtmasse (`zulassigeGesamtmasse`)**: Massebereich (z. B. "3,0–4,75 t", "9,0–14,0 t").
- **Besatzung (`besatzung`)**: Taktische Besatzungsstärke (z. B. "Staffel", "Gruppe", "Trupp").
- **Löschwasser (`loeschwasser`)**: Wassertankvolumen (z. B. "500 l", "1.000 l", "1.200 l", "2.000 l", "kein Tank").
- **Schaummittel (`schaummittel`)**: Vorrat an Schaummittel (z. B. "120 l (6 × 20 l)", "mind. 500 l (fest eingebaut)", "keines").
- **Pumpe (`pumpe`)**: Feuerlöschpumpe (z. B. "PFPN 10-1000", "FPN 10-1000", "FPN 10-2000").
- **Abmessungen (`abmessungen`)**: Maximalabmessungen Länge × Breite × Höhe in Metern (`Abmessungen`).
- **Fahrgestell-Kategorie (`fahrgestellKategorie`)**: Kategorie nach DIN EN 1846 (z. B. "Straße (Kat. 1)", "Gelände (Kat. 2)").
- **Antrieb (`antrieb`)**: Antriebsart (z. B. "Straße", "Allrad", "Straße oder Allrad").
- **Führerschein (`fuehrerschein` / `fuehrerscheinklasse`)**: Erforderliche Fahrerlaubnisklasse (z. B. "C1", "C").

---

# 2. Fachliche Anwendungsfälle

## UC-F01: Einsatzfahrzeug nach DIN 14530 anlegen
Ein Einsatzfahrzeug wird unter Angabe von Bezeichnung, Funkrufkennung und genormtem Fahrzeugtyp erzeugt.
Standardwerte der DIN 14530 werden automatisch vom Fahrzeugtyp bereitgestellt, können aber bei individueller Fahrzeugbeschaffung fahrzeugspezifisch überschrieben werden.
