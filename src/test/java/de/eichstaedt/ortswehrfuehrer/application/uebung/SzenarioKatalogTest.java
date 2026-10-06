package de.eichstaedt.ortswehrfuehrer.application.uebung;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.eichstaedt.ortswehrfuehrer.domain.Ausbildung;
import de.eichstaedt.ortswehrfuehrer.domain.Einsatzfahrzeug;
import de.eichstaedt.ortswehrfuehrer.domain.Fahrzeugtyp;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.FunktionsBesetzung;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.Schwierigkeitsgrad;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.SzenarioParameter;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.TaktischeEinheit;
import de.eichstaedt.ortswehrfuehrer.domain.uebung.TaktischeFunktion;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SzenarioKatalog JSON Tests")
class SzenarioKatalogTest {

  private SzenarioKatalog katalog;

  @BeforeEach
  void setUp() {
    katalog = new SzenarioKatalog();
  }

  @Test
  @DisplayName("SzenarioKatalog lädt Vorlagen aus Standard-JSON-Datei")
  void laedtVorlagenAusJson() {
    assertNotNull(katalog.getVorlagen());
    assertEquals(4, katalog.getVorlagen().size());
    assertTrue(katalog.getVorlagen().containsKey("LOESCHUEBUNG_INNENANGRIFF"));
    assertTrue(katalog.getVorlagen().containsKey("LOESCHUEBUNG_AUSSENANGRIFF"));
    assertTrue(katalog.getVorlagen().containsKey("TH_VU_EINGEKLEMMT"));
    assertTrue(katalog.getVorlagen().containsKey("TH_BASISMASSNAHMEN"));
  }

  @Test
  @DisplayName("Einzelne Vorlage per ID abrufen")
  void getVorlagePerId() {
    Optional<SzenarioKatalog.SzenarioVorlage> vorlage = katalog.getVorlage("LOESCHUEBUNG_INNENANGRIFF");
    assertTrue(vorlage.isPresent());
    assertEquals("LOESCHUEBUNG", vorlage.get().art());
    assertNotNull(vorlage.get().auftraege());
    assertFalse(vorlage.get().auftraege().isEmpty());
    assertNotNull(vorlage.get().sicherheitshinweise());
    assertNotNull(vorlage.get().bewertungspunkte());
  }

  @Test
  @DisplayName("Löschübung mit 4 AGT erzeugt Innenangriff")
  void erstelleLoeschuebungMitInnenangriff() {
    List<FunktionsBesetzung> besetzungen = List.of(
        new FunktionsBesetzung(TaktischeFunktion.EINHEITSFUEHRER, "1", "GF", Set.of(Ausbildung.GRUPPENFUEHRER)),
        new FunktionsBesetzung(TaktischeFunktion.MASCHINIST, "2", "Ma", Set.of(Ausbildung.MASCHINIST)),
        new FunktionsBesetzung(TaktischeFunktion.ANGRIFFSTRUPPFUEHRER, "3", "ATF", Set.of(Ausbildung.ATEMSCHUTZGERAETETRAEGER)),
        new FunktionsBesetzung(TaktischeFunktion.ANGRIFFSTRUPPMANN, "4", "ATM", Set.of(Ausbildung.ATEMSCHUTZGERAETETRAEGER)),
        new FunktionsBesetzung(TaktischeFunktion.WASSERTRUPPFUEHRER, "5", "WTF", Set.of(Ausbildung.ATEMSCHUTZGERAETETRAEGER)),
        new FunktionsBesetzung(TaktischeFunktion.WASSERTRUPPMANN, "6", "WTM", Set.of(Ausbildung.ATEMSCHUTZGERAETETRAEGER))
    );
    Einsatzfahrzeug hlf = Einsatzfahrzeug.builder().id("1").bezeichnung("HLF 20").fahrzeugtyp(Fahrzeugtyp.HLF20).build();
    SzenarioParameter parameter = new SzenarioParameter(90, Schwierigkeitsgrad.ANSPRUCHSVOLL, null, "Hauptstraße 1", null);

    SzenarioKatalog.VorlagenPaket paket = katalog.erstelleLoeschuebung(
        TaktischeEinheit.GRUPPE,
        besetzungen,
        List.of(hlf),
        parameter,
        null
    );

    assertNotNull(paket);
    assertTrue(paket.titel().contains("Hauptstraße 1"));
    assertTrue(paket.ausgangslage().contains("Hauptstraße 1"));
    assertEquals("B2 - Gebäudebrand / Zimmerbrand", paket.alarmstichwort());
    // Gruppe includes Schlauchtrupp and Melder
    assertTrue(paket.auftraege().stream().anyMatch(a -> a.einheit().equals("Schlauchtrupp")));
    assertTrue(paket.auftraege().stream().anyMatch(a -> a.einheit().equals("Melder")));
    // Anspruchsvoll includes 3 Lageentwicklungen
    assertEquals(3, paket.lageentwicklungen().size());
  }

