package de.eichstaedt.ortswehrfuehrer.application.uebung;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.eichstaedt.ortswehrfuehrer.application.WehrApplicationService;
import de.eichstaedt.ortswehrfuehrer.domain.Ausbildung;
import de.eichstaedt.ortswehrfuehrer.domain.Einsatzfahrzeug;
import de.eichstaedt.ortswehrfuehrer.domain.Fahrzeugtyp;
import de.eichstaedt.ortswehrfuehrer.domain.Kamerad;
import de.eichstaedt.ortswehrfuehrer.domain.WehrFactory;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Einsatzauftrag;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.FunktionsBesetzung;
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

@DisplayName("UebungsszenarioApplicationService Tests")
class UebungsszenarioApplicationServiceTest {

  private WehrApplicationService wehrService;
  private SzenarioGuardrailService guardrailService;
  private SzenarioGeneratorService generatorService;
  private UebungsszenarioApplicationService applicationService;

  @BeforeEach
  void setUp() {
    wehrService = new WehrApplicationService(new WehrFactory());
    guardrailService = new SzenarioGuardrailService();
    SzenarioKatalog katalog = new SzenarioKatalog();
    generatorService = new SzenarioGeneratorService(katalog, guardrailService);
    applicationService = new UebungsszenarioApplicationService(wehrService, guardrailService, generatorService);
  }

  @Test
  @DisplayName("Vollständiger Lebenszyklus: Start -> Fahrzeuge -> Besetzung -> Generierung -> Freigabe")
  void vollstaendigerLebenszyklus() {
    // 1. Start
    Uebungsszenario ent = applicationService.neuesSzenarioStarten(Uebungsart.LOESCHUEBUNG, "Ortswehrführer Meier");
    assertNotNull(ent.getId());
    assertEquals(Uebungsstatus.ENTWURF, ent.getStatus());

    // 2. Fahrzeugzuweisung (TSF vorhanden in Standard-Wehr)
    Einsatzfahrzeug fahrzeug = wehrService.getAlleFahrzeuge().getFirst();
    applicationService.fahrzeugeZuweisen(ent.getId(), Set.of(fahrzeug.getId()), TaktischeEinheit.STAFFEL);

    Uebungsszenario nachFahrzeug = applicationService.getSzenario(ent.getId());
    assertEquals(TaktischeEinheit.STAFFEL, nachFahrzeug.getTaktischeEinheit());
    assertTrue(nachFahrzeug.getFahrzeugIds().contains(fahrzeug.getId()));

    // 3. Besetzung zuweisen mit Kameraden aus der Wehr
    Kamerad gfKamerad = wehrService.getAlleKameraden().stream()
        .filter(k -> k.hatAusbildung(Ausbildung.GRUPPENFUEHRER))
        .findFirst().orElseThrow();
    Kamerad agtKamerad = wehrService.getAlleKameraden().stream()
        .filter(k -> k.hatAusbildung(Ausbildung.ATEMSCHUTZGERAETETRAEGER))
        .findFirst().orElseThrow();

    // Zusätzlichen Maschinisten anlegen
    Kamerad maKamerad = wehrService.kameradHinzufuegen(Kamerad.builder()
        .vorname("Bernd")
        .nachname("Klaus")
        .mitAusbildung(Ausbildung.MASCHINIST)
        .build());

    List<FunktionsBesetzung> besetzung = List.of(
        new FunktionsBesetzung(TaktischeFunktion.EINHEITSFUEHRER, gfKamerad.getId(), null, null),
        new FunktionsBesetzung(TaktischeFunktion.MASCHINIST, maKamerad.getId(), null, null),
        new FunktionsBesetzung(TaktischeFunktion.ANGRIFFSTRUPPFUEHRER, agtKamerad.getId(), null, null),
        new FunktionsBesetzung(TaktischeFunktion.ANGRIFFSTRUPPMANN, "k-ext-1", "Kamerad Extern 1", Set.of(Ausbildung.TRUPPMANN_TEIL_1)),
        new FunktionsBesetzung(TaktischeFunktion.WASSERTRUPPFUEHRER, "k-ext-2", "Kamerad Extern 2", Set.of(Ausbildung.TRUPPMANN_TEIL_1)),
        new FunktionsBesetzung(TaktischeFunktion.WASSERTRUPPMANN, "k-ext-3", "Kamerad Extern 3", Set.of(Ausbildung.TRUPPMANN_TEIL_1))
    );

    applicationService.besetzungAktualisieren(ent.getId(), besetzung);

    Uebungsszenario nachBesetzung = applicationService.getSzenario(ent.getId());
    assertEquals(6, nachBesetzung.getBesetzungen().size());
    // Qualifikationen wurden angereichert
    assertTrue(nachBesetzung.getBesetzungen().getFirst().hatQualifikation(Ausbildung.GRUPPENFUEHRER));

    // 4. Generierung
    Uebungsszenario generiert = applicationService.generiereSzenario(ent.getId());
    assertEquals(Uebungsstatus.GENERIERT, generiert.getStatus());
    assertNotNull(generiert.getAusgangslage());
    assertFalse(generiert.getEinsatzauftraege().isEmpty());

    // 5. Abschnittsanpassung
    applicationService.anpassenAusgangslage(ent.getId(), "Manuell angepasste Ausgangslage");
    assertEquals("Manuell angepasste Ausgangslage", applicationService.getSzenario(ent.getId()).getAusgangslage());

    applicationService.anpassenEinsatzauftrag(ent.getId(), 0, new Einsatzauftrag("AT", "Brandbekämpfung", "C-Rohr", "Dach", "Leiter"));
    assertEquals("Brandbekämpfung", applicationService.getSzenario(ent.getId()).getEinsatzauftraege().getFirst().auftrag());

    // 6. Freigabe
    Uebungsszenario freigegeben = applicationService.freigeben(ent.getId());
    assertEquals(Uebungsstatus.FREIGEGEBEN, freigegeben.getStatus());

    // 7. Archivieren
    Uebungsszenario archiviert = applicationService.archivieren(ent.getId());
    assertEquals(Uebungsstatus.ARCHIVIERT, archiviert.getStatus());

    // 8. Löschen
    applicationService.loescheSzenario(ent.getId());
    assertTrue(applicationService.findeSzenario(ent.getId()).isEmpty());
  }

