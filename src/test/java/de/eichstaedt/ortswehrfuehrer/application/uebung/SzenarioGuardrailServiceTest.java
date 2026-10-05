package de.eichstaedt.ortswehrfuehrer.application.uebung;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.eichstaedt.ortswehrfuehrer.domain.Ausbildung;
import de.eichstaedt.ortswehrfuehrer.domain.Einsatzfahrzeug;
import de.eichstaedt.ortswehrfuehrer.domain.Fahrzeugtyp;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.FunktionsBesetzung;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.GuardrailPruefbericht;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.SzenarioParameter;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.TaktischeEinheit;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.TaktischeFunktion;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Uebungsart;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SzenarioGuardrailService Tests")
class SzenarioGuardrailServiceTest {

  private SzenarioGuardrailService guardrailService;

  @BeforeEach
  void setUp() {
    guardrailService = new SzenarioGuardrailService();
  }

  @Test
  @DisplayName("Golden Set 1: Löschübung TSF-W mit Staffel (6 Kräfte, 2 AGT) -> Warnung bzgl. Innenangriff / Sicherheitstrupp")
  void goldenSet1_LoeschuebungTsffWStaffelWenigAgt() {
    Einsatzfahrzeug tsfw = Einsatzfahrzeug.builder()
        .id("fz-tsfw")
        .bezeichnung("TSF-W Göttlin")
        .kennung("Florian Havelland 08/48-01")
        .fahrzeugtyp(Fahrzeugtyp.TSF_W)
        .build();

    List<FunktionsBesetzung> besetzungen = List.of(
        new FunktionsBesetzung(TaktischeFunktion.EINHEITSFUEHRER, "k-1", "Kamerad 1", Set.of(Ausbildung.GRUPPENFUEHRER, Ausbildung.SPRECHFUNKER)),
        new FunktionsBesetzung(TaktischeFunktion.MASCHINIST, "k-2", "Kamerad 2", Set.of(Ausbildung.MASCHINIST)),
        new FunktionsBesetzung(TaktischeFunktion.ANGRIFFSTRUPPFUEHRER, "k-3", "Kamerad 3", Set.of(Ausbildung.ATEMSCHUTZGERAETETRAEGER, Ausbildung.TRUPPMANN_TEIL_1)),
        new FunktionsBesetzung(TaktischeFunktion.ANGRIFFSTRUPPMANN, "k-4", "Kamerad 4", Set.of(Ausbildung.ATEMSCHUTZGERAETETRAEGER, Ausbildung.TRUPPMANN_TEIL_1)),
        new FunktionsBesetzung(TaktischeFunktion.WASSERTRUPPFUEHRER, "k-5", "Kamerad 5", Set.of(Ausbildung.TRUPPMANN_TEIL_1)),
        new FunktionsBesetzung(TaktischeFunktion.WASSERTRUPPMANN, "k-6", "Kamerad 6", Set.of(Ausbildung.TRUPPMANN_TEIL_1))
    );

    GuardrailPruefbericht bericht = guardrailService.pruefe(
        Uebungsart.LOESCHUEBUNG,
        TaktischeEinheit.STAFFEL,
        besetzungen,
        List.of(tsfw),
        SzenarioParameter.standard()
    );

    assertTrue(bericht.gueltig(), "Szenario muss grundsätzlich gültig sein");
    assertFalse(bericht.hatFehler(), "Darf keine harten Fehler enthalten");
    assertTrue(bericht.hatWarnungen(), "Muss Warnung wegen fehlendem Sicherheitstrupp enthalten");
    assertTrue(bericht.warnungen().stream().anyMatch(w -> w.contains("Sicherheitstrupp") || w.contains("Atemschutzgeräteträger")));
    assertTrue(bericht.empfehlungen().stream().anyMatch(e -> e.contains("Außenangriff") || e.contains("Riegelstellung")));
  }