  @Test
  @DisplayName("Löschübung mit weniger als 4 AGT erzeugt Außenangriff")
  void erstelleLoeschuebungMitAussenangriff() {
    List<FunktionsBesetzung> besetzungen = List.of(
        new FunktionsBesetzung(TaktischeFunktion.EINHEITSFUEHRER, "1", "GF", Set.of(Ausbildung.GRUPPENFUEHRER)),
        new FunktionsBesetzung(TaktischeFunktion.MASCHINIST, "2", "Ma", Set.of(Ausbildung.MASCHINIST)),
        new FunktionsBesetzung(TaktischeFunktion.ANGRIFFSTRUPPFUEHRER, "3", "ATF", Set.of(Ausbildung.ATEMSCHUTZGERAETETRAEGER)),
        new FunktionsBesetzung(TaktischeFunktion.ANGRIFFSTRUPPMANN, "4", "ATM", Set.of(Ausbildung.ATEMSCHUTZGERAETETRAEGER))
    );
    Einsatzfahrzeug tsfw = Einsatzfahrzeug.builder().id("1").bezeichnung("TSF-W").fahrzeugtyp(Fahrzeugtyp.TSF_W).build();
    SzenarioParameter parameter = new SzenarioParameter(90, Schwierigkeitsgrad.MITTEL, null, "Feldweg 2", null);

    SzenarioKatalog.VorlagenPaket paket = katalog.erstelleLoeschuebung(
        TaktischeEinheit.STAFFEL,
        besetzungen,
        List.of(tsfw),
        parameter,
        null
    );

    assertNotNull(paket);
    assertTrue(paket.titel().contains("Feldweg 2"));
    assertEquals("B2 - Schuppenbrand / Außenangriff", paket.alarmstichwort());
    // Staffel does not include Schlauchtrupp
    assertFalse(paket.auftraege().stream().anyMatch(a -> a.einheit().equals("Schlauchtrupp")));
    // Standard difficulty has 2 Lageentwicklungen
    assertEquals(2, paket.lageentwicklungen().size());
  }

  @Test
  @DisplayName("TH-Übung mit HLF erzeugt VU mit eingeklemmter Person")
  void erstelleThMitHlf() {
    Einsatzfahrzeug hlf = Einsatzfahrzeug.builder().id("1").bezeichnung("HLF 10").fahrzeugtyp(Fahrzeugtyp.HLF10).build();
    SzenarioParameter parameter = new SzenarioParameter(90, Schwierigkeitsgrad.MITTEL, null, "B5 Ortsumgehung", null);

    SzenarioKatalog.VorlagenPaket paket = katalog.erstelleTechnischeHilfeleistung(
        TaktischeEinheit.GRUPPE,
        List.of(),
        List.of(hlf),
        parameter,
        null
    );

    assertNotNull(paket);
    assertTrue(paket.titel().contains("Verkehrsunfall"));
    assertTrue(paket.titel().contains("B5 Ortsumgehung"));
    assertEquals("H:Hilfeleistung-Klemm / VU mit P-klemmt", paket.alarmstichwort());
  }

  @Test
  @DisplayName("TH-Übung ohne HLF erzeugt Basismaßnahmen (Sturmschaden)")
  void erstelleThOhneHlf() {
    Einsatzfahrzeug lf = Einsatzfahrzeug.builder().id("1").bezeichnung("LF 8/6").fahrzeugtyp(Fahrzeugtyp.LF10).build();
    SzenarioParameter parameter = new SzenarioParameter(90, Schwierigkeitsgrad.MITTEL, null, "Dorfplatz", null);

    SzenarioKatalog.VorlagenPaket paket = katalog.erstelleTechnischeHilfeleistung(
        TaktischeEinheit.STAFFEL,
        List.of(),
        List.of(lf),
        parameter,
        null
    );

    assertNotNull(paket);
    assertTrue(paket.titel().contains("Sturmschaden"));
    assertEquals("H:Hilfeleistung-Natur / Baum auf Straße", paket.alarmstichwort());
  }

  @Test
  @DisplayName("Fehler bei ungültiger Ressourcen-Datei")
  void fehlerBeiNichtVorhandenerDatei() {
    assertThrows(IllegalStateException.class, () -> new SzenarioKatalog(new ObjectMapper(), "/ungueltiger-pfad.json"));
  }

  @Test
  @DisplayName("Fehler bei unbekannter Vorlagen-ID")
  void fehlerBeiUnbekannterVorlageId() {
    assertThrows(IllegalArgumentException.class, () -> katalog.erstelleAusVorlage("NICHT_EXISTENT", TaktischeEinheit.GRUPPE, "Ort", "Fahrzeug", Schwierigkeitsgrad.MITTEL));
  }
}
