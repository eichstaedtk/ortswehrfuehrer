# Spezifikation: Barrierefreiheit von Webseiten

Prüfgrundlage für Code-Reviews und Webseitenprüfungen

| | |
|---|---|
| Stand | 05.10.2026 |
| Zielstandard | WCAG 2.2, Konformitätsstufe AA |
| Rechtlicher Bezug (DE) | BITV 2.0 (öffentliche Stellen), BFSG (bestimmte private Anbieter), EN 301 549 |
| Geltungsbereich | Websites, Web-Apps und deren Inhalte (HTML, CSS, JavaScript, Medien, PDF-Downloads) |

> **Hinweis:** Diese Spezifikation ist eine technische Prüfhilfe und keine Rechtsberatung. Rechtliche Pflichten (Geltungsbereich, Fristen, Ausnahmen, Bußgelder) sind im Einzelfall mit dem Datenschutz-/Rechtsbeauftragten oder der zuständigen Marktüberwachung bzw. Überwachungsstelle zu klären.

---

## 1. Normativer Rahmen

### 1.1 Wie die Regelwerke zusammenhängen

| Ebene | Regelwerk | Rolle |
|---|---|---|
| Technischer Standard | **WCAG 2.2** (W3C, Oktober 2023) | Das "Wie": konkrete Erfolgskriterien (A, AA, AAA) |
| Europäische Norm | **EN 301 549** | Brücke zwischen Gesetz und WCAG |
| Gesetz / Verordnung | **BITV 2.0** | Bundes- und Landesbehörden, öffentliche Stellen |
| Gesetz / Verordnung | **BFSG** und BFSGV | Bestimmte Produkte und Dienstleistungen für Verbraucher (z. B. E-Commerce) |

### 1.2 Stand der EN 301 549

- Als harmonisierte Norm im EU-Amtsblatt zitiert ist weiterhin **EN 301 549 V3.2.1**. Sie beruht auf **WCAG 2.1 Level AA**.
- **EN 301 549 V4.1.1** wurde im September 2026 veröffentlicht und beruht auf **WCAG 2.2**. Sie ist nach den verfügbaren Quellen noch nicht im Amtsblatt zitiert und löst deshalb noch keine Konformitätsvermutung aus. Der genaue Zeitpunkt der Zitierung ist in den Quellen widersprüchlich angegeben (Oktober bis Dezember 2026) und muss beim Einsatz geprüft werden.
- Für Webseiten kommen in V4.1.1 sechs neue Kriterien der Stufen A und AA aus WCAG 2.2 hinzu (siehe Abschnitt 2.2).

### 1.3 Festlegung für diese Spezifikation

1. Alle Anforderungen **WCAG 2.2 A und AA** MÜSSEN erfüllt sein.
2. WCAG 2.2 AA schließt WCAG 2.1 AA ein. Damit sind sowohl die aktuell zitierte als auch die kommende Fassung der EN 301 549 abgedeckt.
3. Kriterien der Stufe AAA SIND NICHT verpflichtend, SOLLEN aber bei geringem Aufwand umgesetzt werden.

### 1.4 Hinweis zu WCAG 2.1 / 2.2: Kriterium 4.1.1 (Parsing)

In WCAG 2.2 ist 4.1.1 entfallen. Die EN 301 549 V3.2.1 enthält es noch. Daher SOLL das HTML weiterhin valide sein (eindeutige IDs, korrekt verschachtelte Elemente, keine doppelten Attribute).

### 1.5 Pflichten über die Technik hinaus (Deutschland)

| Pflicht | Betrifft | Prüfpunkt |
|---|---|---|
| Erklärung zur Barrierefreiheit | Öffentliche Stellen (BITV 2.0); Anbieter nach BFSG haben eigene Informationspflichten | Seite vorhanden, von jeder Seite erreichbar, aktuell |
| Feedback-Mechanismus | Öffentliche Stellen | Kontaktmöglichkeit für Barrieren vorhanden und selbst barrierefrei |
| Informationen in Leichter Sprache und Deutscher Gebärdensprache | Bundesbehörden (Vorgaben für Länder und Kommunen abweichend) | Auffindbar, wenn rechtlich gefordert |
| Schlichtungsverfahren | Öffentliche Stellen | Hinweis in der Erklärung vorhanden |

