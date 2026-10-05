package de.eichstaedt.ortswehrfuehrer.application.uebung;

import de.eichstaedt.ortswehrfuehrer.domain.Ausbildung;
import de.eichstaedt.ortswehrfuehrer.domain.Einsatzfahrzeug;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Bewertungspunkt;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Einsatzauftrag;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.FunktionsBesetzung;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.GuardrailPruefbericht;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Lageentwicklung;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Schwierigkeitsgrad;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Sicherheitshinweis;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.SzenarioParameter;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.TaktischeEinheit;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.TaktischeFunktion;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Uebungsart;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

/**
 * Katalog von taktischen Fachvorlagen und Generierungsregeln nach FwDV 3 und UVV.
 */
@ApplicationScoped
public class SzenarioKatalog {

  public static record VorlagenPaket(
      String titel,
      String alarmstichwort,
      String ausgangslage,
      String schadenslage,
      List<Einsatzauftrag> auftraege,
      List<Lageentwicklung> lageentwicklungen,
      List<Sicherheitshinweis> sicherheitshinweise,
      List<Bewertungspunkt> bewertungspunkte
  ) {}

  public VorlagenPaket erstelleLoeschuebung(
      TaktischeEinheit einheit,
      List<FunktionsBesetzung> besetzungen,
      List<Einsatzfahrzeug> fahrzeuge,
      SzenarioParameter parameter,
      GuardrailPruefbericht guardrailBericht
  ) {
    long anzahlAgt = besetzungen.stream()
        .filter(FunktionsBesetzung::istBesetzt)
        .filter(b -> b.hatQualifikation(Ausbildung.ATEMSCHUTZGERAETETRAEGER))
        .count();

    boolean darfInnenangriff = anzahlAgt >= 4;
    String ort = parameter != null && !parameter.ortObjekt().isBlank() ? parameter.ortObjekt() : "Wohngebäude Dorfstraße 14";
    Schwierigkeitsgrad grad = parameter != null ? parameter.schwierigkeit() : Schwierigkeitsgrad.MITTEL;

    String fahrzeugNamen = ermittleFahrzeugnamen(fahrzeuge, "Löschfahrzeug");

    if (darfInnenangriff) {
      return erstelleZimmerbrandMitInnenangriff(einheit, ort, fahrzeugNamen, grad);
    } else {
      return erstelleAussenangriffLage(einheit, ort, fahrzeugNamen, grad, anzahlAgt);
    }
  }

  public VorlagenPaket erstelleTechnischeHilfeleistung(
      TaktischeEinheit einheit,
      List<FunktionsBesetzung> besetzungen,
      List<Einsatzfahrzeug> fahrzeuge,
      SzenarioParameter parameter,
      GuardrailPruefbericht guardrailBericht
  ) {
    boolean hatHydraulik = fahrzeuge.stream().anyMatch(f -> f.getFahrzeugtyp() != null && f.getFahrzeugtyp().name().toUpperCase().startsWith("HLF"));
    String ort = parameter != null && !parameter.ortObjekt().isBlank() ? parameter.ortObjekt() : "Kreuzung B102 / Ortsausgang";
    Schwierigkeitsgrad grad = parameter != null ? parameter.schwierigkeit() : Schwierigkeitsgrad.MITTEL;
    String fahrzeugNamen = ermittleFahrzeugnamen(fahrzeuge, "Hilfeleistungsfahrzeug");

    if (hatHydraulik) {
      return erstelleVerkehrsunfallEingeklemmt(einheit, ort, fahrzeugNamen, grad);
    } else {
      return erstelleThBasismassnahmen(einheit, ort, fahrzeugNamen, grad);
    }
  }