  @Test
  @DisplayName("Golden Set 2: TH-Übung HLF mit Gruppe (9 Kräfte, voll qualifiziert) -> Gültig ohne Warnungen")
  void goldenSet2_ThUebungHlfGruppeVollqualifiziert() {
    Einsatzfahrzeug hlf = Einsatzfahrzeug.builder()
        .id("fz-hlf")
        .bezeichnung("HLF 20 Rathenow")
        .kennung("Florian Havelland 01/46-01")
        .fahrzeugtyp(Fahrzeugtyp.HLF20)
        .build();

    List<FunktionsBesetzung> besetzungen = List.of(
        new FunktionsBesetzung(TaktischeFunktion.EINHEITSFUEHRER, "k-1", "GF Meyer", Set.of(Ausbildung.GRUPPENFUEHRER, Ausbildung.SPRECHFUNKER, Ausbildung.TECHNISCHE_HILFELEISTUNG)),
        new FunktionsBesetzung(TaktischeFunktion.MASCHINIST, "k-2", "Ma Schulze", Set.of(Ausbildung.MASCHINIST, Ausbildung.SPRECHFUNKER)),
        new FunktionsBesetzung(TaktischeFunktion.MELDER, "k-3", "Me Schmidt", Set.of(Ausbildung.SPRECHFUNKER, Ausbildung.TRUPPMANN_TEIL_1)),
        new FunktionsBesetzung(TaktischeFunktion.ANGRIFFSTRUPPFUEHRER, "k-4", "ATF Lehmann", Set.of(Ausbildung.TRUPPFUEHRER, Ausbildung.TECHNISCHE_HILFELEISTUNG)),
        new FunktionsBesetzung(TaktischeFunktion.ANGRIFFSTRUPPMANN, "k-5", "ATM Becker", Set.of(Ausbildung.TRUPPMANN_TEIL_1, Ausbildung.TECHNISCHE_HILFELEISTUNG)),
        new FunktionsBesetzung(TaktischeFunktion.WASSERTRUPPFUEHRER, "k-6", "WTF Weber", Set.of(Ausbildung.TRUPPFUEHRER, Ausbildung.TECHNISCHE_HILFELEISTUNG)),
        new FunktionsBesetzung(TaktischeFunktion.WASSERTRUPPMANN, "k-7", "WTM Hoffmann", Set.of(Ausbildung.TRUPPMANN_TEIL_1)),
        new FunktionsBesetzung(TaktischeFunktion.SCHLAUCHTRUPPFUEHRER, "k-8", "STF Wagner", Set.of(Ausbildung.TRUPPFUEHRER)),
        new FunktionsBesetzung(TaktischeFunktion.SCHLAUCHTRUPPMANN, "k-9", "STM Richter", Set.of(Ausbildung.TRUPPMANN_TEIL_1))
    );

    GuardrailPruefbericht bericht = guardrailService.pruefe(
        Uebungsart.TECHNISCHE_HILFELEISTUNG,
        TaktischeEinheit.GRUPPE,
        besetzungen,
        List.of(hlf),
        SzenarioParameter.standard()
    );

    assertTrue(bericht.gueltig());
    assertFalse(bericht.hatFehler());
    assertFalse(bericht.hatWarnungen());
  }

