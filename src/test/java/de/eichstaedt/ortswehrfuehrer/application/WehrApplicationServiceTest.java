package de.eichstaedt.ortswehrfuehrer.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
  @DisplayName("Einsatzfahrzeug entfernen und löschen")
  void testFahrzeugEntfernen() {
    Einsatzfahrzeug tlf = Einsatzfahrzeug.builder()
        .id("fz-tlf-entfernen")
        .bezeichnung("Tanklöschfahrzeug 3000")
        .kennung("Florian Musterstadt 1/24-99")
        .fahrzeugtyp(Fahrzeugtyp.TLF3000)
        .build();

    service.fahrzeugHinzufuegen(tlf);
    assertEquals(3, service.getFahrzeugeDerAktivenWehr().size());

    service.fahrzeugEntfernen(tlf.getId());
    assertEquals(2, service.getFahrzeugeDerAktivenWehr().size());
    assertFalse(service.getFahrzeugeDerAktivenWehr().contains(tlf));

    service.fahrzeugHinzufuegen(tlf);
    assertEquals(3, service.getFahrzeugeDerAktivenWehr().size());

    service.fahrzeugEntfernen(tlf);
    assertEquals(2, service.getFahrzeugeDerAktivenWehr().size());

    service.fahrzeugHinzufuegen(tlf);
    assertEquals(3, service.getFahrzeugeDerAktivenWehr().size());

    service.fahrzeugLoeschen(tlf.getId());
    assertEquals(2, service.getFahrzeugeDerAktivenWehr().size());

    assertThrows(IllegalArgumentException.class, () -> service.fahrzeugEntfernen((String) null));
    assertThrows(IllegalArgumentException.class, () -> service.fahrzeugEntfernen("   "));
    assertThrows(IllegalArgumentException.class, () -> service.fahrzeugEntfernen((Einsatzfahrzeug) null));
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
    assertNull(fzUngueltig.getFahrzeugtyp());
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
    assertNull(k2.getGeburtsdatum());
    assertNull(k2.getAdresse());

    assertThrows(IllegalArgumentException.class, () -> service.kameradHinzufuegen("", "Nachname", "2000-01-01", "", "", "", "", "", ""));
    assertThrows(IllegalArgumentException.class, () -> service.kameradHinzufuegen("Vorname", "", "2000-01-01", "", "", "", "", "", ""));
    assertThrows(IllegalArgumentException.class, () -> service.kameradHinzufuegen(null, "Nachname", "2000-01-01", "", "", "", "", "", ""));
    assertThrows(IllegalArgumentException.class, () -> service.kameradHinzufuegen("Vorname", null, "2000-01-01", "", "", "", "", "", ""));
  }

  @Test
  @DisplayName("Kamerad entfernen")
  void testKameradEntfernen() {
    Kamerad k = service.kameradHinzufuegen("Test", "Person", LocalDate.of(1995, 3, 10), null, null, null);
    assertEquals(4, service.getAlleKameraden().size());

    service.kameradEntfernen(k.getId());
    assertEquals(3, service.getAlleKameraden().size());
    assertFalse(service.getAlleKameraden().contains(k));

    service.kameradHinzufuegen(k);
    assertEquals(4, service.getAlleKameraden().size());

    service.kameradEntfernen(k);
    assertEquals(3, service.getAlleKameraden().size());

    assertThrows(IllegalArgumentException.class, () -> service.kameradEntfernen((String) null));
    assertThrows(IllegalArgumentException.class, () -> service.kameradEntfernen("   "));
    assertThrows(IllegalArgumentException.class, () -> service.kameradEntfernen((Kamerad) null));
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
    assertThrows(IllegalStateException.class, () -> service.fahrzeugEntfernen("1"));
  }

  @Test
  @DisplayName("Fahrzeug ändern aktualisiert Eigenschaften und validiert Eingaben")
  void testFahrzeugAendern() {
    Einsatzfahrzeug fz = service.fahrzeugHinzufuegen("Florian Alt 1/48-1", "TSF", "Altes TSF");
    assertNotNull(fz);
    assertEquals("Florian Alt 1/48-1", fz.getKennung());

    Einsatzfahrzeug geaendert = service.fahrzeugAendern(
        fz.getId(),
        "Florian Neu 1/48-2",
        "TSF_W",
        "Neues TSF-W"
    );
    assertEquals("Florian Neu 1/48-2", geaendert.getKennung());
    assertEquals(Fahrzeugtyp.TSF_W, geaendert.getFahrzeugtyp());
    assertEquals("Neues TSF-W", geaendert.getBezeichnung());
    assertEquals(geaendert, service.getFahrzeug(fz.getId()));

    service.fahrzeugAendern(fz.getId(), "Florian Neu 1/48-3", Fahrzeugtyp.HLF10, "HLF");
    assertEquals("Florian Neu 1/48-3", fz.getKennung());
    assertEquals(Fahrzeugtyp.HLF10, fz.getFahrzeugtyp());
    assertEquals("HLF", fz.getBezeichnung());

    fz.setKennung("Florian Direct 1/48-4");
    service.fahrzeugAendern(fz);
    assertEquals("Florian Direct 1/48-4", service.getFahrzeug(fz.getId()).getKennung());

    assertNull(service.getFahrzeug(null));
    assertNull(service.getFahrzeug("  "));
    assertNull(service.getFahrzeug("unbekannte-id"));

    assertThrows(IllegalArgumentException.class, () -> service.fahrzeugAendern(null, "K", "TSF", "B"));
    assertThrows(IllegalArgumentException.class, () -> service.fahrzeugAendern("   ", "K", "TSF", "B"));
    assertThrows(IllegalArgumentException.class, () -> service.fahrzeugAendern(fz.getId(), null, "TSF", "B"));
    assertThrows(IllegalArgumentException.class, () -> service.fahrzeugAendern(fz.getId(), "   ", "TSF", "B"));
    assertThrows(IllegalArgumentException.class, () -> service.fahrzeugAendern("unbekannt", "K", "TSF", "B"));
    assertThrows(IllegalArgumentException.class, () -> service.fahrzeugAendern((Einsatzfahrzeug) null));

    service.setAktiveWehr(null);
    assertThrows(IllegalStateException.class, () -> service.fahrzeugAendern(fz.getId(), "K", "TSF", "B"));
    assertThrows(IllegalStateException.class, () -> service.fahrzeugAendern(fz));
  }

  @Test
  @DisplayName("Kamerad ändern aktualisiert Daten, trimmt Eingaben und ordnet Abteilung neu zu")
  void testKameradAendern() {
    Kamerad k = service.kameradHinzufuegen("Max", "Mustermann", LocalDate.now().minusYears(30), null, null, null);
    assertEquals(WehrApplicationService.EINSATZABTEILUNG, service.ermittleAbteilungName(k));

    Kamerad geaendert = service.kameradAendern(
        " " + k.getId() + " ",
        " Konrad ",
        " Eichstädt ",
        LocalDate.now().minusYears(70).toString(),
        " Lindenallee ",
        "3",
        "",
        "Göttlin",
        "  ",
        " konrad@example.com "
    );

    assertEquals(k, geaendert);
    assertEquals(4, service.getAlleKameraden().size());
    assertEquals("Konrad", geaendert.getVorname());
    assertEquals("Eichstädt", geaendert.getNachname());
    assertEquals(new Adresse("Lindenallee", "3", "", "Göttlin"), geaendert.getAdresse());
    assertNull(geaendert.getTelefonnummer());
    assertEquals("konrad@example.com", geaendert.getEmailAdresse());
    assertEquals(WehrApplicationService.ALTERS_UND_EHRENABTEILUNG, service.ermittleAbteilungName(geaendert));
    assertEquals(geaendert, service.getKamerad(k.getId()));

    service.kameradAendern(k.getId(), "Konrad", "Eichstädt", null, null, null, null, null, null, null);
    assertNull(geaendert.getGeburtsdatum());
    assertNull(geaendert.getAdresse());
    assertEquals(WehrApplicationService.EINSATZABTEILUNG, service.ermittleAbteilungName(geaendert));
  }

  @Test
  @DisplayName("Kamerad ändern validiert Eingaben")
  void testKameradAendernValidierung() {
    Kamerad k = service.kameradHinzufuegen("Max", "Mustermann", null, null, null, null);

    assertThrows(IllegalArgumentException.class,
        () -> service.kameradAendern(null, "Konrad", "Eichstädt", null, null, null, null));
    assertThrows(IllegalArgumentException.class,
        () -> service.kameradAendern("  ", "Konrad", "Eichstädt", null, null, null, null));
    assertThrows(IllegalArgumentException.class,
        () -> service.kameradAendern(k.getId(), "", "Eichstädt", null, null, null, null));
    assertThrows(IllegalArgumentException.class,
        () -> service.kameradAendern(k.getId(), "Konrad", null, null, null, null, null));
    assertThrows(IllegalArgumentException.class,
        () -> service.kameradAendern("unbekannt", "Konrad", "Eichstädt", null, null, null, null));
    assertEquals("Max", service.getKamerad(k.getId()).getVorname());

    assertNull(service.getKamerad(null));
    assertNull(service.getKamerad("unbekannt"));

    service.setAktiveWehr(null);
    assertNull(service.getKamerad(k.getId()));
    assertThrows(IllegalStateException.class,
        () -> service.kameradAendern(k.getId(), "Konrad", "Eichstädt", null, null, null, null));
  }
}