  private VorlagenPaket erstelleZimmerbrandMitInnenangriff(
      TaktischeEinheit einheit,
      String ort,
      String fahrzeugNamen,
      Schwierigkeitsgrad grad
  ) {
    String titel = "Brand mit Menschenrettung – " + ort;
    String stichwort = "B2 - Gebäudebrand / Zimmerbrand";
    String ausgangslage = String.format("Gemeldeter Zimmerbrand im 1. Obergeschoss des Objekts %s. Starke Rauchentwicklung aus zwei Fenstern. 1 Person im Gebäude vermisst. Wasserentnahmestelle: Unterflurhydrant vor dem Gebäude.", ort);
    String schadenslage = "Rauchentwicklung breitet sich in den Treppenraum aus. Brandherd im Schlafzimmer. Keine sichtbaren Flammen im Außenbereich.";

    List<Einsatzauftrag> auftraege = new ArrayList<>();
    auftraege.add(new Einsatzauftrag(
        "Angriffstrupp",
        "zur Menschenrettung und Brandbekämpfung",
        "1. C-Rohr unter Atemschutz",
        "in das 1. Obergeschoss",
        "über Treppenraum"
    ));
    auftraege.add(new Einsatzauftrag(
        "Wassertrupp",
        "als Sicherheitstrupp unter Atemschutz und zur Herstellung der Wasserversorgung",
        "Standrohr und B-Leitung",
        "vom Unterflurhydranten zum Fahrzeug",
        "über Gehweg"
    ));

    if (einheit.istGruppe()) {
      auftraege.add(new Einsatzauftrag(
          "Schlauchtrupp",
          "zur Riegelstellung und Vorbereitung der Belüftung",
          "2. C-Rohr und Drucklüfter",
          "zum Hauseingang und Nachbargrundstück",
          "über Vorgarten"
      ));
      auftraege.add(new Einsatzauftrag(
          "Melder",
          "zur Lagefeststellung und Unterstützung des Gruppenführers",
          "Handfunkgerät und Dokumentationsmappe",
          "an der Einsatzstelle",
          "über Einsatzabschnitt 1"
      ));
    }

    auftraege.add(new Einsatzauftrag(
        "Maschinist",
        "zur Bereitstellung von Löschwasser und Überwachung der Pumpe",
        "Feuerlöschkreiselpumpe und Verteiler",
        "am Einsatzfahrzeug",
        "über Straßenrand"
    ));

    List<Lageentwicklung> lage = new ArrayList<>();
    lage.add(new Lageentwicklung(10, "Vermisste Person im Flur aufgefunden und an Rettungsdienst übergeben.", "Notfallmeldung an Einheitsführer, weitere Brandbekämpfung fortsetzen."));
    if (grad == Schwierigkeitsgrad.ANSPRUCHSVOLL) {
      lage.add(new Lageentwicklung(20, "Druckabfall im Hydrantennetz – Wasserversorgung gefährdet.", "Wasserpuffer im Tank nutzen und zweite Leitung vom offenen Gewässer vorbereiten."));
      lage.add(new Lageentwicklung(30, "Meldung einer Verpuffung im Nebenraum – Nullsicht im 1. OG.", "Rückzugsweg prüfen, Sicherheitstrupp in Bereitschaft halten, Lüftung einleiten."));
    } else {
      lage.add(new Lageentwicklung(20, "Feuer unter Kontrolle, Nachlöscharbeiten mit Wärmebildkamera.", "Belüftung des Gebäudes einleiten und Brandgut ins Freie bringen."));
    }

    List<Sicherheitshinweis> sicherheit = List.of(
        new Sicherheitshinweis("FwDV 7", "Atemschutz", "Lückenlose Atemschutzüberwachung (Zeit, Druck, Standort) vor dem Betreten des Gefahrenbereichs durchführen.", true),
        new Sicherheitshinweis("DGUV Vorschrift 49", "UVV", "Rückzugsweg freihalten, Schlauchreserven im Treppenraum knickfrei auslegen.", true),
        new Sicherheitshinweis("FwDV 3", "Taktik", "Sicherheitstrupp steht voll ausgerüstet mit angeschlossenem PA am Verteiler bereit.", true)
    );

    List<Bewertungspunkt> punkte = List.of(
        new Bewertungspunkt("Führung", "Vollständiger FwDV-3-Einsatzbefehl (Einheit, Auftrag, Mittel, Ziel, Weg) gegeben", 3),
        new Bewertungspunkt("Atemschutz", "Atemschutzüberwachung korrekt dokumentiert und Funkprüfung erfolgt", 3),
        new Bewertungspunkt("Sicherheit", "Sicherheitstrupp einsatzbereit am Verteiler positioniert", 2),
        new Bewertungspunkt("Wasserversorgung", "Stabile Löschwasserversorgung vor dem Vorgehen des AT gesichert", 2)
    );

    return new VorlagenPaket(titel, stichwort, ausgangslage, schadenslage, auftraege, lage, sicherheit, punkte);
  }

