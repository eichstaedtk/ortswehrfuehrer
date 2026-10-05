package de.eichstaedt.ortswehrfuehrer.application.uebung;

import de.eichstaedt.ortswehrfuehrer.domain.Ausbildung;
import de.eichstaedt.ortswehrfuehrer.domain.Einsatzfahrzeug;
import de.eichstaedt.ortswehrfuehrer.domain.Fahrzeugtyp;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.FunktionsBesetzung;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.GuardrailPruefbericht;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.SzenarioParameter;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.TaktischeEinheit;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.TaktischeFunktion;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Uebungsart;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Uebungsszenario;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Validierungs- und Guardrail-Engine zur Überprüfung von Übungsszenarien
 * gemäß FwDV 2, FwDV 3, FwDV 7 und DGUV Vorschrift 49 (UVV Feuerwehr).
 */
@ApplicationScoped
public class SzenarioGuardrailService {

  public GuardrailPruefbericht pruefeSzenario(Uebungsszenario szenario, List<Einsatzfahrzeug> ausgewaehlteFahrzeuge) {
    if (szenario == null) {
      return GuardrailPruefbericht.fehlerhaft(List.of("Szenario darf nicht null sein."), List.of(), List.of());
    }

    return pruefe(
        szenario.getArt(),
        szenario.getTaktischeEinheit(),
        szenario.getBesetzungen(),
        ausgewaehlteFahrzeuge,
        szenario.getParameter()
    );
  }

  public GuardrailPruefbericht pruefe(
      Uebungsart art,
      TaktischeEinheit einheit,
      List<FunktionsBesetzung> besetzungen,
      List<Einsatzfahrzeug> fahrzeuge,
      SzenarioParameter parameter
  ) {
    List<String> fehler = new ArrayList<>();
    List<String> warnungen = new ArrayList<>();
    List<String> empfehlungen = new ArrayList<>();

    Uebungsart uebungsart = art != null ? art : Uebungsart.LOESCHUEBUNG;
    TaktischeEinheit taktischeEinheit = einheit != null ? einheit : TaktischeEinheit.GRUPPE;
    List<FunktionsBesetzung> besetzungsListe = besetzungen != null ? besetzungen : List.of();
    List<Einsatzfahrzeug> fahrzeugListe = fahrzeuge != null ? fahrzeuge : List.of();

    // 1. Fahrzeugprüfung
    pruefeFahrzeuge(fahrzeugListe, uebungsart, fehler, warnungen, empfehlungen);

    // 2. Mannschafts- und Sollstärkeprüfung
    pruefeMannschaftsstaerke(taktischeEinheit, besetzungsListe, fehler, warnungen, empfehlungen);

    // 3. Führungsqualifikation (Einheitsführer)
    pruefeFuehrungsqualifikation(taktischeEinheit, besetzungsListe, fehler, warnungen, empfehlungen);

    // 4. Maschinistenqualifikation
    pruefeMaschinistenqualifikation(besetzungsListe, fehler, warnungen, empfehlungen);

    // 5. Atemschutz-Guardrails (Löschübung / Innenangriff)
    if (uebungsart == Uebungsart.LOESCHUEBUNG) {
      pruefeAtemschutz(taktischeEinheit, besetzungsListe, fehler, warnungen, empfehlungen);
    }

    // 6. TH-Guardrails (Technische Hilfeleistung)
    if (uebungsart == Uebungsart.TECHNISCHE_HILFELEISTUNG) {
      pruefeTechnischeHilfeleistung(fahrzeugListe, besetzungsListe, fehler, warnungen, empfehlungen);
    }

    // 7. Weitere taktische Empfehlungen (Funk, etc.)
    pruefeZusatzqualifikationen(besetzungsListe, empfehlungen);

    boolean istGueltig = fehler.isEmpty();
    return new GuardrailPruefbericht(istGueltig, fehler, warnungen, empfehlungen);
  }

  private void pruefeFahrzeuge(
      List<Einsatzfahrzeug> fahrzeuge,
      Uebungsart art,
      List<String> fehler,
      List<String> warnungen,
      List<String> empfehlungen
  ) {
    if (fahrzeuge.isEmpty()) {
      fehler.add("Mindestens ein Einsatzfahrzeug muss für die Übung ausgewählt sein.");
      return;
    }

    if (art == Uebungsart.LOESCHUEBUNG) {
      boolean hatLoeschfahrzeug = fahrzeuge.stream().anyMatch(this::istLoeschfahrzeug);
      if (!hatLoeschfahrzeug) {
        warnungen.add("Keines der ausgewählten Fahrzeuge führt Löschwasser oder eine Pumpe mit. Löschwasserversorgung muss extern aufgebaut werden.");
        empfehlungen.add("Übungsschwerpunkt auf offene Wasserentnahme oder lange Wegstrecke setzen.");
      }
    }
  }