---

## 2. Anforderungen nach den vier Prinzipien (POUR)

Schreibweise: **MUSS** = verpflichtend, **SOLL** = Empfehlung mit begründeten Ausnahmen, **KANN** = optional.

### 2.1 Wahrnehmbar (Perceivable)

| ID | WCAG | Stufe | Anforderung | Prüfung im Code / Test |
|---|---|---|---|---|
| P-01 | 1.1.1 | A | Informative Bilder MÜSSEN eine Textalternative haben. Dekorative Bilder MÜSSEN leeres `alt=""` oder CSS-Hintergrund nutzen. | Jedes `<img>` hat `alt`. Alt-Text beschreibt Zweck, nicht "Bild von". Bei Icon-Buttons trägt der Button den Namen. |
| P-02 | 1.1.1 | A | Komplexe Grafiken (Diagramme) MÜSSEN eine ausführliche Alternative haben. | Tabelle, Text oder `aria-describedby`-Verweis neben der Grafik. |
| P-03 | 1.2.1–1.2.3 | A | Aufgezeichnete Audio-/Videoinhalte MÜSSEN Transkript, Untertitel und Audiodeskription bzw. Medienalternative haben. | `<track kind="captions">` oder Transkript vorhanden. Prüfung per Abspielen. |
| P-04 | 1.2.4, 1.2.5 | AA | Live-Untertitel und Audiodeskription für aufgezeichnete Videos MÜSSEN vorhanden sein, sofern Videoinhalte betroffen sind. | Redaktionelle Prüfung. |
| P-05 | 1.3.1 | A | Struktur und Beziehungen MÜSSEN semantisch ausgezeichnet sein (Überschriften, Listen, Tabellen, Formulare, Landmarks). | Siehe Abschnitt 3.1 bis 3.4. Keine reinen `div`/`span`-Layouts für Strukturen. |
| P-06 | 1.3.2 | A | Die Lesereihenfolge im DOM MUSS sinnvoll sein. | CSS aus, DOM-Reihenfolge lesen. Kein `order`/`position`, das die Bedeutung verändert. |
| P-07 | 1.3.3 | A | Anweisungen DÜRFEN NICHT ausschließlich auf Form, Farbe, Größe oder Position verweisen ("der rote Button rechts"). | Textprüfung. |
| P-08 | 1.3.4 | AA | Inhalte MÜSSEN in Hoch- und Querformat nutzbar sein. | Keine Sperre per CSS/JS (`orientation`-Lock) außer bei Notwendigkeit. |
| P-09 | 1.3.5 | AA | Eingabefelder für Personendaten MÜSSEN passende `autocomplete`-Werte haben. | `autocomplete="given-name"`, `email`, `postal-code` usw. |
| P-10 | 1.4.1 | A | Farbe DARF NICHT das einzige Mittel zur Informationsvermittlung sein. | Fehler, Links im Fließtext, Diagramme haben zusätzlich Text, Symbol oder Muster. |
| P-11 | 1.4.2 | A | Automatisch startende Audioinhalte (über 3 s) MÜSSEN stoppbar sein. | Kein Autoplay mit Ton ohne Steuerung. |
| P-12 | 1.4.3 | AA | Kontrast Text/Hintergrund MUSS mindestens **4,5:1** betragen (großer Text: **3:1**). | Kontrastprüfung aller Text-/Hintergrundpaare, auch Hover, Fokus, deaktivierte Zustände mit Informationsgehalt. |
| P-13 | 1.4.4 | AA | Text MUSS bis 200 % ohne Verlust von Inhalt und Funktion vergrößerbar sein. | Browser-Zoom 200 %. Schriftgrößen in `rem`/`em`, nicht in `px` fix. |
| P-14 | 1.4.5 | AA | Bilder von Text SIND zu vermeiden. | Kein Text als Grafik außer Logos. |
| P-15 | 1.4.10 | AA | Reflow: Inhalt MUSS bei 320 CSS-px Breite (400 % Zoom bei 1280 px) ohne zweidimensionales Scrollen nutzbar sein. | Responsive prüfen. Ausnahmen: Tabellen, Karten, Code. |
| P-16 | 1.4.11 | AA | Kontrast von UI-Komponenten und aussagekräftigen Grafiken MUSS mindestens **3:1** betragen. | Rahmen von Formularfeldern, Icons, Fokusindikatoren, Diagrammelemente. |
| P-17 | 1.4.12 | AA | Textabstände MÜSSEN anpassbar sein, ohne dass Inhalt abgeschnitten wird. | Bookmarklet "Text Spacing" testen. Keine feste Höhe bei Textcontainern. |
| P-18 | 1.4.13 | AA | Per Hover/Fokus eingeblendete Inhalte MÜSSEN schließbar (Esc), überfahrbar und dauerhaft sein. | Tooltips, Dropdowns manuell prüfen. |