  private VorlagenPaket erstelleAussenangriffLage(
      TaktischeEinheit einheit,
      String ort,
      String fahrzeugNamen,
      Schwierigkeitsgrad grad,
      long anzahlAgt
  ) {
    String titel = "Gebäudebrand Außenangriff & Riegelstellung – " + ort;
    String stichwort = "B2 - Schuppenbrand / Außenangriff";
    String ausgangslage = String.format("Vollbrand eines freistehenden Schuppens und Anbaus bei %s. Keine Personen im Gebäude. Gefahr des Übergreifens auf angrenzendes Wohnhaus. Eingesetzte Fahrzeuge: %s.", ort, fahrzeugNamen);
    String schadenslage = "Flammen schlagen aus Dachstuhl. Strahlungswärme gefährdet Nachbargebäude im Abstand von 4 Metern. Offene Wasserentnahme aus Löschteich in 80 m Entfernung.";

    List<Einsatzauftrag> auftraege = new ArrayList<>();
    auftraege.add(new Einsatzauftrag(
        "Angriffstrupp",
        "zur Riegelstellung zum Schutz des Nachbargebäudes",
        "1. B-Rohr / C-Hohlstrahlrohr",
        "zur Gebäudewand Ostseite",
        "über Einfahrt"
    ));
    auftraege.add(new Einsatzauftrag(
        "Wassertrupp",
        "zur offenen Wasserentnahme und Speisung des Fahrzeugs",
        "4 Saugschläuche, Saugkorb und Leinen",
        "vom Löschteich zum Fahrzeug",
        "über Wiese"
    ));

    if (einheit.istGruppe()) {
      auftraege.add(new Einsatzauftrag(
          "Schlauchtrupp",
          "zur Brandbekämpfung der Brandstelle von der Westseite",
          "2. C-Rohr",
          "zur Schuppenfront",
          "über Garten"
      ));
    }

    auftraege.add(new Einsatzauftrag(
        "Maschinist",
        "zur Inbetriebnahme der Saugleitung und Förderung",
        "Saugleitung und Tragkraftspritze/FP",
        "am Fahrzeug / Wasserentnahmestelle",
        "über befestigten Weg"
    ));

    List<Lageentwicklung> lage = new ArrayList<>();
    lage.add(new Lageentwicklung(10, "Erfolgreiche Riegelstellung, Ausbreitung auf das Wohnhaus gestoppt.", "Fokus auf Niederschlagen der Flammen im Schuppen richten."));
    if (grad == Schwierigkeitsgrad.ANSPRUCHSVOLL) {
      lage.add(new Lageentwicklung(20, "Ansauggitter verlegt durch Schlamm/Blätter, Förderdruck bricht ein.", "Saugkorb spülen, Saugschutzkorb/Korbhalterung anpassen."));
    } else {
      lage.add(new Lageentwicklung(20, "Dachhaut stürzt ein, gezielte Ablöschung von Brandnestern.", "Schaumteppich oder Netzmittel einsetzen."));
    }

    List<Sicherheitshinweis> sicherheit = List.of(
        new Sicherheitshinweis("DGUV Vorschrift 49", "UVV", "Trümmerschatten und Einsturzbereich bei brennenden Holzstrukturen einhalten (1,5-fache Wandhöhe).", true),
        new Sicherheitshinweis("FwDV 3", "Löscheinsatz", "Schutzkleidung gegen Wärmestrahlung vollständig anlegen (Visier, Handschuhe).", false),
        new Sicherheitshinweis("FwDV 7", "Hinweis", "Wegen reduzierter AGT-Kapazität ist das Betreten des Gefahrenbereichs streng untersagt.", true)
    );

    List<Bewertungspunkt> punkte = List.of(
        new Bewertungspunkt("Taktik", "Schutz des Nachbargebäudes vor Brandbekämpfung priorisiert (Riegelstellung)", 3),
        new Bewertungspunkt("Wasserförderung", "Saugleitung nach FwDV 3 ordnungsgemäß gekuppelt und gesichert", 3),
        new Bewertungspunkt("Sicherheit", "Sicherheitsabstand zum einsturzgefährdeten Dach eingehalten", 2)
    );

    return new VorlagenPaket(titel, stichwort, ausgangslage, schadenslage, auftraege, lage, sicherheit, punkte);
  }