  private void pruefeMannschaftsstaerke(
      TaktischeEinheit einheit,
      List<FunktionsBesetzung> besetzungen,
      List<String> fehler,
      List<String> warnungen,
      List<String> empfehlungen
  ) {
    long besetztePositionen = besetzungen.stream().filter(FunktionsBesetzung::istBesetzt).count();

    if (besetztePositionen == 0) {
      fehler.add("Es wurden noch keine Kameraden für die Übung zugewiesen.");
      return;
    }

    if (besetztePositionen < 3) {
      fehler.add(String.format("Die Mindeststärke von 3 Einsatzkräften (Selbstständiger Trupp) wird unterschritten (aktuell besetzt: %d).", besetztePositionen));
      return;
    }

    int sollStaerke = einheit.getSollStaerke();
    if (besetztePositionen < sollStaerke) {
      if (einheit == TaktischeEinheit.GRUPPE) {
        if (besetztePositionen >= 6) {
          warnungen.add(String.format("Für eine Gruppe (1/8/9) fehlen %d Einsatzkräfte (aktuell: %d).", (sollStaerke - besetztePositionen), besetztePositionen));
          empfehlungen.add("Reduzierung der Taktischen Einheit auf Staffel (1/5/6) empfohlen.");
        } else {
          fehler.add(String.format("Für eine Gruppe (1/8/9) sind nur %d von 9 Einsatzkräften besetzt. Bitte weisen Sie weiteres Personal zu oder wählen Sie Staffel oder Selbstständigen Trupp.", besetztePositionen));
          empfehlungen.add("Reduzierung auf Staffel oder Selbstständigen Trupp vorgeschlagen.");
        }
      } else if (einheit == TaktischeEinheit.STAFFEL) {
        warnungen.add(String.format("Für eine Staffel (1/5/6) sind nur %d von 6 Positionen besetzt.", besetztePositionen));
        empfehlungen.add("Reduzierung auf Selbstständigen Trupp (1/2/3) oder Nachbesetzung der Staffel empfohlen.");
      } else {
        warnungen.add(String.format("Für die Taktische Einheit %s sind %d von %d Positionen besetzt.", einheit.getBezeichnung(), besetztePositionen, sollStaerke));
      }
    }
  }

  private void pruefeFuehrungsqualifikation(
      TaktischeEinheit einheit,
      List<FunktionsBesetzung> besetzungen,
      List<String> fehler,
      List<String> warnungen,
      List<String> empfehlungen
  ) {
    Optional<FunktionsBesetzung> efOpt = besetzungen.stream()
        .filter(b -> b.funktion() == TaktischeFunktion.EINHEITSFUEHRER)
        .findFirst();

    if (efOpt.isEmpty() || !efOpt.get().istBesetzt()) {
      fehler.add("Die Position des Einheitsführers ist zwingend erforderlich und muss besetzt sein.");
      return;
    }

    FunktionsBesetzung ef = efOpt.get();
    boolean hatGruppenfuehrer = ef.hatQualifikation(Ausbildung.GRUPPENFUEHRER)
        || ef.hatQualifikation(Ausbildung.ZUGFUEHRER)
        || ef.hatQualifikation(Ausbildung.VERBANDSFUEHRER)
        || ef.hatQualifikation(Ausbildung.LEITER_EINER_FEUERWEHR);
    boolean hatTruppfuehrer = ef.hatQualifikation(Ausbildung.TRUPPFUEHRER);

    if (einheit == TaktischeEinheit.GRUPPE) {
      if (!hatGruppenfuehrer) {
        if (hatTruppfuehrer) {
          warnungen.add(String.format("Einheitsführer %s besitzt Truppführer-Qualifikation, für eine Gruppe ist regulär Gruppenführer-Ausbildung (FwDV 2 Ziffer 4.1) vorgesehen.", ef.kameradName()));
          empfehlungen.add("Übung unter besonderer Beobachtung der Führungsvorgänge durch den Übungsleiter durchführen.");
        } else {
          fehler.add(String.format("Einheitsführer %s verfügt über keine Führungsausbildung (Gruppenführer/Truppführer nach FwDV 2).", ef.kameradName()));
        }
      }
    } else {
      // Staffel oder Selbstständiger Trupp: Truppführer ausreichend
      if (!hatGruppenfuehrer && !hatTruppfuehrer) {
        fehler.add(String.format("Einheitsführer %s verfügt über keine Führungsausbildung (mind. Truppführer FwDV 2 Ziffer 2.2).", ef.kameradName()));
      }
    }
  }

  private void pruefeMaschinistenqualifikation(
      List<FunktionsBesetzung> besetzungen,
      List<String> fehler,
      List<String> warnungen,
      List<String> empfehlungen
  ) {
    Optional<FunktionsBesetzung> maOpt = besetzungen.stream()
        .filter(b -> b.funktion() == TaktischeFunktion.MASCHINIST)
        .findFirst();

    if (maOpt.isEmpty() || !maOpt.get().istBesetzt()) {
      fehler.add("Die Position des Maschinisten muss besetzt sein.");
      return;
    }

    FunktionsBesetzung ma = maOpt.get();
    if (!ma.hatQualifikation(Ausbildung.MASCHINIST)) {
      warnungen.add(String.format("Maschinist %s besitzt im System keine eingetragene Maschinistenausbildung (FwDV 2 Ziffer 3.3).", ma.kameradName()));
      empfehlungen.add("Sicherstellen, dass die Bedienung der Feuerlöschkreiselpumpe und Aggregate durch eine fachkundige Person überwacht wird.");
    }
  }