### 2.2 Bedienbar (Operable)

| ID | WCAG | Stufe | Anforderung | Prüfung im Code / Test |
|---|---|---|---|---|
| O-01 | 2.1.1 | A | Alle Funktionen MÜSSEN per Tastatur bedienbar sein. | Komplette Seite nur mit Tab, Shift+Tab, Enter, Leertaste, Pfeiltasten, Esc durchlaufen. |
| O-02 | 2.1.2 | A | Es DARF KEINE Tastaturfalle geben. | Modale, Widgets und eingebettete Inhalte lassen sich per Tastatur wieder verlassen. |
| O-03 | 2.1.4 | A | Einzeltasten-Kurzbefehle MÜSSEN abschaltbar, änderbar oder nur bei Fokus aktiv sein. | Review von `keydown`-Handlern auf Einzelzeichen. |
| O-04 | 2.2.1 | A | Zeitlimits MÜSSEN abschaltbar, anpassbar oder verlängerbar sein. | Session-Timeouts: Warnung und Verlängerung. |
| O-05 | 2.2.2 | A | Bewegte, blinkende oder automatisch aktualisierte Inhalte (über 5 s) MÜSSEN pausierbar sein. | Karussells, Ticker, Animationen mit Pause-Steuerung. `prefers-reduced-motion` beachten. |
| O-06 | 2.3.1 | A | Keine Elemente, die mehr als dreimal pro Sekunde blitzen. | Medien- und Animationsprüfung. |
| O-07 | 2.4.1 | A | Ein Mechanismus zum Überspringen wiederkehrender Blöcke MUSS vorhanden sein. | "Zum Inhalt springen"-Link als erstes fokussierbares Element, Landmarks. |
| O-08 | 2.4.2 | A | Jede Seite MUSS einen aussagekräftigen, eindeutigen `<title>` haben. | Titel wechselt bei Seiten- und Routenwechsel (auch in SPAs). |
| O-09 | 2.4.3 | A | Fokusreihenfolge MUSS logisch sein. | Tab-Reihenfolge entspricht der visuellen Reihenfolge. Kein `tabindex` größer als 0. |
| O-10 | 2.4.4 | A | Linkzweck MUSS aus Linktext oder Kontext hervorgehen. | Keine Links mit nur "hier", "mehr" ohne Kontext (`aria-label` / `aria-describedby`). |
| O-11 | 2.4.5 | AA | Mindestens zwei Wege zu Seiten (z. B. Navigation und Suche oder Sitemap). | Strukturprüfung. |
| O-12 | 2.4.6 | AA | Überschriften und Beschriftungen MÜSSEN Thema oder Zweck beschreiben. | Redaktionelle Prüfung. |
| O-13 | 2.4.7 | AA | Der Tastaturfokus MUSS sichtbar sein. | Kein `outline: none` ohne gleichwertigen Ersatz. `:focus-visible` nutzen. |
| O-14 | 2.4.11 (neu in 2.2) | AA | Fokussierte Elemente DÜRFEN NICHT vollständig von Sticky-Headern, Cookie-Bannern o. Ä. verdeckt werden. | Mit Tab durch die Seite gehen, `scroll-padding-top` und `scroll-margin` prüfen. |
| O-15 | 2.5.1 | A | Mehrfinger- und Pfadgesten MÜSSEN eine Einfachbedienung als Alternative haben. | Karten, Slider, Zoom-Gesten. |
| O-16 | 2.5.2 | A | Aktionen SOLLEN erst beim Loslassen (`click`/`pointerup`) auslösen, abbrechbar sein. | Kein `mousedown`/`touchstart` für Aktionen. |
| O-17 | 2.5.3 | A | Der sichtbare Beschriftungstext MUSS im zugänglichen Namen enthalten sein. | `aria-label` beginnt mit dem sichtbaren Text. |
| O-18 | 2.5.4 | A | Bewegungsgesteuerte Funktionen MÜSSEN alternativ bedienbar und abschaltbar sein. | Schütteln, Kippen. |
| O-19 | 2.5.7 (neu in 2.2) | AA | Funktionen mit Ziehen (Drag & Drop) MÜSSEN eine Alternative ohne Ziehen haben. | Sortierlisten, Slider, Datei-Upload mit Schaltflächen oder Pfeiltasten. |
| O-20 | 2.5.8 (neu in 2.2) | AA | Zielgröße für Zeiger-Eingaben MUSS mindestens **24 × 24 CSS-px** betragen (oder ausreichend Abstand). | Mobile Navigation, Icon-Buttons, Pagination, Schließen-Buttons. |