  @Test
  @DisplayName("Golden Set 4: Zu wenig Personal (<3) oder fehlende Führungsqualifikation -> Harter Fehler")
  void goldenSet4_ZuWenigPersonalOderFehlendeFuehrung() {
    Einsatzfahrzeug hlf = Einsatzfahrzeug.builder()
        .id("fz-hlf")
        .bezeichnung("HLF 20 Rathenow")
        .kennung("Florian Havelland 01/46-01")
        .fahrzeugtyp(Fahrzeugtyp.HLF20)
        .build();

    // Fall A: Nur 2 Kräfte zugewiesen (< 3)
    List<FunktionsBesetzung> besetzung2Kraefte = List.of(
        new FunktionsBesetzung(TaktischeFunktion.EINHEITSFUEHRER, "k-1", "GF Meyer", Set.of(Ausbildung.GRUPPENFUEHRER)),
        new FunktionsBesetzung(TaktischeFunktion.MASCHINIST, "k-2", "Ma Schulze", Set.of(Ausbildung.MASCHINIST))
    );

    GuardrailPruefbericht berichtA = guardrailService.pruefe(
        Uebungsart.LOESCHUEBUNG,
        TaktischeEinheit.GRUPPE,
        besetzung2Kraefte,
        List.of(hlf),
        SzenarioParameter.standard()
    );

    assertFalse(berichtA.gueltig());
    assertTrue(berichtA.hatFehler());
    assertTrue(berichtA.fehler().stream().anyMatch(f -> f.contains("Mindeststärke") || f.contains("Einsatzkräften")));

    // Fall B: Einheitsführer ohne Führungsausbildung
    List<FunktionsBesetzung> besetzungOhneFuehrung = List.of(
        new FunktionsBesetzung(TaktischeFunktion.EINHEITSFUEHRER, "k-1", "Kamerad Meyer", Set.of(Ausbildung.TRUPPMANN_TEIL_1)),
        new FunktionsBesetzung(TaktischeFunktion.MASCHINIST, "k-2", "Ma Schulze", Set.of(Ausbildung.MASCHINIST)),
        new FunktionsBesetzung(TaktischeFunktion.MELDER, "k-3", "Me Schmidt", Set.of(Ausbildung.TRUPPMANN_TEIL_1))
    );

    GuardrailPruefbericht berichtB = guardrailService.pruefe(
        Uebungsart.LOESCHUEBUNG,
        TaktischeEinheit.SELBSTSTAENDIGER_TRUPP,
        besetzungOhneFuehrung,
        List.of(hlf),
        SzenarioParameter.standard()
    );

    assertFalse(berichtB.gueltig());
    assertTrue(berichtB.hatFehler());
    assertTrue(berichtB.fehler().stream().anyMatch(f -> f.contains("Führungsausbildung")));
  }

  @Test
  @DisplayName("TH-Übung mit Fahrzeug ohne Hydraulikaggregat -> Warnung über fehlenden Rettungssatz")
  void thUebungOhneHydraulikFahrzeug() {
    Einsatzfahrzeug tsf = Einsatzfahrzeug.builder()
        .id("fz-tsf")
        .bezeichnung("TSF Göttlin")
        .kennung("Florian Havelland 08/47-01")
        .fahrzeugtyp(Fahrzeugtyp.TSF)
        .build();

    List<FunktionsBesetzung> besetzungen = List.of(
        new FunktionsBesetzung(TaktischeFunktion.EINHEITSFUEHRER, "k-1", "GF Meyer", Set.of(Ausbildung.GRUPPENFUEHRER)),
        new FunktionsBesetzung(TaktischeFunktion.MASCHINIST, "k-2", "Ma Schulze", Set.of(Ausbildung.MASCHINIST)),
        new FunktionsBesetzung(TaktischeFunktion.MELDER, "k-3", "Me Schmidt", Set.of(Ausbildung.TRUPPMANN_TEIL_1))
    );

    GuardrailPruefbericht bericht = guardrailService.pruefe(
        Uebungsart.TECHNISCHE_HILFELEISTUNG,
        TaktischeEinheit.SELBSTSTAENDIGER_TRUPP,
        besetzungen,
        List.of(tsf),
        SzenarioParameter.standard()
    );

    assertTrue(bericht.gueltig());
    assertTrue(bericht.hatWarnungen());
    assertTrue(bericht.warnungen().stream().anyMatch(w -> w.contains("hydraulischen Rettungssatz")));
  }

  @Test
  @DisplayName("Prüfung ohne Fahrzeugauswahl -> Harter Fehler")
  void pruefungOhneFahrzeuge() {
    GuardrailPruefbericht bericht = guardrailService.pruefe(
        Uebungsart.LOESCHUEBUNG,
        TaktischeEinheit.GRUPPE,
        List.of(),
        List.of(),
        SzenarioParameter.standard()
    );

    assertFalse(bericht.gueltig());
    assertTrue(bericht.hatFehler());
    assertTrue(bericht.fehler().stream().anyMatch(f -> f.contains("Einsatzfahrzeug")));
  }
}