  private void pruefeAtemschutz(
      TaktischeEinheit einheit,
      List<FunktionsBesetzung> besetzungen,
      List<String> fehler,
      List<String> warnungen,
      List<String> empfehlungen
  ) {
    long anzahlAgt = besetzungen.stream()
        .filter(FunktionsBesetzung::istBesetzt)
        .filter(b -> b.hatQualifikation(Ausbildung.ATEMSCHUTZGERAETETRAEGER))
        .count();

    boolean atfAgt = besetzungen.stream().anyMatch(b -> b.funktion() == TaktischeFunktion.ANGRIFFSTRUPPFUEHRER && b.hatQualifikation(Ausbildung.ATEMSCHUTZGERAETETRAEGER));
    boolean atmAgt = besetzungen.stream().anyMatch(b -> b.funktion() == TaktischeFunktion.ANGRIFFSTRUPPMANN && b.hatQualifikation(Ausbildung.ATEMSCHUTZGERAETETRAEGER));
    boolean angriffstruppHatAgt = atfAgt && atmAgt;

    if (anzahlAgt == 0) {
      warnungen.add("Keine Atemschutzgeräteträger in der Mannschaft vorhanden. Innenangriff ist unzulässig (FwDV 7).");
      empfehlungen.add("Szenario als Außenangriff, Brandbekämpfung im Freien oder Wasserförderung über lange Wegstrecke konzipieren.");
    } else if (anzahlAgt < 4) {
      if (angriffstruppHatAgt) {
        warnungen.add(String.format("Es sind %d Atemschutzgeräteträger vorhanden. Für einen regulären Innenangriff nach FwDV 7 fehlt ein vollständiger Sicherheitstrupp (2 weitere AGT).", anzahlAgt));
        empfehlungen.add("Übungslage auf Außenangriff, Riegelstellung oder Personenrettung im Freien anpassen.");
      } else {
        warnungen.add("Der Angriffstrupp ist nicht vollständig mit Atemschutzgeräteträgern besetzt.");
        empfehlungen.add("Kameraden mit AGT-Lehrgang dem Angriffstrupp zuweisen oder auf Außenangriff umstellen.");
      }
    }
  }

  private void pruefeTechnischeHilfeleistung(
      List<Einsatzfahrzeug> fahrzeuge,
      List<FunktionsBesetzung> besetzungen,
      List<String> fehler,
      List<String> warnungen,
      List<String> empfehlungen
  ) {
    boolean hatHydraulikFahrzeug = fahrzeuge.stream().anyMatch(this::hatThHydraulikAusstattung);

    if (!hatHydraulikFahrzeug) {
      warnungen.add("Keines der ausgewählten Einsatzfahrzeuge verfügt über einen hydraulischen Rettungssatz (Schere/Spreizer nach DIN 14530 / HLF/RW).");
      empfehlungen.add("Szenario auf TH-Basismaßnahmen begrenzen (z. B. Notfalltüröffnung, Ausleuchtung, Verkehrsabsicherung oder Beseitigung von Sturmschäden).");
    }

    long anzahlThAusbildung = besetzungen.stream()
        .filter(FunktionsBesetzung::istBesetzt)
        .filter(b -> b.hatQualifikation(Ausbildung.TECHNISCHE_HILFELEISTUNG))
        .count();

    if (anzahlThAusbildung == 0) {
      empfehlungen.add("Keine spezifische Ausbildung 'Technische Hilfeleistung' (FwDV 2 Ziffer 3.4) erfasst. Schwerpunkt auf grundlegende Gerätekunde legen.");
    }
  }

  private void pruefeZusatzqualifikationen(
      List<FunktionsBesetzung> besetzungen,
      List<String> empfehlungen
  ) {
    boolean hatFunk = besetzungen.stream()
        .filter(b -> b.funktion() == TaktischeFunktion.MELDER || b.funktion() == TaktischeFunktion.EINHEITSFUEHRER)
        .anyMatch(b -> b.hatQualifikation(Ausbildung.SPRECHFUNKER));

    if (!hatFunk && !besetzungen.isEmpty()) {
      empfehlungen.add("Weder Einheitsführer noch Melder besitzen den Sprechfunker-Lehrgang. Funkdisziplin und Lagemeldungen besonders begleiten.");
    }
  }

  public boolean istLoeschfahrzeug(Einsatzfahrzeug fahrzeug) {
    if (fahrzeug == null || fahrzeug.getFahrzeugtyp() == null) {
      return false;
    }
    Fahrzeugtyp typ = fahrzeug.getFahrzeugtyp();
    return typ != Fahrzeugtyp.LF1000;
  }

  public boolean hatThHydraulikAusstattung(Einsatzfahrzeug fahrzeug) {
    if (fahrzeug == null || fahrzeug.getFahrzeugtyp() == null) {
      return false;
    }
    String typName = fahrzeug.getFahrzeugtyp().name().toUpperCase();
    return typName.startsWith("HLF") || typName.startsWith("RW");
  }
}