### 2.3 Verständlich (Understandable)

| ID | WCAG | Stufe | Anforderung | Prüfung im Code / Test |
|---|---|---|---|---|
| U-01 | 3.1.1 | A | Die Seitensprache MUSS im `<html lang>` angegeben sein. | `<html lang="de">`. Gültiger Sprachcode. |
| U-02 | 3.1.2 | AA | Anderssprachige Passagen MÜSSEN mit `lang` ausgezeichnet sein. | `<span lang="en">`. |
| U-03 | 3.2.1 | A | Fokuserhalt DARF KEINE unerwartete Kontextänderung auslösen. | Kein automatischer Seitenwechsel bei `focus`. |
| U-04 | 3.2.2 | A | Eingabe in Felder DARF KEINE unerwartete Kontextänderung auslösen (z. B. Absenden bei `change`). | Select-Auswahl, die sofort navigiert, ohne Vorwarnung, ist unzulässig. |
| U-05 | 3.2.3 | AA | Wiederkehrende Navigationen MÜSSEN konsistent angeordnet sein. | Seitenübergreifender Vergleich. |
| U-06 | 3.2.4 | AA | Gleiche Funktionen MÜSSEN konsistent benannt sein. | Gleiche Beschriftung und Icons für gleiche Funktion. |
| U-07 | 3.2.6 (neu in 2.2) | A | Hilfemechanismen (Kontakt, Hilfe, Chat) MÜSSEN auf allen Seiten an gleicher relativer Position stehen. | Seitenübergreifender Vergleich. |
| U-08 | 3.3.1 | A | Eingabefehler MÜSSEN erkannt und im Text beschrieben werden. | Fehlermeldung als Text, nicht nur rot/Icon. Verknüpfung per `aria-describedby`, `aria-invalid="true"`. |
| U-09 | 3.3.2 | A | Eingabefelder MÜSSEN Beschriftungen oder Anweisungen haben. | Jedes Feld hat sichtbares `<label>`. Pflichtfelder gekennzeichnet. |
| U-10 | 3.3.3 | AA | Bei erkannten Fehlern MUSS ein Korrekturvorschlag erfolgen, wenn möglich. | Konkrete Hinweise (z. B. Formatbeispiel). |
| U-11 | 3.3.4 | AA | Bei rechtlich/finanziell relevanten Eingaben MUSS es Prüfung, Korrektur oder Bestätigung geben. | Bestellungen, Verträge: Zusammenfassung vor Absenden. |
| U-12 | 3.3.7 (neu in 2.2) | A | Bereits eingegebene Informationen DÜRFEN NICHT erneut abgefragt werden (Ausnahmen: Sicherheit). | Mehrstufige Formulare: Vorbefüllung, "Wie oben". |
| U-13 | 3.3.8 (neu in 2.2) | AA | Authentifizierung DARF KEINEN kognitiven Funktionstest verlangen (z. B. Merken, Abtippen), ohne Alternative. | Kein `autocomplete="off"` auf Login-Feldern, Einfügen in Passwortfelder erlaubt. Passwortmanager funktioniert. CAPTCHAs mit Alternative. |

### 2.4 Robust (Robust)

