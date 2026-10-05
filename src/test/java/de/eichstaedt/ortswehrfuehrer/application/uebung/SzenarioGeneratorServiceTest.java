package de.eichstaedt.ortswehrfuehrer.application.uebung;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.eichstaedt.ortswehrfuehrer.domain.Ausbildung;
import de.eichstaedt.ortswehrfuehrer.domain.Einsatzfahrzeug;
import de.eichstaedt.ortswehrfuehrer.domain.Fahrzeugtyp;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.FunktionsBesetzung;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Schwierigkeitsgrad;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.SzenarioParameter;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.TaktischeEinheit;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.TaktischeFunktion;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Uebungsart;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Uebungsstatus;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Uebungsszenario;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SzenarioGeneratorService Tests")
class SzenarioGeneratorServiceTest {

  private SzenarioGeneratorService generatorService;
  private SzenarioGuardrailService guardrailService;
  private SzenarioKatalog katalog;

  @BeforeEach
  void setUp() {
    guardrailService = new SzenarioGuardrailService();
    katalog = new SzenarioKatalog();
    generatorService = new SzenarioGeneratorService(katalog, guardrailService);
  }

  @Test
  @DisplayName("Golden Set 1: Generierung Löschübung TSF-W mit Staffel (2 AGT) -> Generiert Außenangriff mit Riegelstellung")
  void goldenSet1_GenerierungLoeschuebungTsffWStaffel() {
    Einsatzfahrzeug tsfw = Einsatzfahrzeug.builder()
        .id("fz-tsfw")
        .bezeichnung("TSF-W Göttlin")
        .kennung("Florian Havelland 08/48-01")
        .fahrzeugtyp(Fahrzeugtyp.TSF_W)
        .build();

    Uebungsszenario szenario = Uebungsszenario.starten(Uebungsart.LOESCHUEBUNG, "Ortswehrführer");
    szenario.fahrzeugHinzufuegen(tsfw.getId());
    szenario.taktischeEinheitFestlegen(TaktischeEinheit.STAFFEL);

    List<FunktionsBesetzung> besetzungen = List.of(
        new FunktionsBesetzung(TaktischeFunktion.EINHEITSFUEHRER, "k-1", "Kamerad 1", Set.of(Ausbildung.GRUPPENFUEHRER, Ausbildung.SPRECHFUNKER)),
        new FunktionsBesetzung(TaktischeFunktion.MASCHINIST, "k-2", "Kamerad 2", Set.of(Ausbildung.MASCHINIST)),
        new FunktionsBesetzung(TaktischeFunktion.ANGRIFFSTRUPPFUEHRER, "k-3", "Kamerad 3", Set.of(Ausbildung.ATEMSCHUTZGERAETETRAEGER, Ausbildung.TRUPPMANN_TEIL_1)),
        new FunktionsBesetzung(TaktischeFunktion.ANGRIFFSTRUPPMANN, "k-4", "Kamerad 4", Set.of(Ausbildung.ATEMSCHUTZGERAETETRAEGER, Ausbildung.TRUPPMANN_TEIL_1)),
        new FunktionsBesetzung(TaktischeFunktion.WASSERTRUPPFUEHRER, "k-5", "Kamerad 5", Set.of(Ausbildung.TRUPPMANN_TEIL_1)),
        new FunktionsBesetzung(TaktischeFunktion.WASSERTRUPPMANN, "k-6", "Kamerad 6", Set.of(Ausbildung.TRUPPMANN_TEIL_1))
    );
    szenario.besetzungenAktualisieren(besetzungen);

    Uebungsszenario generiert = generatorService.generiereSzenario(szenario, List.of(tsfw));

    assertEquals(Uebungsstatus.GENERIERT, generiert.getStatus());
    assertNotNull(generiert.getAusgangslage());
    assertTrue(generiert.getAusgangslage().contains("Schuppen") || generiert.getAusgangslage().contains("Außenangriff"));
    assertFalse(generiert.getEinsatzauftraege().isEmpty());
    assertTrue(generiert.getEinsatzauftraege().stream().anyMatch(a -> a.auftrag().contains("Riegelstellung")));
    assertTrue(generiert.getLageentwicklungen().size() >= 2);
    assertTrue(generiert.getSicherheitshinweise().stream().anyMatch(s -> s.vorschrift().contains("UVV") || s.vorschrift().contains("FwDV")));
  }