  private VorlagenPaket erstelleVerkehrsunfallEingeklemmt(
      TaktischeEinheit einheit,
      String ort,
      String fahrzeugNamen,
      Schwierigkeitsgrad grad
  ) {
    String titel = "THL - Verkehrsunfall mit eingeklemmter Person – " + ort;
    String stichwort = "H:Hilfeleistung-Klemm / VU mit P-klemmt";
    String ausgangslage = String.format("PKW in Seitenlage gegen Baum geprallt auf %s. 1 Person im Fahrzeug eingeklemmt, ansprechbar aber verletzt. Betriebsmittel laufen aus. Eingesetzte Kräfte mit %s.", ort, fahrzeugNamen);
    String schadenslage = "Fahrzeug ungesichert auf Fahrerseite. Deformation an B-Säule blockiert Fahrertür. Dämmerung / Dunkelheit erfordert Ausleuchtung.";

    List<Einsatzauftrag> auftraege = new ArrayList<>();
    auftraege.add(new Einsatzauftrag(
        "Angriffstrupp",
        "zur Personenbetreuung und Schaffung einer Erstöffnung",
        "Halligan-Tool, Federkörner, Glasmanagement und Decke",
        "zur Heckscheibe / Beifahrerfenster des verunfallten PKW",
        "über Straßenbankett"
    ));
    auftraege.add(new Einsatzauftrag(
        "Wassertrupp",
        "zur Absicherung gegen Brandgefahr (zweifacher Brandschutz) und Ausleuchtung der Einsatzstelle",
        "Zweifacher Brandschutz (Pulverlöscher + Schnellangriff/C-Leitung) und Flutlichtstrahler",
        "zur Unfallstelle",
        "über rechten Fahrbahnrand"
    ));

    if (einheit.istGruppe()) {
      auftraege.add(new Einsatzauftrag(
          "Schlauchtrupp",
          "zur Geräteablage und Unterstützung bei der Stabilisierung",
          "Bereitstellungsplane, Unterbaumaterial (Stab-Fast / Formhölzer) und Hydraulikaggregat",
          "zur Gerätebereitstellungszone",
          "über Gehweg"
      ));
      auftraege.add(new Einsatzauftrag(
          "Melder",
          "zur Verkehrsabsicherung und Lagemeldung",
          "Warndreieck, Blitzleuchte und Leitkegel",
          "100 m vor die Unfallstelle in Fahrtrichtung",
          "über Standstreifen"
      ));
    }

    auftraege.add(new Einsatzauftrag(
        "Maschinist",
        "zur Absicherung mit Blaulicht, Warnblinkanlage und Inbetriebnahme des Aggregats",
        "Lichtmast, Bordnetz und Hydraulikaggregat",
        "am Einsatzfahrzeug",
        "über Fahrbahn"
    ));

    List<Lageentwicklung> lage = new ArrayList<>();
    lage.add(new Lageentwicklung(10, "Fahrzeug stabilisiert, Notarzt trifft ein und ordnet schonende Rettung an.", "Große Seitenöffnung mittels Schere und Spreizer vorbereiten."));
    if (grad == Schwierigkeitsgrad.ANSPRUCHSVOLL) {
      lage.add(new Lageentwicklung(20, "Batteriebrand im Motorraum droht durch Kurzschluss.", "Brandschutz aktivieren, Massekabel mit geeignetem Werkzeug kappen."));
      lage.add(new Lageentwicklung(30, "Zustandsverschlechterung der Person – Notarzt ordnet Sofortrettung (Crash-Rettung) an.", "Taktikwechsel von schonender Rettung auf Sofortrettung mittels Spineboard."));
    } else {
      lage.add(new Lageentwicklung(20, "Dachentfernung abgeschlossen, Person achsengerecht über Spineboard befreit.", "Übergabe an Rettungsdienst und Übergabe der Einsatzstelle an Polizei."));
    }

    List<Sicherheitshinweis> sicherheit = List.of(
        new Sicherheitshinweis("DGUV Vorschrift 49", "UVV", "Verkehrsabsicherung und Warnkleidung (Warnweste/Einsatzkleidung Reflex) zwingend anlegen.", true),
        new Sicherheitshinweis("FwDV 3", "TH-Einsatz", "Zweifacher Brandschutz (Wasser/Schaum und Pulver) vor Beginn der technischen Rettung sicherstellen.", true),
        new Sicherheitshinweis("DGUV Information 205-010", "Patientengerechte Rettung", "Airbag-Sicherheitsabstände (Regel 30-60-90) und Gasdruckdämpfer beim Schneiden beachten.", true)
    );

    List<Bewertungspunkt> punkte = List.of(
        new Bewertungspunkt("Einsatzstellenhygiene & Ordnung", "Geräteablage nach FwDV 3 sauber aufgebaut", 2),
        new Bewertungspunkt("Sicherheit", "Zweifacher Brandschutz und Verkehrsabsicherung lückenlos errichtet", 3),
        new Bewertungspunkt("Technik", "Fahrzeugstabilisierung wirksam vor Beginn hydraulischer Arbeiten durchgeführt", 3),
        new Bewertungspunkt("Patientenschutz", "Glasmanagement und Patientenschutzplane konsequent angewendet", 2)
    );

    return new VorlagenPaket(titel, stichwort, ausgangslage, schadenslage, auftraege, lage, sicherheit, punkte);
  }