| ID | WCAG | Stufe | Anforderung | Prüfung im Code / Test |
|---|---|---|---|---|
| R-01 | 4.1.2 | A | Name, Rolle und Wert aller UI-Komponenten MÜSSEN programmatisch ermittelbar sein. | Native Elemente bevorzugen. Bei eigenen Widgets ARIA-Rolle, Name und Zustand setzen. |
| R-02 | 4.1.3 | AA | Statusmeldungen MÜSSEN ohne Fokusänderung an Hilfstechnologien gemeldet werden. | `role="status"`, `role="alert"` oder `aria-live`. Suchtreffer, Warenkorb, Erfolg und Fehler. |
| R-03 | 4.1.1 (WCAG 2.1) | A | HTML SOLL valide sein (siehe 1.4). | HTML-Validator. |

---

## 3. Code-Review-Regeln

### 3.1 Semantik hat Vorrang vor ARIA

**Erste Regel von ARIA:** Wenn ein natives HTML-Element die Funktion bietet, MUSS dieses genutzt werden. ARIA ergänzt nur, was HTML nicht leisten kann. Falsches ARIA ist schlechter als kein ARIA.

```html
<!-- Nicht zulässig -->
<div class="btn" onclick="speichern()">Speichern</div>

<!-- Zulässig -->
<button type="button" onclick="speichern()">Speichern</button>
```

| Anforderung | Prüffrage |
|---|---|
| Aktion löst etwas aus | Ist es ein `<button>`? |
| Navigation zu einer anderen Seite oder Stelle | Ist es ein `<a href>`? |
| Gruppierte Inhalte | Wird `<ul>/<ol>/<dl>` genutzt? |
| Datentabellen | `<table>` mit `<th>`, `scope`, `<caption>`? |
| Formularfelder | `<label for>` oder umschließendes `<label>`? |
| Gruppen aus Radio/Checkbox | `<fieldset>` mit `<legend>`? |

### 3.2 Seitenstruktur und Landmarks

- Pro Seite genau ein `<main>`, ein `<h1>`.
- `<header>`, `<nav>`, `<footer>`, `<aside>` sinnvoll nutzen. Mehrere `<nav>` bekommen unterscheidbare Namen (`aria-label`).
- Überschriftenebenen NICHT überspringen (`h1`, dann `h2`, nicht `h4`). Überschriften NICHT zur optischen Formatierung missbrauchen.
- Ein Skip-Link `<a href="#main">Zum Inhalt</a>` ist das erste fokussierbare Element.

### 3.3 Formulare

```html
<label for="mail">E-Mail-Adresse</label>
<input id="mail" name="mail" type="email" autocomplete="email"
       required aria-describedby="mail-hint mail-err" aria-invalid="true">
<p id="mail-hint">Beispiel: name@example.org</p>
<p id="mail-err">Bitte geben Sie eine gültige E-Mail-Adresse ein.</p>
```

Prüfpunkte:
- [ ] Jedes Feld hat ein sichtbares, programmatisch verknüpftes Label (kein reiner Platzhalter).
- [ ] Pflichtfelder sind im Text gekennzeichnet, nicht nur durch Farbe.
- [ ] Fehlermeldungen stehen am Feld, nennen das Problem und, wenn möglich, die Korrektur.
- [ ] Bei Fehler beim Absenden: Fokus auf Fehlerübersicht oder erstes fehlerhaftes Feld.
- [ ] `autocomplete`-Werte sind gesetzt, wo passend.
- [ ] Authentifizierung funktioniert mit Passwortmanager und Einfügen.

### 3.4 Interaktive Komponenten (eigene Widgets)

| Komponente | Mindestanforderungen |
|---|---|
| **Modaler Dialog** | `<dialog>` oder `role="dialog"` mit `aria-modal="true"` und Namen. Fokus beim Öffnen in den Dialog, Fokusfalle nur innerhalb, Esc schließt, Fokus zurück zum Auslöser. |
| **Menü / Disclosure** | Auslöser ist `<button>` mit `aria-expanded` und `aria-controls`. Nicht `role="menu"` für normale Navigation. |
| **Tabs** | `role="tablist"`, `tab`, `tabpanel`, `aria-selected`, Pfeiltasten wechseln, Tab springt in das Panel. |
| **Akkordeon** | Button in Überschrift mit `aria-expanded`. |
| **Autocomplete / Combobox** | ARIA-Combobox-Muster (APG) vollständig umsetzen oder native Alternative nutzen. |
| **Karussell** | Pause-Button, Tastatursteuerung, nicht automatisch ohne Stopp, Inhalt für Screenreader sinnvoll. |
| **Tooltip** | Auslösbar per Fokus, schließbar (Esc), nicht allein Träger wichtiger Information. |
| **Benutzerdefinierte Checkbox/Radio/Select** | Bevorzugt native Elemente stylen. Sonst vollständige Tastatur- und ARIA-Umsetzung nach APG. |

