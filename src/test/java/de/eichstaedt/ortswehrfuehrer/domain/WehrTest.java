package de.eichstaedt.ortswehrfuehrer.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class WehrTest {

  @Test
  void testInitialState() {
    Wehr wehr = new Wehr();
    assertNull(wehr.getId());
    assertNull(wehr.getName());
    assertNull(wehr.getGruendungsdatum());
    assertNotNull(wehr.getGebaeude());
    assertTrue(wehr.getGebaeude().isEmpty());
    assertNotNull(wehr.getFahrzeuge());
    assertTrue(wehr.getFahrzeuge().isEmpty());
    assertNotNull(wehr.getFahrzeugIds());
    assertTrue(wehr.getFahrzeugIds().isEmpty());
    assertNotNull(wehr.getEinsatzfahrzeuge());
    assertTrue(wehr.getEinsatzfahrzeuge().isEmpty());
    assertNotNull(wehr.getEinsatzfahrzeugIds());
    assertTrue(wehr.getEinsatzfahrzeugIds().isEmpty());
    assertNotNull(wehr.getKameraden());
    assertTrue(wehr.getKameraden().isEmpty());

    assertNotNull(wehr.getJugendabteilung());
    assertEquals("Jugendabteilung", wehr.getJugendabteilung().getBezeichnung());
    assertTrue(wehr.getJugendabteilung().getKameraden().isEmpty());

    assertNotNull(wehr.getEinsatzabteilung());
    assertEquals("Einsatzabteilung", wehr.getEinsatzabteilung().getBezeichnung());
    assertTrue(wehr.getEinsatzabteilung().getKameraden().isEmpty());

    assertNotNull(wehr.getAltersUndEhrenabteilung());
    assertEquals("Alters- und Ehrenabteilung", wehr.getAltersUndEhrenabteilung().getBezeichnung());
    assertTrue(wehr.getAltersUndEhrenabteilung().getKameraden().isEmpty());
  }

  @Test
  void testGruenden() {
    Wehr wehr = new Wehr();
    String wehrName = "Freiwillige Feuerwehr Goettlin";
    LocalDate gruendungsdatum = LocalDate.of(1924, 5, 1);

    Wehr result = wehr.gruenden(wehrName, gruendungsdatum);

    assertSame(wehr, result);
    assertEquals(wehrName, wehr.getName());
    assertEquals(gruendungsdatum, wehr.getGruendungsdatum());
    assertNotNull(wehr.getId());
    assertDoesNotThrow(() -> UUID.fromString(wehr.getId()));
  }

  @Test
  void testGruendenNurMitName() {
    Wehr wehr = new Wehr();
    String wehrName = "Freiwillige Feuerwehr Goettlin";

    Wehr result = wehr.gruenden(wehrName);

    assertSame(wehr, result);
    assertEquals(wehrName, wehr.getName());
    assertNull(wehr.getGruendungsdatum());
    assertNotNull(wehr.getId());
    assertDoesNotThrow(() -> UUID.fromString(wehr.getId()));
  }

  @Test
  void testKonstruktorenUndSetter() {
    LocalDate gruendung = LocalDate.of(1920, 1, 1);
    Wehr wehr1 = new Wehr("Wehr 1", gruendung);
    assertNotNull(wehr1.getId());
    assertEquals("Wehr 1", wehr1.getName());
    assertEquals(gruendung, wehr1.getGruendungsdatum());

    Wehr wehr2 = new Wehr("custom-id-1", "Wehr 2", gruendung);
    assertEquals("custom-id-1", wehr2.getId());
    assertEquals("Wehr 2", wehr2.getName());
    assertEquals(gruendung, wehr2.getGruendungsdatum());

    Wehr wehr3 = new Wehr("   ", "Wehr 3", null);
    assertNotNull(wehr3.getId());
    assertDoesNotThrow(() -> UUID.fromString(wehr3.getId()));

    Wehr wehr4 = new Wehr();
    wehr4.setId("id-4");
    wehr4.setName("Wehr 4");
    wehr4.setGruendungsdatum(gruendung);
    assertEquals("id-4", wehr4.getId());
    assertEquals("Wehr 4", wehr4.getName());
    assertEquals(gruendung, wehr4.getGruendungsdatum());
  }

  @Test
  void testGebaeudeHinzufuegen() {
    Wehr wehr = new Wehr();
    Adresse adresse = new Adresse("Hauptstraße", "12a", "14712", "Göttlin");
    Gebaeude gebaeude = new Gebaeude("Gerätehaus Göttlin", adresse);

    Wehr result = wehr.gebaeudeHinzufuegen(gebaeude);

    assertSame(wehr, result);
    assertEquals(1, wehr.getGebaeude().size());
    assertEquals(gebaeude, wehr.getGebaeude().getFirst());
    assertEquals("Gerätehaus Göttlin", wehr.getGebaeude().getFirst().getBezeichnung());
    assertEquals(adresse, wehr.getGebaeude().getFirst().getAdresse());
  }

  @Test
  void testSetGebaeude() {
    Wehr wehr = new Wehr();
    Adresse adresse1 = new Adresse("Hauptstraße", "12a", "14712", "Göttlin");
    Gebaeude gebaeude1 = new Gebaeude("Gerätehaus Göttlin", adresse1);
    Adresse adresse2 = new Adresse("Seestraße", "5", "14712", "Göttlin");
    Gebaeude gebaeude2 = new Gebaeude("Bootshaus Göttlin", adresse2);

    wehr.getGebaeude().addAll(List.of(gebaeude1, gebaeude2));

    assertEquals(2, wehr.getGebaeude().size());
    assertEquals(gebaeude1, wehr.getGebaeude().get(0));
    assertEquals(gebaeude2, wehr.getGebaeude().get(1));
  }

  @Test
  void testKameradHinzufuegen() {
    Wehr wehr = new Wehr();
    Adresse adresse = new Adresse("Dorfstraße", "12", "14712", "Göttlin");
    LocalDate geburtsdatum = LocalDate.of(1990, 5, 20);
    Kamerad kamerad = Kamerad.builder()
        .vorname("Max")
        .nachname("Mustermann")
        .geburtsdatum(geburtsdatum)
        .adresse(adresse)
        .telefonnummer("01738884932")
        .emailAdresse("max.mustermann@example.com")
        .build();

    Wehr result = wehr.kameradHinzufuegen(kamerad);

    assertSame(wehr, result);
    assertEquals(1, wehr.getKameraden().size());
    assertTrue(wehr.getKameraden().contains(kamerad));
    assertEquals("Max", kamerad.getVorname());
    assertEquals("Mustermann", kamerad.getNachname());
    assertEquals(geburtsdatum, kamerad.getGeburtsdatum());
    assertEquals(adresse, kamerad.getAdresse());
    assertEquals("01738884932", kamerad.getTelefonnummer());
    assertEquals("max.mustermann@example.com", kamerad.getEmailAdresse());
    assertTrue(wehr.getEinsatzabteilung().getKameraden().contains(kamerad));
  }

  @Test
  void testKameradHinzufuegenJugendabteilungBis16Jahre() {
    Wehr wehr = new Wehr();
    LocalDate stichtag = LocalDate.of(2026, 1, 1);

    // Genau 16 Jahre alt
    Kamerad jugendlicher16 = Kamerad.builder()
        .vorname("Tim")
        .nachname("Müller")
        .geburtsdatum(LocalDate.of(2010, 1, 1))
        .build();

    // 12 Jahre alt
    Kamerad kind12 = Kamerad.builder()
        .vorname("Anna")
        .nachname("Schmidt")
        .geburtsdatum(LocalDate.of(2014, 1, 1))
        .build();

    wehr.kameradHinzufuegen(jugendlicher16, stichtag);
    wehr.kameradHinzufuegen(kind12, stichtag);

    assertEquals(2, wehr.getKameraden().size());
    assertEquals(2, wehr.getJugendabteilung().getKameraden().size());
    assertTrue(wehr.getJugendabteilung().getKameraden().contains(jugendlicher16));
    assertTrue(wehr.getJugendabteilung().getKameraden().contains(kind12));
    assertTrue(wehr.getEinsatzabteilung().getKameraden().isEmpty());
    assertTrue(wehr.getAltersUndEhrenabteilung().getKameraden().isEmpty());
  }

  @Test
  void testKameradHinzufuegenEinsatzabteilung17Bis65Jahre() {
    Wehr wehr = new Wehr();
    LocalDate stichtag = LocalDate.of(2026, 1, 1);

    // Genau 17 Jahre alt
    Kamerad einsatzkraft17 = Kamerad.builder()
        .vorname("Felix")
        .nachname("Weber")
        .geburtsdatum(LocalDate.of(2009, 1, 1))
        .build();

    // Genau 65 Jahre alt
    Kamerad einsatzkraft65 = Kamerad.builder()
        .vorname("Hans")
        .nachname("Bauer")
        .geburtsdatum(LocalDate.of(1961, 1, 1))
        .build();

    wehr.kameradHinzufuegen(einsatzkraft17, stichtag);
    wehr.kameradHinzufuegen(einsatzkraft65, stichtag);

    assertEquals(2, wehr.getKameraden().size());
    assertEquals(2, wehr.getEinsatzabteilung().getKameraden().size());
    assertTrue(wehr.getEinsatzabteilung().getKameraden().contains(einsatzkraft17));
    assertTrue(wehr.getEinsatzabteilung().getKameraden().contains(einsatzkraft65));
    assertTrue(wehr.getJugendabteilung().getKameraden().isEmpty());
    assertTrue(wehr.getAltersUndEhrenabteilung().getKameraden().isEmpty());
  }

  @Test
  void testKameradHinzufuegenAltersUndEhrenabteilungUeber65Jahre() {
    Wehr wehr = new Wehr();
    LocalDate stichtag = LocalDate.of(2026, 1, 1);

    // Genau 66 Jahre alt (> 65)
    Kamerad senior66 = Kamerad.builder()
        .vorname("Walter")
        .nachname("Koch")
        .geburtsdatum(LocalDate.of(1960, 1, 1))
        .build();

    // 80 Jahre alt
    Kamerad senior80 = Kamerad.builder()
        .vorname("Ernst")
        .nachname("Richter")
        .geburtsdatum(LocalDate.of(1946, 1, 1))
        .build();

    wehr.kameradHinzufuegen(senior66, stichtag);
    wehr.kameradHinzufuegen(senior80, stichtag);

    assertEquals(2, wehr.getKameraden().size());
    assertEquals(2, wehr.getAltersUndEhrenabteilung().getKameraden().size());
    assertTrue(wehr.getAltersUndEhrenabteilung().getKameraden().contains(senior66));
    assertTrue(wehr.getAltersUndEhrenabteilung().getKameraden().contains(senior80));
    assertTrue(wehr.getJugendabteilung().getKameraden().isEmpty());
    assertTrue(wehr.getEinsatzabteilung().getKameraden().isEmpty());
  }

  @Test
  void testKameradHinzufuegenZuordnungVerschiedenerAbteilungen() {
    Wehr wehr = new Wehr();
    LocalDate stichtag = LocalDate.of(2026, 1, 1);

    Kamerad jugend = Kamerad.builder()
        .vorname("Jonas")
        .nachname("Klein")
        .geburtsdatum(LocalDate.of(2012, 5, 10)) // 13 Jahre
        .build();
    Kamerad einsatz = Kamerad.builder()
        .vorname("Sabine")
        .nachname("Gross")
        .geburtsdatum(LocalDate.of(1995, 3, 15)) // 30 Jahre
        .build();
    Kamerad ehren = Kamerad.builder()
        .vorname("Kurt")
        .nachname("Alt")
        .geburtsdatum(LocalDate.of(1950, 8, 22)) // 75 Jahre
        .build();

    wehr.kameradHinzufuegen(jugend, stichtag)
        .kameradHinzufuegen(einsatz, stichtag)
        .kameradHinzufuegen(ehren, stichtag);

    assertEquals(3, wehr.getKameraden().size());

    assertEquals(1, wehr.getJugendabteilung().getKameraden().size());
    assertTrue(wehr.getJugendabteilung().getKameraden().contains(jugend));

    assertEquals(1, wehr.getEinsatzabteilung().getKameraden().size());
    assertTrue(wehr.getEinsatzabteilung().getKameraden().contains(einsatz));

    assertEquals(1, wehr.getAltersUndEhrenabteilung().getKameraden().size());
    assertTrue(wehr.getAltersUndEhrenabteilung().getKameraden().contains(ehren));
  }

  @Test
  void testKameradenWerdenAusAllenAbteilungenGelesen() {
    Wehr wehr = new Wehr();
    LocalDate stichtag = LocalDate.of(2026, 1, 1);

    Kamerad jugend = Kamerad.builder()
        .vorname("Tim")
        .nachname("Müller")
        .geburtsdatum(LocalDate.of(2012, 1, 1))
        .build();
    Kamerad einsatz = Kamerad.builder()
        .vorname("Max")
        .nachname("Mustermann")
        .geburtsdatum(LocalDate.of(1990, 1, 1))
        .build();
    Kamerad ehren = Kamerad.builder()
        .vorname("Hans")
        .nachname("Bauer")
        .geburtsdatum(LocalDate.of(1950, 1, 1))
        .build();

    wehr.kameradHinzufuegen(jugend, stichtag)
        .kameradHinzufuegen(einsatz, stichtag)
        .kameradHinzufuegen(ehren, stichtag);

    Set<Kamerad> alleKameraden = wehr.getKameraden();
    assertEquals(3, alleKameraden.size());
    assertTrue(alleKameraden.contains(jugend));
    assertTrue(alleKameraden.contains(einsatz));
    assertTrue(alleKameraden.contains(ehren));
  }

  @Test
  void testKeineDoppeltenKameradenBeimMehrfachenHinzufuegen() {
    Wehr wehr = new Wehr();
    LocalDate stichtag = LocalDate.of(2026, 1, 1);

    Kamerad kamerad = Kamerad.builder()
        .id("kamerad-unique")
        .vorname("Max")
        .nachname("Mustermann")
        .geburtsdatum(LocalDate.of(1990, 1, 1))
        .build();

    wehr.kameradHinzufuegen(kamerad, stichtag);
    wehr.kameradHinzufuegen(kamerad, stichtag);

    assertEquals(1, wehr.getKameraden().size());
    assertEquals(1, wehr.getEinsatzabteilung().getKameraden().size());
    assertTrue(wehr.getKameraden().contains(kamerad));
  }

  @Test
  void testFahrzeugHinzufuegenViaId() {
    Wehr wehr = new Wehr();
    String fahrzeugId = "fz-tsf-01";

    Wehr result = wehr.fahrzeugHinzufuegen(fahrzeugId);

    assertSame(wehr, result);
    assertEquals(1, wehr.getFahrzeugIds().size());
    assertTrue(wehr.getFahrzeugIds().contains("fz-tsf-01"));
    assertTrue(wehr.getFahrzeuge().contains("fz-tsf-01"));
    assertTrue(wehr.getEinsatzfahrzeuge().contains("fz-tsf-01"));
    assertTrue(wehr.getEinsatzfahrzeugIds().contains("fz-tsf-01"));
  }

  @Test
  void testFahrzeugHinzufuegenViaEinsatzfahrzeug() {
    Wehr wehr = new Wehr();
    Einsatzfahrzeug fahrzeug = Einsatzfahrzeug.builder()
        .id("fz-tsf-48-1")
        .bezeichnung("Tragkraftspritzenfahrzeug")
        .kennung("Florian Göttlin 48-1")
        .fahrzeugtyp(Fahrzeugtyp.TSF)
        .build();

    Wehr result = wehr.fahrzeugHinzufuegen(fahrzeug);

    assertSame(wehr, result);
    assertEquals(1, wehr.getFahrzeugIds().size());
    assertTrue(wehr.getFahrzeugIds().contains(fahrzeug.getId()));
    assertTrue(wehr.getFahrzeuge().contains("fz-tsf-48-1"));
  }

  @Test
  void testEinsatzfahrzeugHinzufuegenAlias() {
    Wehr wehr = new Wehr();
    Einsatzfahrzeug fahrzeug = Einsatzfahrzeug.builder()
        .id("fz-hlf-48-2")
        .bezeichnung("Hilfeleistungslöschgruppenfahrzeug")
        .kennung("Florian Göttlin 48-2")
        .fahrzeugtyp(Fahrzeugtyp.HLF10)
        .build();

    Wehr result = wehr.einsatzfahrzeugHinzufuegen(fahrzeug);
    assertSame(wehr, result);
    assertTrue(wehr.getFahrzeugIds().contains("fz-hlf-48-2"));

    wehr.einsatzfahrzeugHinzufuegen("fz-tlf-48-3");
    assertEquals(2, wehr.getFahrzeugIds().size());
    assertTrue(wehr.getFahrzeugIds().contains("fz-tlf-48-3"));
  }

  @Test
  void testKeineDoppeltenFahrzeugeBeimMehrfachenHinzufuegen() {
    Wehr wehr = new Wehr();
    Einsatzfahrzeug fahrzeug = Einsatzfahrzeug.builder()
        .id("fz-unique-1")
        .bezeichnung("TSF")
        .kennung("Florian 1")
        .fahrzeugtyp(Fahrzeugtyp.TSF)
        .build();

    wehr.fahrzeugHinzufuegen(fahrzeug);
    wehr.fahrzeugHinzufuegen(fahrzeug);
    wehr.fahrzeugHinzufuegen("fz-unique-1");

    assertEquals(1, wehr.getFahrzeugIds().size());
    assertTrue(wehr.getFahrzeugIds().contains("fz-unique-1"));
  }

  @Test
  void testFahrzeugEntfernen() {
    Wehr wehr = new Wehr();
    Einsatzfahrzeug fahrzeug1 = Einsatzfahrzeug.builder()
        .id("fz-1")
        .bezeichnung("TSF")
        .kennung("Florian 1")
        .fahrzeugtyp(Fahrzeugtyp.TSF)
        .build();
    Einsatzfahrzeug fahrzeug2 = Einsatzfahrzeug.builder()
        .id("fz-2")
        .bezeichnung("TLF 2000")
        .kennung("Florian 2")
        .fahrzeugtyp(Fahrzeugtyp.TLF2000)
        .build();

    wehr.fahrzeugHinzufuegen(fahrzeug1).fahrzeugHinzufuegen(fahrzeug2);
    assertEquals(2, wehr.getFahrzeugIds().size());

    wehr.fahrzeugEntfernen(fahrzeug1);
    assertEquals(1, wehr.getFahrzeugIds().size());
    assertFalse(wehr.getFahrzeugIds().contains("fz-1"));
    assertTrue(wehr.getFahrzeugIds().contains("fz-2"));

    wehr.fahrzeugEntfernen("fz-2");
    assertTrue(wehr.getFahrzeugIds().isEmpty());

    // Null safety
    assertDoesNotThrow(() -> wehr.fahrzeugEntfernen((String) null));
    assertDoesNotThrow(() -> wehr.fahrzeugEntfernen((Einsatzfahrzeug) null));
  }

  @Test
  void testEinsatzfahrzeugEntfernenAlias() {
    Wehr wehr = new Wehr();
    Einsatzfahrzeug fahrzeug = Einsatzfahrzeug.builder()
        .id("fz-alias-1")
        .bezeichnung("TSF")
        .kennung("Florian 1")
        .fahrzeugtyp(Fahrzeugtyp.TSF)
        .build();

    wehr.einsatzfahrzeugHinzufuegen(fahrzeug);
    assertEquals(1, wehr.getFahrzeugIds().size());

    wehr.einsatzfahrzeugEntfernen(fahrzeug);
    assertTrue(wehr.getFahrzeugIds().isEmpty());

    wehr.einsatzfahrzeugHinzufuegen("fz-alias-2");
    assertEquals(1, wehr.getFahrzeugIds().size());

    wehr.einsatzfahrzeugEntfernen("fz-alias-2");
    assertTrue(wehr.getFahrzeugIds().isEmpty());
  }

  @Test
  void testUcF02EinsatzfahrzeugLoeschen() {
    // Gegeben sei eine WehrFactory
    WehrFactory factory = new WehrFactory();

    // Wenn eine Wehr mit dem Namen "Freiwillige Feuerwehr Göttlin" und dem Gründungsdatum "01.05.1924" über die Factory erzeugt wird
    LocalDate gruendung = LocalDate.of(1924, 5, 1);
    Wehr wehr = factory.erzeugeWehr("Freiwillige Feuerwehr Göttlin", gruendung);

    // Dann besitzt die erzeugte Wehr eine gültige UUID als ID
    assertNotNull(wehr.getId());
    assertDoesNotThrow(() -> UUID.fromString(wehr.getId()));
    // Und der Name lautet "Freiwillige Feuerwehr Göttlin"
    assertEquals("Freiwillige Feuerwehr Göttlin", wehr.getName());
    // Und das Gründungsdatum ist der 01.05.1924
    assertEquals(gruendung, wehr.getGruendungsdatum());
    // Und alle Abteilungen und Kollektionen sind initialisiert
    assertNotNull(wehr.getJugendabteilung());
    assertNotNull(wehr.getEinsatzabteilung());
    assertNotNull(wehr.getAltersUndEhrenabteilung());
    assertNotNull(wehr.getGebaeude());
    assertNotNull(wehr.getFahrzeugIds());

    // Die verfügt über ein Fahrzeug vom Typ TSF mit der Kennung HVL/5/47/2.
    Einsatzfahrzeug tsf = Einsatzfahrzeug.builder()
        .id("fz-tsf-goettlin")
        .kennung("HVL/5/47/2")
        .fahrzeugtyp(Fahrzeugtyp.TSF)
        .bezeichnung("Tragkraftspritzenfahrzeug")
        .build();
    wehr.fahrzeugHinzufuegen(tsf);
    assertEquals(1, wehr.getFahrzeugIds().size());
    assertTrue(wehr.getFahrzeugIds().contains(tsf.getId()));

    // Es wird die Funktion Fahrzeug entfernen aufgerufen.
    wehr.fahrzeugEntfernen(tsf);

    // Nachbedingung: Die Wehr verfügt über keine Fahrzeuge mehr.
    assertTrue(wehr.getFahrzeugIds().isEmpty());
    assertTrue(wehr.getFahrzeuge().isEmpty());
  }

  @Test
  void testUcF03EinsatzfahrzeugAendern() {
    // Gegeben sei eine WehrFactory
    WehrFactory factory = new WehrFactory();

    // Wenn eine Wehr mit dem Namen "Freiwillige Feuerwehr Göttlin" und dem Gründungsdatum "01.05.1924" über die Factory erzeugt wird
    LocalDate gruendung = LocalDate.of(1924, 5, 1);
    Wehr wehr = factory.erzeugeWehr("Freiwillige Feuerwehr Göttlin", gruendung);

    // Dann besitzt die erzeugte Wehr eine gültige UUID als ID
    assertNotNull(wehr.getId());
    assertDoesNotThrow(() -> UUID.fromString(wehr.getId()));
    // Und der Name lautet "Freiwillige Feuerwehr Göttlin"
    assertEquals("Freiwillige Feuerwehr Göttlin", wehr.getName());
    // Und das Gründungsdatum ist der 01.05.1924
    assertEquals(gruendung, wehr.getGruendungsdatum());
    // Und alle Abteilungen und Kollektionen sind initialisiert
    assertNotNull(wehr.getJugendabteilung());
    assertNotNull(wehr.getEinsatzabteilung());
    assertNotNull(wehr.getAltersUndEhrenabteilung());
    assertNotNull(wehr.getGebaeude());
    assertNotNull(wehr.getFahrzeugIds());

    // Die verfügt über ein Fahrzeug vom Typ TSF mit der Kennung HVL/5/47/2.
    Einsatzfahrzeug tsf = Einsatzfahrzeug.builder()
        .id("fz-tsf-goettlin")
        .kennung("HVL/5/47/2")
        .fahrzeugtyp(Fahrzeugtyp.TSF)
        .bezeichnung("Tragkraftspritzenfahrzeug")
        .build();
    wehr.fahrzeugHinzufuegen(tsf);
    assertEquals(1, wehr.getFahrzeugIds().size());
    assertTrue(wehr.getFahrzeugIds().contains(tsf.getId()));

    // Es wird die Funktion Fahrzeug aendern aufgerufen und die Kennung zu HVL/5/47/1 geändert.
    wehr.fahrzeugAendern(tsf, "HVL/5/47/1");

    // Nachbedingung:
    // - Die Wehr verfügt über ein Fahrzeug mehr (bzw. weiterhin über das Fahrzeug).
    // - Die Kennung ist nun HVL/5/47/1
    assertEquals(1, wehr.getFahrzeugIds().size());
    assertEquals("HVL/5/47/1", tsf.getKennung());
  }

  @Test
  void testUc82KameradEntfernen() {
    // Gegeben sei eine WehrFactory
    WehrFactory factory = new WehrFactory();

    // Wenn eine Wehr mit dem Namen "Freiwillige Feuerwehr Göttlin" und dem Gründungsdatum "01.05.1924" über die Factory erzeugt wird
    LocalDate gruendung = LocalDate.of(1924, 5, 1);
    Wehr wehr = factory.erzeugeWehr("Freiwillige Feuerwehr Göttlin", gruendung);

    // Dann besitzt die erzeugte Wehr eine gültige UUID als ID
    assertNotNull(wehr.getId());
    assertDoesNotThrow(() -> UUID.fromString(wehr.getId()));
    // Und der Name lautet "Freiwillige Feuerwehr Göttlin"
    assertEquals("Freiwillige Feuerwehr Göttlin", wehr.getName());
    // Und das Gründungsdatum ist der 01.05.1924
    assertEquals(gruendung, wehr.getGruendungsdatum());
    // Und alle Abteilungen und Kollektionen sind initialisiert
    assertNotNull(wehr.getJugendabteilung());
    assertNotNull(wehr.getEinsatzabteilung());
    assertNotNull(wehr.getAltersUndEhrenabteilung());
    assertNotNull(wehr.getGebaeude());
    assertNotNull(wehr.getFahrzeugIds());
    assertNotNull(wehr.getKameraden());

    // Die verfügt über ein Kamerad besitzt den Vornamen "Max" und den Nachnamen "Mustermann.
    Kamerad max = Kamerad.builder()
        .id("kam-max-mustermann")
        .vorname("Max")
        .nachname("Mustermann")
        .geburtsdatum(LocalDate.of(1990, 5, 20))
        .build();
    wehr.kameradHinzufuegen(max);
    assertEquals(1, wehr.getKameraden().size());
    assertTrue(wehr.getKameraden().contains(max));

    // Es wird die Funktion Kamerad entfernen aufgerufen.
    wehr.kameradEntfernen(max);

    // Nachbedingung: Die Wehr verfügt über keine Kameraden mehr.
    assertTrue(wehr.getKameraden().isEmpty());
  }

  @Test
  void testKameradEntfernenUeberIdUndVerschiedeneAbteilungen() {
    Wehr wehr = new Wehr();
    LocalDate refDate = LocalDate.of(2024, 1, 1);

    Kamerad jugend = Kamerad.builder().id("k-j").vorname("Jugend").nachname("Mitglied").geburtsdatum(LocalDate.of(2012, 1, 1)).build();
    Kamerad einsatz = Kamerad.builder().id("k-e").vorname("Einsatz").nachname("Mitglied").geburtsdatum(LocalDate.of(1990, 1, 1)).build();
    Kamerad alters = Kamerad.builder().id("k-a").vorname("Alters").nachname("Mitglied").geburtsdatum(LocalDate.of(1950, 1, 1)).build();

    wehr.kameradHinzufuegen(jugend, refDate);
    wehr.kameradHinzufuegen(einsatz, refDate);
    wehr.kameradHinzufuegen(alters, refDate);

    assertEquals(3, wehr.getKameraden().size());
    assertTrue(wehr.getJugendabteilung().getKameraden().contains(jugend));
    assertTrue(wehr.getEinsatzabteilung().getKameraden().contains(einsatz));
    assertTrue(wehr.getAltersUndEhrenabteilung().getKameraden().contains(alters));

    // Entfernen per ID
    wehr.kameradEntfernen("k-j");
    assertEquals(2, wehr.getKameraden().size());
    assertFalse(wehr.getJugendabteilung().getKameraden().contains(jugend));

    // Entfernen per Instanz
    wehr.kameradEntfernen(einsatz);
    assertEquals(1, wehr.getKameraden().size());
    assertFalse(wehr.getEinsatzabteilung().getKameraden().contains(einsatz));

    // Entfernen des letzten Kameraden
    wehr.kameradEntfernen("k-a");
    assertTrue(wehr.getKameraden().isEmpty());

    // Null/Blank checks
    assertDoesNotThrow(() -> wehr.kameradEntfernen((Kamerad) null));
    assertDoesNotThrow(() -> wehr.kameradEntfernen((String) null));
    assertDoesNotThrow(() -> wehr.kameradEntfernen("   "));
  }

  @Test
  void testSetFahrzeugeUndSetFahrzeugIds() {
    Wehr wehr = new Wehr();

    wehr.setFahrzeugIds(Set.of("fz-1", "fz-2"));
    assertEquals(2, wehr.getFahrzeugIds().size());
    assertTrue(wehr.getFahrzeugIds().contains("fz-1"));
    assertTrue(wehr.getFahrzeugIds().contains("fz-2"));

    wehr.setFahrzeuge(Set.of("fz-3"));
    assertEquals(1, wehr.getFahrzeuge().size());
    assertTrue(wehr.getFahrzeuge().contains("fz-3"));
  }

  @Test
  void testFahrzeugHinzufuegenNullUndInitialisierung() {
    Wehr wehr = new Wehr();
    wehr.setFahrzeuge(null);
    assertNull(wehr.getFahrzeuge());

    Einsatzfahrzeug fahrzeug = Einsatzfahrzeug.builder()
        .id("fz-1")
        .bezeichnung("TSF")
        .kennung("Florian 1")
        .fahrzeugtyp(Fahrzeugtyp.TSF)
        .build();

    wehr.fahrzeugHinzufuegen(fahrzeug);
    assertEquals(1, wehr.getFahrzeuge().size());
    assertTrue(wehr.getFahrzeuge().contains("fz-1"));

    wehr.fahrzeugHinzufuegen((Einsatzfahrzeug) null);
    assertEquals(1, wehr.getFahrzeuge().size());

    wehr.fahrzeugHinzufuegen((String) null);
    assertEquals(1, wehr.getFahrzeuge().size());

    wehr.fahrzeugHinzufuegen("   ");
    assertEquals(1, wehr.getFahrzeuge().size());
  }

  @Test
  void testEqualsUndHashCode() {
    Wehr w1 = new Wehr("w-1", "Feuerwehr A", LocalDate.of(2000, 1, 1));
    Wehr w2 = new Wehr("w-1", "Feuerwehr B", LocalDate.of(2010, 5, 5));
    Wehr w3 = new Wehr("w-2", "Feuerwehr A", LocalDate.of(2000, 1, 1));

    assertEquals(w1, w2);
    assertTrue(w1.equals(w1));
    assertEquals(w1.hashCode(), w2.hashCode());
    assertFalse(w1.equals(w3));
    assertNotNull(w1);
    assertFalse(w1.equals(null));
    assertFalse(w1.equals("other"));
  }
}