  @Test
  @DisplayName("Golden Set 2: Generierung TH-Übung HLF mit Gruppe -> Generiert VU mit eingeklemmter Person & Rettungssatz")
  void goldenSet2_GenerierungThUebungHlfGruppe() {
    Einsatzfahrzeug hlf = Einsatzfahrzeug.builder()
        .id("fz-hlf")
        .bezeichnung("HLF 20 Rathenow")
        .kennung("Florian Havelland 01/46-01")
        .fahrzeugtyp(Fahrzeugtyp.HLF20)
        .build();

    Uebungsszenario szenario = Uebungsszenario.starten(Uebungsart.TECHNISCHE_HILFELEISTUNG, "Ortswehrführer");
    szenario.fahrzeugHinzufuegen(hlf.getId());
    szenario.taktischeEinheitFestlegen(TaktischeEinheit.GRUPPE);

    List<FunktionsBesetzung> besetzungen = List.of(
        new FunktionsBesetzung(TaktischeFunktion.EINHEITSFUEHRER, "k-1", "GF Meyer", Set.of(Ausbildung.GRUPPENFUEHRER, Ausbildung.SPRECHFUNKER)),
        new FunktionsBesetzung(TaktischeFunktion.MASCHINIST, "k-2", "Ma Schulze", Set.of(Ausbildung.MASCHINIST)),
        new FunktionsBesetzung(TaktischeFunktion.MELDER, "k-3", "Me Schmidt", Set.of(Ausbildung.SPRECHFUNKER, Ausbildung.TRUPPMANN_TEIL_1)),
        new FunktionsBesetzung(TaktischeFunktion.ANGRIFFSTRUPPFUEHRER, "k-4", "ATF Lehmann", Set.of(Ausbildung.TRUPPFUEHRER, Ausbildung.TECHNISCHE_HILFELEISTUNG)),
        new FunktionsBesetzung(TaktischeFunktion.ANGRIFFSTRUPPMANN, "k-5", "ATM Becker", Set.of(Ausbildung.TRUPPMANN_TEIL_1)),
        new FunktionsBesetzung(TaktischeFunktion.WASSERTRUPPFUEHRER, "k-6", "WTF Weber", Set.of(Ausbildung.TRUPPFUEHRER)),
        new FunktionsBesetzung(TaktischeFunktion.WASSERTRUPPMANN, "k-7", "WTM Hoffmann", Set.of(Ausbildung.TRUPPMANN_TEIL_1)),
        new FunktionsBesetzung(TaktischeFunktion.SCHLAUCHTRUPPFUEHRER, "k-8", "STF Wagner", Set.of(Ausbildung.TRUPPFUEHRER)),
        new FunktionsBesetzung(TaktischeFunktion.SCHLAUCHTRUPPMANN, "k-9", "STM Richter", Set.of(Ausbildung.TRUPPMANN_TEIL_1))
    );
    szenario.besetzungenAktualisieren(besetzungen);

    Uebungsszenario generiert = generatorService.generiereSzenario(szenario, List.of(hlf));

    assertEquals(Uebungsstatus.GENERIERT, generiert.getStatus());
    assertTrue(generiert.getTitel().contains("Verkehrsunfall"));
    assertTrue(generiert.getEinsatzauftraege().stream().anyMatch(a -> a.auftrag().contains("Personenbetreuung") || a.mittel().contains("Glasmanagement")));
    assertTrue(generiert.getEinsatzauftraege().stream().anyMatch(a -> a.auftrag().contains("Brandschutz")));
  }