Quelle für Muster: WAI-ARIA Authoring Practices Guide (APG).

### 3.5 Bilder, Icons, SVG, Medien

```html
<!-- Informatives Bild -->
<img src="diagramm.png" alt="Umsatz 2025: Q1 2 Mio., Q2 3 Mio.">

<!-- Dekorativ -->
<img src="deko.svg" alt="">

<!-- Icon-Button -->
<button type="button" aria-label="Menü öffnen">
  <svg aria-hidden="true" focusable="false">…</svg>
</button>
```

- [ ] `<video>`/`<audio>` haben bedienbare Steuerelemente, Untertitel, Transkript.
- [ ] Kein Autoplay mit Ton.
- [ ] Inline-SVG mit Bedeutung hat `role="img"` und `<title>`, dekoratives SVG `aria-hidden="true"`.

### 3.6 CSS

- [ ] Kein `outline: none` ohne sichtbaren Ersatz. Fokus-Indikator mit mindestens 3:1 Kontrast.
- [ ] Informationen NICHT nur über `::before`/`::after`-Inhalt oder Farbe vermitteln.
- [ ] `display:none` und `visibility:hidden` entfernen aus dem Accessibility Tree (gewollt). Für nur visuell versteckten Text eine `.sr-only`-Klasse nutzen.
- [ ] `@media (prefers-reduced-motion: reduce)` für Animationen berücksichtigen.
- [ ] Schriftgrößen relativ (`rem`), Zeilenhöhe und Abstände flexibel.
- [ ] `@media (prefers-color-scheme)` und Windows-Kontrastmodus (`forced-colors`) beachten.
- [ ] Zielgrößen mindestens 24 × 24 px.

### 3.7 JavaScript / Single-Page-Apps

- [ ] Bei Routenwechsel: `document.title` aktualisieren und Fokus gezielt setzen (z. B. auf `h1` mit `tabindex="-1"`) oder per Live-Region ankündigen.
- [ ] Dynamische Inhalte: Statusmeldungen über `role="status"` bzw. `aria-live="polite"`. Dringendes über `role="alert"`. Live-Region MUSS schon vor der Änderung im DOM existieren.
- [ ] Fokus NICHT unerwartet verschieben. Fokus bei Entfernung des fokussierten Elements neu setzen.
- [ ] Event-Handler: Neben `click` auch Tastatur über native Elemente sicherstellen.
- [ ] Zeitgesteuerte Aktionen (Timeouts, Auto-Refresh) mit Warnung und Verlängerung.

### 3.8 ARIA: Typische Fehler

| Fehler | Korrektur |
|---|---|
| `role="button"` auf `<div>` ohne Tastaturbedienung | Natives `<button>` verwenden |
| `aria-label` auf Elementen ohne Rolle (`div`, `span`) | Passende Rolle oder natives Element |
| `aria-hidden="true"` auf fokussierbaren Elementen | Element aus Tab-Reihenfolge nehmen oder `aria-hidden` entfernen |
| Doppelte oder fehlende `id`-Verweise bei `aria-labelledby` / `aria-describedby` | Referenzen prüfen |
| `role="presentation"` auf semantisch wichtigen Elementen (Tabellen, Listen) | Rolle entfernen |
| Redundante Rollen (`<nav role="navigation">`) | Entfernen |
| `aria-label` überschreibt sichtbaren Text (Verstoß gegen 2.5.3) | Sichtbaren Text im Namen beibehalten |
| `tabindex` > 0 | Entfernen, DOM-Reihenfolge anpassen |

### 3.9 Dokumente (PDF, Office-Downloads)

- [ ] Dokumente werden bevorzugt als barrierefreies HTML angeboten.
- [ ] PDF: getaggt, Sprache gesetzt, Überschriften- und Listenstruktur, Alternativtexte, Lesereihenfolge, Titel angezeigt. Prüfung mit PAC (PDF Accessibility Checker).
- [ ] Link-Text nennt Format und Größe ("Antrag (PDF, 120 KB)").

