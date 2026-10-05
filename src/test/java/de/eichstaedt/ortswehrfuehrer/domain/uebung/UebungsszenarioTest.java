package de.eichstaedt.ortswehrfuehrer.domain.uebung;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.eichstaedt.ortswehrfuehrer.domain.Ausbildung;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Uebungsszenario Domain Tests")
class UebungsszenarioTest {

  @Test
  @DisplayName("Sollte neues Uebungsszenario im Status ENTWURF initialisieren")
  void initialisierungEntwurf() {
    Uebungsszenario szenario = Uebungsszenario.starten(Uebungsart.LOESCHUEBUNG, "Kamerad Müller");

    assertNotNull(szenario.getId());
    assertFalse(szenario.getId().isBlank());
    assertEquals(Uebungsstatus.ENTWURF, szenario.getStatus());
    assertEquals(Uebungsart.LOESCHUEBUNG, szenario.getArt());
    assertEquals("Kamerad Müller", szenario.getErsteller());
    assertNotNull(szenario.getParameter());
    assertEquals(TaktischeEinheit.GRUPPE, szenario.getTaktischeEinheit());
    assertTrue(szenario.getFahrzeugIds().isEmpty());
  }

  @Test
  @DisplayName("Sollte Besetzung und Fahrzeuge aktualisieren")
  void besetzungUndFahrzeugeAktualisieren() {
    Uebungsszenario szenario = Uebungsszenario.starten(Uebungsart.TECHNISCHE_HILFELEISTUNG, "Kamerad Schmidt");

    szenario.fahrzeugHinzufuegen("fahrzeug-hlf-1");
    szenario.taktischeEinheitFestlegen(TaktischeEinheit.STAFFEL);

    FunktionsBesetzung besetzung1 = new FunktionsBesetzung(
        TaktischeFunktion.EINHEITSFUEHRER,
        "k-1",
        "Max Mustermann",
        Set.of(Ausbildung.GRUPPENFUEHRER, Ausbildung.TRUPPFUEHRER)
    );
    szenario.besetzungenAktualisieren(List.of(besetzung1));

    assertTrue(szenario.getFahrzeugIds().contains("fahrzeug-hlf-1"));
    assertEquals(1, szenario.getFahrzeugIds().size());
    assertEquals(TaktischeEinheit.STAFFEL, szenario.getTaktischeEinheit());
    assertEquals(1, szenario.getBesetzungen().size());
    assertEquals(TaktischeFunktion.EINHEITSFUEHRER, szenario.getBesetzungen().getFirst().funktion());
    assertTrue(szenario.getBesetzungen().getFirst().hatQualifikation(Ausbildung.GRUPPENFUEHRER));
  }

  @Test
  @DisplayName("Sollte Szenario generieren und Status auf GENERIERT setzen")
  void szenarioGenerieren() {
    Uebungsszenario szenario = Uebungsszenario.starten(Uebungsart.LOESCHUEBUNG, "Kamerad Müller");

    Einsatzauftrag auftrag = new Einsatzauftrag(
        "Angriffstrupp",
        "zur Menschenrettung und Brandbekämpfung",
        "1. C-Rohr",
        "in das 1. Obergeschoss",
        "über den Treppenraum"
    );
    Lageentwicklung lage = new Lageentwicklung(10, "Rauchentwicklung nimmt zu", "Belüftungsmaßnahme vorbereiten");
    Sicherheitshinweis hinweis = new Sicherheitshinweis("FwDV 7", "Atemschutz", "Atemschutzüberwachung lückenlos durchführen", true);
    Bewertungspunkt punkt = new Bewertungspunkt("Einsatzbefehl", "FwDV-3-Schema eingehalten", 3);

    szenario.generieren(
        "Zimmerbrand im 1. OG",
        "B2 - Gebäudebrand",
        "Ausgedehnter Zimmerbrand im 1. OG",
        "1 Person am Fenster vermisst",
        List.of(auftrag),
        List.of(lage),
        List.of(hinweis),
        List.of(punkt)
    );

    assertEquals(Uebungsstatus.GENERIERT, szenario.getStatus());
    assertEquals("Zimmerbrand im 1. OG", szenario.getTitel());
    assertEquals("B2 - Gebäudebrand", szenario.getAlarmstichwort());
    assertEquals(1, szenario.getEinsatzauftraege().size());
    assertEquals(1, szenario.getLageentwicklungen().size());
    assertEquals(1, szenario.getSicherheitshinweise().size());
    assertEquals(1, szenario.getBewertungspunkte().size());
  }

  @Test
  @DisplayName("Sollte Freigabe nur bei fehlerfreiem Guardrail und generiertem Inhalt erlauben")
  void freigabeValidierung() {
    Uebungsszenario szenario = Uebungsszenario.starten(Uebungsart.LOESCHUEBUNG, "Kamerad Müller");

    // Nicht generiert
    assertThrows(IllegalStateException.class, szenario::freigeben);

    // Mit Fehler im GuardrailBericht
    szenario.setAusgangslage("Brand");
    szenario.guardrailBerichtAktualisieren(GuardrailPruefbericht.fehlerhaft(List.of("Keine Führungskraft"), List.of(), List.of()));
    IllegalStateException ex = assertThrows(IllegalStateException.class, szenario::freigeben);
    assertTrue(ex.getMessage().contains("Guardrail-Fehler"));

    // Fehler behoben -> Freigabe erfolgreich
    szenario.guardrailBerichtAktualisieren(GuardrailPruefbericht.erfolgreich());
    assertDoesNotThrow(szenario::freigeben);
    assertEquals(Uebungsstatus.FREIGEGEBEN, szenario.getStatus());
  }

  @Test
  @DisplayName("Sollte Einsatzbefehl nach FwDV 3 korrekt formatieren")
  void einsatzauftragBefehlFormatierung() {
    Einsatzauftrag auftrag = new Einsatzauftrag(
        "Angriffstrupp",
        "Menschenrettung und Brandbekämpfung",
        "1. C-Rohr",
        "1. Obergeschoss",
        "Treppenraum"
    );

    String befehl = auftrag.formatiereBefehl();
    assertEquals("Angriffstrupp zur Menschenrettung und Brandbekämpfung mit 1. C-Rohr nach/zu 1. Obergeschoss über Treppenraum vor!", befehl);
  }

  @Test
  @DisplayName("Sollte taktische Einheiten und deren Sollstärken korrekt abbilden")
  void taktischeEinheiten() {
    assertEquals(3, TaktischeEinheit.SELBSTSTAENDIGER_TRUPP.getSollStaerke());
    assertEquals("0/1/2/3", TaktischeEinheit.SELBSTSTAENDIGER_TRUPP.getStaerke());

    assertEquals(6, TaktischeEinheit.STAFFEL.getSollStaerke());
    assertEquals("0/1/5/6", TaktischeEinheit.STAFFEL.getStaerke());
    assertEquals(6, TaktischeEinheit.STAFFEL.getErforderlicheFunktionen().size());

    assertEquals(9, TaktischeEinheit.GRUPPE.getSollStaerke());
    assertEquals("0/1/8/9", TaktischeEinheit.GRUPPE.getStaerke());
    assertEquals(9, TaktischeEinheit.GRUPPE.getErforderlicheFunktionen().size());
  }
}
