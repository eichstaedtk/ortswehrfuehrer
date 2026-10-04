package de.eichstaedt.ortswehrfuehrer.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.eichstaedt.ortswehrfuehrer.domain.Adresse;
import de.eichstaedt.ortswehrfuehrer.domain.Einsatzfahrzeug;
import de.eichstaedt.ortswehrfuehrer.domain.Fahrzeugtyp;
import de.eichstaedt.ortswehrfuehrer.domain.Kamerad;
import de.eichstaedt.ortswehrfuehrer.domain.Wehr;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WehrApplicationServiceTest {

  private WehrApplicationService service;

  @BeforeEach
  void setUp() {
    service = new WehrApplicationService();
  }

  @Test
  @DisplayName("Service initialisiert standardmäßig eine aktive Wehr mit 3 Kameraden und 2 Einsatzfahrzeugen")
  void testInitialisiereStandardWehr() {
    Wehr wehr = service.getAktiveWehr();
    assertNotNull(wehr);
    assertEquals("Freiwillige Feuerwehr Musterstadt", wehr.getName());
    assertEquals(3, service.getAlleKameraden().size());

    List<Einsatzfahrzeug> fahrzeuge = service.getFahrzeugeDerAktivenWehr();
    assertEquals(2, fahrzeuge.size());
    assertTrue(fahrzeuge.stream().anyMatch(f -> f.getFahrzeugtyp() == Fahrzeugtyp.TSF_W && "Florian Musterstadt 1/48-1".equals(f.getKennung())));
    assertTrue(fahrzeuge.stream().anyMatch(f -> f.getFahrzeugtyp() == Fahrzeugtyp.HLF10 && "Florian Musterstadt 1/43-1".equals(f.getKennung())));
  }

  @Test
  @DisplayName("Kamerad über Objekt hinzufügen und automatisch der Abteilung zuweisen")
  void testKameradHinzufuegenObjekt() {
    Kamerad jugend = Kamerad.builder()
        .vorname("Tim")
        .nachname("Müller")
        .geburtsdatum(LocalDate.now().minusYears(14))
        .build();

    service.kameradHinzufuegen(jugend);

    Set<Kamerad> alle = service.getAlleKameraden();
    assertTrue(alle.contains(jugend));
    assertEquals("Jugendabteilung", service.ermittleAbteilungName(jugend));
  }

  @Test
  @DisplayName("Kamerad über Parameter-Methode hinzufügen")
  void testKameradHinzufuegenParameter() {
    Adresse adresse = new Adresse("Dorfstraße", "10", "12345", "Musterstadt");
    Kamerad neu = service.kameradHinzufuegen(
        "Klaus",
        "Schulz",
        LocalDate.now().minusYears(70),
        adresse,
        "0172 9876543",
        "klaus.schulz@feuerwehr.de"
    );

    assertNotNull(neu);
    assertEquals("Klaus", neu.getVorname());
    assertEquals("Alters- und Ehrenabteilung", service.ermittleAbteilungName(neu));
  }

  @Test
  @DisplayName("Hinzufügen von null-Kamerad wirft IllegalArgumentException")
  void testKameradNullWirftException() {
    assertThrows(IllegalArgumentException.class, () -> service.kameradHinzufuegen(null));
  }

  @Test
  @DisplayName("Abteilungsname für Einsatzabteilung und unbekannte Kameraden korrekt ermitteln")
  void testErmittleAbteilungName() {
    Kamerad einsatz = Kamerad.builder()
        .vorname("Erika")
        .nachname("Musterfrau")
        .geburtsdatum(LocalDate.now().minusYears(30))
        .build();
    service.kameradHinzufuegen(einsatz);

    assertEquals("Einsatzabteilung", service.ermittleAbteilungName(einsatz));

    Kamerad unbekannt = Kamerad.builder().vorname("Fremd").nachname("User").build();
    assertEquals("Nicht zugeordnet", service.ermittleAbteilungName(unbekannt));
    assertEquals("Unbekannt", service.ermittleAbteilungName(null));
  }

  @Test
  @DisplayName("Einsatzfahrzeug hinzufügen und verwalten")
  void testFahrzeugHinzufuegen() {
    Einsatzfahrzeug tlf = Einsatzfahrzeug.builder()
        .id("fz-tlf-03")
        .bezeichnung("Tanklöschfahrzeug 3000")
        .kennung("Florian Musterstadt 1/24-1")
        .fahrzeugtyp(Fahrzeugtyp.TLF3000)
        .build();

    service.fahrzeugHinzufuegen(tlf);

    List<Einsatzfahrzeug> fahrzeuge = service.getFahrzeugeDerAktivenWehr();
    assertEquals(3, fahrzeuge.size());
    assertTrue(fahrzeuge.contains(tlf));
    assertTrue(service.getAlleFahrzeuge().contains(tlf));

    Einsatzfahrzeug lf20 = service.fahrzeugHinzufuegen("Florian Musterstadt 1/44-1", Fahrzeugtyp.LF20, "Löschgruppenfahrzeug 20");
    assertNotNull(lf20);
    assertEquals("Florian Musterstadt 1/44-1", lf20.getKennung());
    assertEquals(Fahrzeugtyp.LF20, lf20.getFahrzeugtyp());
    assertEquals(4, service.getFahrzeugeDerAktivenWehr().size());

    assertThrows(IllegalArgumentException.class, () -> service.fahrzeugHinzufuegen(null));
    assertThrows(IllegalArgumentException.class, () -> service.fahrzeugHinzufuegen("", Fahrzeugtyp.LF20, "LF20"));
    assertThrows(IllegalArgumentException.class, () -> service.fahrzeugHinzufuegen(null, Fahrzeugtyp.LF20, "LF20"));
    assertThrows(IllegalArgumentException.class, () -> service.fahrzeugHinzufuegen("", "LF20", "LF20"));
    assertThrows(IllegalArgumentException.class, () -> service.fahrzeugHinzufuegen(null, "LF20", "LF20"));
  }

  @Test
  @DisplayName("Einsatzfahrzeug über String-Parameter hinzufügen mit Fallback und ungültigem Typ")
  void testFahrzeugHinzufuegenMitStringParametern() {
    Einsatzfahrzeug fzMitTyp = service.fahrzeugHinzufuegen("Florian Test 1/10-1", "KLF", "");
    assertNotNull(fzMitTyp);
    assertEquals(Fahrzeugtyp.KLF, fzMitTyp.getFahrzeugtyp());
    assertEquals("KLF", fzMitTyp.getBezeichnung());

    Einsatzfahrzeug fzUngueltig = service.fahrzeugHinzufuegen("Florian Test 1/11-1", "UNGUELTIG", "");
    assertNotNull(fzUngueltig);
    assertEquals(null, fzUngueltig.getFahrzeugtyp());
    assertEquals("Florian Test 1/11-1", fzUngueltig.getBezeichnung());
  }

  @Test
  @DisplayName("Kamerad über String-Formulardaten hinzufügen mit Adresserzeugung und Datum-Parsing")
  void testKameradHinzufuegenMitFormulardaten() {
    Kamerad k1 = service.kameradHinzufuegen(
        "Lisa",
        "Müller",
        "2000-01-01",
        "Hauptstr.",
        "1",
        "12345",
        "Stadt",
        "01234",
        "lisa@test.de"
    );
    assertNotNull(k1);
    assertEquals("Lisa", k1.getVorname());
    assertEquals(LocalDate.of(2000, 1, 1), k1.getGeburtsdatum());
    assertNotNull(k1.getAdresse());
    assertEquals("Hauptstr.", k1.getAdresse().strasse());

    Kamerad k2 = service.kameradHinzufuegen(
        "Tom",
        "Bauer",
        "ungueltiges-datum",
        "",
        "",
        "",
        "",
        "",
        ""
    );
    assertNotNull(k2);
    assertEquals(null, k2.getGeburtsdatum());
    assertEquals(null, k2.getAdresse());

    assertThrows(IllegalArgumentException.class, () -> service.kameradHinzufuegen("", "Nachname", "2000-01-01", "", "", "", "", "", ""));
    assertThrows(IllegalArgumentException.class, () -> service.kameradHinzufuegen("Vorname", "", "2000-01-01", "", "", "", "", "", ""));
    assertThrows(IllegalArgumentException.class, () -> service.kameradHinzufuegen(null, "Nachname", "2000-01-01", "", "", "", "", "", ""));
    assertThrows(IllegalArgumentException.class, () -> service.kameradHinzufuegen("Vorname", null, "2000-01-01", "", "", "", "", "", ""));
  }

  @Test
  @DisplayName("Dashboard-Übersicht liefert aggregierte Kennzahlen und Objektreferenzen")
  void testGetDashboardUebersicht() {
    DashboardUebersicht uebersicht = service.getDashboardUebersicht();
    assertNotNull(uebersicht);
    assertEquals(1, uebersicht.anzahlWehren());
    assertEquals(3, uebersicht.anzahlKameraden());
    assertEquals(2, uebersicht.anzahlFahrzeuge());
    assertEquals(1, uebersicht.anzahlGebaeude());
    assertNotNull(uebersicht.beispielWehr());
    assertEquals(3, uebersicht.kameraden().size());
    assertEquals(2, uebersicht.fahrzeuge().size());
  }

  @Test
  @DisplayName("Service ohne aktive Wehr wirft Exception bzw. liefert leere Menge")
  void testServiceOhneAktiveWehr() {
    service.setAktiveWehr(null);
    assertEquals(Set.of(), service.getAlleKameraden());
    assertEquals(List.of(), service.getFahrzeugeDerAktivenWehr());
    assertEquals("Unbekannt", service.ermittleAbteilungName(Kamerad.builder().vorname("A").nachname("B").build()));

    Kamerad testK = Kamerad.builder().vorname("A").nachname("B").build();
    assertThrows(IllegalStateException.class, () -> service.kameradHinzufuegen(testK));

    Einsatzfahrzeug testFz = Einsatzfahrzeug.builder().id("1").kennung("K").build();
    assertThrows(IllegalStateException.class, () -> service.fahrzeugHinzufuegen(testFz));
  }
}