---

## 4. Prüfverfahren

### 4.1 Prüfstufen

| Stufe | Vorgehen | Zweck |
|---|---|---|
| **1. Automatisiert** | axe-core, Lighthouse, WAVE, pa11y, Accessibility Insights, ESLint (`jsx-a11y` bzw. Entsprechungen) | Schnelle Erkennung eindeutiger Fehler. Findet nur einen Teil der Probleme. |
| **2. Manuell (Experte)** | Tastaturtest, Zoom/Reflow, Kontrast, Struktur, Formularlogik | Prüfung der Kriterien, die Tools nicht beurteilen können |
| **3. Hilfstechnologie** | Screenreader (NVDA, JAWS, VoiceOver, TalkBack), Sprachsteuerung, Vergrößerung | Prüfung der echten Nutzbarkeit |
| **4. Nutzertest** | Tests mit Menschen mit Behinderungen | Validierung der Praxistauglichkeit |

Ein bestandener automatischer Test bedeutet **nicht** Konformität.

### 4.2 Schnelltest (10 Minuten pro Seite)

1. **Tastatur:** Mit Tab durch die Seite. Ist alles erreichbar, sichtbar fokussiert, in sinnvoller Reihenfolge, ohne Falle?
2. **Zoom:** 200 % und 400 % (bzw. 320 px Breite). Kein Verlust, kein horizontales Scrollen.
3. **Textabstände:** Bookmarklet anwenden. Nichts wird abgeschnitten.
4. **Kontrast:** Stichprobe Text, Buttons, Rahmen, Fokus.
5. **Struktur:** Überschriften, Landmarks, Seitentitel, `lang`.
6. **Bilder:** CSS aus bzw. Alt-Texte prüfen.
7. **Formulare:** Labels, Fehlermeldungen, Autocomplete, Fehler absichtlich auslösen.
8. **Screenreader-Stichprobe:** Überschriften-, Landmark- und Formularnavigation, Dialoge, Statusmeldungen.
9. **Bewegung:** Pausierbarkeit, `prefers-reduced-motion`.
10. **Mobil:** Ausrichtung, Zielgrößen, Gesten.

### 4.3 Empfohlene Testkombinationen für Screenreader

| Betriebssystem | Kombination |
|---|---|
| Windows | NVDA + Firefox, NVDA + Chrome, JAWS + Chrome/Edge |
| macOS / iOS | VoiceOver + Safari |
| Android | TalkBack + Chrome |

### 4.4 Automatisierung in der Entwicklung

- **Linting:** Accessibility-Regeln im Linter (z. B. `eslint-plugin-jsx-a11y`).
- **Komponententests:** `jest-axe` oder `axe-core` in Unit-/Component-Tests.
- **End-to-End:** `@axe-core/playwright` oder pa11y-ci in der CI-Pipeline, Build bricht bei kritischen Verstößen ab.
- **Design-Review:** Kontraste und Zielgrößen vor der Umsetzung prüfen.
- **Definition of Done:** Barrierefreiheit ist Abnahmekriterium jeder Story.

---

## 5. Bewertung und Fehlerklassen

| Schweregrad | Definition | Beispiele | Handlung |
|---|---|---|---|
| **Kritisch** | Funktion oder Inhalt für Nutzergruppen nicht zugänglich | Login nicht per Tastatur möglich, Bestellung blockiert, Tastaturfalle | Sofort beheben, Release blockieren |
| **Hoch** | Starke Einschränkung, Umgehung nur schwer möglich | Fehlende Formularlabels, Kontrast unter 3:1 bei Fließtext, fehlende Alt-Texte bei zentralen Bildern | Vor Release beheben |
| **Mittel** | Erschwerte Nutzung, Umgehung möglich | Fokusreihenfolge leicht unlogisch, mehrdeutige Linktexte | Im nächsten Sprint beheben |
| **Niedrig** | Geringe Beeinträchtigung, Best-Practice-Abweichung | Redundantes ARIA, kleinere Kontrastabweichungen bei Zusatztext | Planen |

---

## 6. Prüfprotokoll (Vorlage)