  @Test
  @DisplayName("Regenerierung einzelner Abschnitte über ApplicationService")
  void regeneriereAbschnitte() {
    Einsatzfahrzeug fahrzeug = wehrService.getAlleFahrzeuge().getFirst();
    Uebungsszenario ent = applicationService.neuesSzenarioStarten(Uebungsart.LOESCHUEBUNG, "Ortswehrführer");
    applicationService.fahrzeugeZuweisen(ent.getId(), Set.of(fahrzeug.getId()), TaktischeEinheit.SELBSTSTAENDIGER_TRUPP);

    Kamerad gf = wehrService.getAlleKameraden().stream().filter(k -> k.hatAusbildung(Ausbildung.GRUPPENFUEHRER)).findFirst().orElseThrow();
    Kamerad ma = wehrService.kameradHinzufuegen(Kamerad.builder().vorname("M").nachname("A").mitAusbildung(Ausbildung.MASCHINIST).build());

    applicationService.besetzungAktualisieren(ent.getId(), List.of(
        new FunktionsBesetzung(TaktischeFunktion.EINHEITSFUEHRER, gf.getId(), null, null),
        new FunktionsBesetzung(TaktischeFunktion.MASCHINIST, ma.getId(), null, null),
        new FunktionsBesetzung(TaktischeFunktion.MELDER, "m-1", "Melder", Set.of())
    ));

    applicationService.generiereSzenario(ent.getId());

    applicationService.regeneriereAbschnitt(ent.getId(), "lage");
    applicationService.regeneriereAbschnitt(ent.getId(), "auftraege");
    applicationService.regeneriereAbschnitt(ent.getId(), "sicherheit");
    applicationService.regeneriereAbschnitt(ent.getId(), "bewertung");

    Uebungsszenario sz = applicationService.getSzenario(ent.getId());
    assertFalse(sz.getLageentwicklungen().isEmpty());
    assertFalse(sz.getEinsatzauftraege().isEmpty());
    assertFalse(sz.getSicherheitshinweise().isEmpty());
    assertFalse(sz.getBewertungspunkte().isEmpty());
  }
}