  private VorlagenPaket erstelleThBasismassnahmen(
      TaktischeEinheit einheit,
      String ort,
      String fahrzeugNamen,
      Schwierigkeitsgrad grad
  ) {
    String titel = "THL - Sturmschaden & Verkehrsabsicherung – " + ort;
    String stichwort = "H:Hilfeleistung-Natur / Baum auf Straße";
    String ausgangslage = String.format("Nach Sturm liegt ein Baum quer über der Fahrbahn bei %s. Keine Verletzten. Dunkelheit und Gegenverkehr. Eingesetzte Kräfte mit %s.", ort, fahrzeugNamen);
    String schadenslage = "Baumkrone blockiert beide Fahrspuren. Äste unter Spannung. Gefahr durch unaufmerksamen Nachfolgeverkehr.";

    List<Einsatzauftrag> auftraege = new ArrayList<>();
    auftraege.add(new Einsatzauftrag(
        "Angriffstrupp",
        "zur Erkundung von Spannungen und Beseitigung der Äste",
        "Motorsäge, Schnittschutzkleidung und Fällheber",
        "zur Baumkrone auf der Fahrbahn",
        "über Straßenrand"
    ));
    auftraege.add(new Einsatzauftrag(
        "Wassertrupp",
        "zur weiträumigen Verkehrsabsicherung und Ausleuchtung",
        "Warndreiecke, Blitzleuchten, Leitkegel und Flutlichtstrahler",
        "100 m vor und hinter die Einsatzstelle",
        "über Seitenstreifen"
    ));

    auftraege.add(new Einsatzauftrag(
        "Maschinist",
        "zur Absicherung des Fahrzeugs und Betrieb des Stromerzeugers",
        "Lichtmast und Stromerzeuger",
        "am Fahrzeug",
        "über Fahrbahnrand"
    ));

    List<Lageentwicklung> lage = new ArrayList<>();
    lage.add(new Lageentwicklung(10, "Hauptstamm steht unter starker Biegespannung.", "Spannungsschnitt fachgerecht von der Druckseite her ansetzen."));
    lage.add(new Lageentwicklung(20, "Fahrbahn geräumt und mit Besen von Splittern gereinigt.", "Verkehrsabsicherung abbauen und Rückmeldung an Leitstelle."));

    List<Sicherheitshinweis> sicherheit = List.of(
        new Sicherheitshinweis("DGUV Regel 114-018", "Motorsägeneinsatz", "Vollständige PSA (Schnittschutzhose, Forsthelm mit Visier/Gehörschutz, Schnittschutzschuhe) zwingend tragen.", true),
        new Sicherheitshinweis("DGUV Vorschrift 49", "UVV", "Sicherheitsbereich im doppelten Baumlängenradius beachten.", true)
    );

    List<Bewertungspunkt> punkte = List.of(
        new Bewertungspunkt("Sicherheit", "Schnittschutzkleidung vollständig und vor Sägenbeginn angelegt", 3),
        new Bewertungspunkt("Verkehrsabsicherung", "Verkehrsabsicherung ordnungsgemäß positioniert", 2),
        new Bewertungspunkt("Sägetechnik", "Schnitttechnik unter Spannungsbeachtung korrekt ausgeführt", 3)
    );

    return new VorlagenPaket(titel, stichwort, ausgangslage, schadenslage, auftraege, lage, sicherheit, punkte);
  }

  private String ermittleFahrzeugnamen(List<Einsatzfahrzeug> fahrzeuge, String fallback) {
    if (fahrzeuge == null || fahrzeuge.isEmpty()) {
      return fallback;
    }
    List<String> namen = fahrzeuge.stream()
        .map(f -> {
          if (f.getBezeichnung() != null && !f.getBezeichnung().isBlank()) {
            return f.getBezeichnung();
          }
          if (f.getFahrzeugtyp() != null) {
            return f.getFahrzeugtyp().name();
          }
          return "Einsatzfahrzeug";
        })
        .toList();
    return String.join(", ", namen);
  }
}