```markdown
### Befund <laufende Nummer>

- **ID Anforderung:** z. B. O-13
- **WCAG-Kriterium / Stufe:** 2.4.7 Focus Visible, AA
- **Seite / URL / Komponente:**
- **Schweregrad:** Kritisch | Hoch | Mittel | Niedrig
- **Beschreibung des Mangels:**
- **Betroffene Nutzergruppen:** z. B. Tastaturnutzer, Screenreader-Nutzer
- **Reproduktionsschritte:**
- **Fundstelle im Code / Screenshot:**
- **Empfohlene Korrektur:**
- **Status:** Offen | In Arbeit | Behoben | Geprüft
```

---

## 7. Review-Checkliste (kompakt)

### Struktur und Navigation
- [ ] `lang` gesetzt, `<title>` aussagekräftig, genau ein `<h1>`, Überschriftenhierarchie ohne Sprünge
- [ ] Landmarks (`header`, `nav`, `main`, `footer`) vorhanden und sinnvoll benannt
- [ ] Skip-Link funktioniert
- [ ] Mindestens zwei Navigationswege, konsistente Navigation und Hilfe

### Inhalte
- [ ] Alt-Texte korrekt (informativ, dekorativ, funktional)
- [ ] Linktexte verständlich
- [ ] Tabellen mit Headern, Listen als Listen
- [ ] Videos mit Untertiteln, Transkript, Audiodeskription
- [ ] Keine Information nur über Farbe

### Darstellung
- [ ] Kontrast Text ≥ 4,5:1 (groß ≥ 3:1), UI und Grafiken ≥ 3:1
- [ ] 200 % Zoom, Reflow bei 320 px, Textabstände ohne Verlust
- [ ] Fokus sichtbar (Kontrast ≥ 3:1) und nicht verdeckt
- [ ] Zielgrößen ≥ 24 × 24 px

### Bedienung
- [ ] Alles per Tastatur erreichbar und bedienbar, keine Falle
- [ ] Logische Fokusreihenfolge
- [ ] Drag-Funktionen mit Alternative, Gesten mit Alternative
- [ ] Bewegte Inhalte pausierbar, `prefers-reduced-motion` beachtet
- [ ] Zeitlimits einstellbar

### Formulare und Authentifizierung
- [ ] Labels, Anweisungen, Pflichtfelder im Text
- [ ] Fehlermeldungen als Text, verknüpft, mit Korrekturhinweis
- [ ] `autocomplete`, keine erneute Abfrage bekannter Daten
- [ ] Login ohne kognitiven Funktionstest, Passwortmanager und Einfügen erlaubt
- [ ] Bestätigung bei rechtlich/finanziell relevanten Aktionen

### Technik / ARIA
- [ ] Native Elemente statt `div`/`span` mit Rollen
- [ ] Name, Rolle, Wert aller Komponenten korrekt
- [ ] Statusmeldungen über Live-Regionen
- [ ] Dialoge: Fokusführung, Esc, Rückgabe des Fokus
- [ ] SPA: Titel und Fokus bei Routenwechsel
- [ ] Valides HTML, eindeutige IDs

### Organisation
- [ ] Erklärung zur Barrierefreiheit vorhanden und aktuell (soweit rechtlich gefordert)
- [ ] Feedback-Möglichkeit für Barrieren
- [ ] Barrierefreiheit in Definition of Done und CI verankert

---

## 8. Quellen und weiterführende Informationen

- W3C: Web Content Accessibility Guidelines (WCAG) 2.2, https://www.w3.org/TR/WCAG22/
- W3C WAI: ARIA Authoring Practices Guide (APG), https://www.w3.org/WAI/ARIA/apg/
- ETSI: EN 301 549 (V3.2.1 und V4.1.1)
- Gesetze: BITV 2.0, BFSG und BFSGV (gesetze-im-internet.de)
- Bundesfachstelle Barrierefreiheit (bundesfachstelle-barrierefreiheit.de)
- BIK für Alle: Prüfverfahren BITV-Test (bitvtest.de)

> Die Angaben zum Normstand (EN 301 549 V4.1.1) beruhen auf Veröffentlichungen aus September 2026. Der Stand der Zitierung im EU-Amtsblatt sollte vor jeder rechtlich relevanten Entscheidung erneut geprüft werden.