  @Test
  @DisplayName("Löschübung mit 4 AGT -> Generiert Innenangriff mit Atemschutz-Sicherheitstrupp")
  void loeschuebungMitVollAtemschutz() {
    Einsatzfahrzeug hlf = Einsatzfahrzeug.builder()
        .id("fz-hlf")
        .bezeichnung("HLF 20 Rathenow")
        .fahrzeugtyp(Fahrzeugtyp.HLF20)
        .build();

    Uebungsszenario szenario = Uebungsszenario.starten(Uebungsart.LOESCHUEBUNG, "Ortswehrführer");
    szenario.fahrzeugHinzufuegen(hlf.getId());
    szenario.taktischeEinheitFestlegen(TaktischeEinheit.STAFFEL);

    List<FunktionsBesetzung> besetzungen = List.of(
        new FunktionsBesetzung(TaktischeFunktion.EINHEITSFUEHRER, "k-1", "GF Meyer", Set.of(Ausbildung.GRUPPENFUEHRER)),
        new FunktionsBesetzung(TaktischeFunktion.MASCHINIST, "k-2", "Ma Schulze", Set.of(Ausbildung.MASCHINIST)),
        new FunktionsBesetzung(TaktischeFunktion.ANGRIFFSTRUPPFUEHRER, "k-3", "ATF Lehmann", Set.of(Ausbildung.ATEMSCHUTZGERAETETRAEGER, Ausbildung.TRUPPFUEHRER)),
        new FunktionsBesetzung(TaktischeFunktion.ANGRIFFSTRUPPMANN, "k-4", "ATM Becker", Set.of(Ausbildung.ATEMSCHUTZGERAETETRAEGER)),
        new FunktionsBesetzung(TaktischeFunktion.WASSERTRUPPFUEHRER, "k-5", "WTF Weber", Set.of(Ausbildung.ATEMSCHUTZGERAETETRAEGER)),
        new FunktionsBesetzung(TaktischeFunktion.WASSERTRUPPMANN, "k-6", "WTM Hoffmann", Set.of(Ausbildung.ATEMSCHUTZGERAETETRAEGER))
    );
    szenario.besetzungenAktualisieren(besetzungen);

    Uebungsszenario generiert = generatorService.generiereSzenario(szenario, List.of(hlf));

    assertTrue(generiert.getAusgangslage().contains("Zimmerbrand") || generiert.getAusgangslage().contains("Menschenrettung"));
    assertTrue(generiert.getEinsatzauftraege().stream().anyMatch(a -> a.auftrag().contains("Sicherheitstrupp")));
  }

  @Test
  @DisplayName("Regeneration einzelner Abschnitte funktioniert fehlerfrei")
  void regenerationAbschnitte() {
    Einsatzfahrzeug tsfw = Einsatzfahrzeug.builder().id("tsfw").fahrzeugtyp(Fahrzeugtyp.TSF_W).build();
    Uebungsszenario szenario = Uebungsszenario.starten(Uebungsart.LOESCHUEBUNG, "Ortswehrführer");
    szenario.fahrzeugHinzufuegen(tsfw.getId());
    szenario.besetzungenAktualisieren(List.of(
        new FunktionsBesetzung(TaktischeFunktion.EINHEITSFUEHRER, "k-1", "GF", Set.of(Ausbildung.GRUPPENFUEHRER)),
        new FunktionsBesetzung(TaktischeFunktion.MASCHINIST, "k-2", "Ma", Set.of(Ausbildung.MASCHINIST)),
        new FunktionsBesetzung(TaktischeFunktion.MELDER, "k-3", "Me", Set.of(Ausbildung.TRUPPMANN_TEIL_1))
    ));
    szenario.taktischeEinheitFestlegen(TaktischeEinheit.SELBSTSTAENDIGER_TRUPP);

    generatorService.generiereSzenario(szenario, List.of(tsfw));

    int anzahlLage = szenario.getLageentwicklungen().size();
    generatorService.regeneriereLageentwicklungen(szenario, List.of(tsfw));
    assertEquals(anzahlLage, szenario.getLageentwicklungen().size());

    generatorService.regeneriereEinsatzauftraege(szenario, List.of(tsfw));
    assertFalse(szenario.getEinsatzauftraege().isEmpty());
  }

  @Test
  @DisplayName("Generierung mit bestehenden Guardrail-Fehlern wirft Exception")
  void fehlerBeiUngueltigenGuardrails() {
    Uebungsszenario szenario = Uebungsszenario.starten(Uebungsart.LOESCHUEBUNG, "Ortswehrführer");
    // Keine Fahrzeuge und keine Besetzung
    assertThrows(IllegalStateException.class, () -> generatorService.generiereSzenario(szenario, List.of()));
  }
}
