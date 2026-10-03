package de.eichstaedt.ortswehrfuehrer.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
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
  void testGebaeudeHinzufuegen() {
    Wehr wehr = new Wehr();
    Adresse adresse = new Adresse("Hauptstraße", "12a", "14712", "Göttlin");
    Gebaeude gebaeude = new Gebaeude("Gerätehaus Göttlin", adresse);

    Wehr result = wehr.gebaeudeHinzufuegen(gebaeude);

    assertSame(wehr, result);
    assertEquals(1, wehr.getGebaeude().size());
    assertEquals(gebaeude, wehr.getGebaeude().get(0));
    assertEquals("Gerätehaus Göttlin", wehr.getGebaeude().get(0).getBezeichnung());
    assertEquals(adresse, wehr.getGebaeude().get(0).getAdresse());
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
        .build();

    Wehr result = wehr.kameradHinzufuegen(kamerad);

    assertSame(wehr, result);
    assertEquals(1, wehr.getKameraden().size());
    assertEquals(kamerad, wehr.getKameraden().get(0));
    assertEquals("Max", wehr.getKameraden().get(0).getVorname());
    assertEquals("Mustermann", wehr.getKameraden().get(0).getNachname());
    assertEquals(geburtsdatum, wehr.getKameraden().get(0).getGeburtsdatum());
    assertEquals(adresse, wehr.getKameraden().get(0).getAdresse());
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
    assertEquals(jugendlicher16, wehr.getJugendabteilung().getKameraden().get(0));
    assertEquals(kind12, wehr.getJugendabteilung().getKameraden().get(1));
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
    assertEquals(einsatzkraft17, wehr.getEinsatzabteilung().getKameraden().get(0));
    assertEquals(einsatzkraft65, wehr.getEinsatzabteilung().getKameraden().get(1));
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
    assertEquals(senior66, wehr.getAltersUndEhrenabteilung().getKameraden().get(0));
    assertEquals(senior80, wehr.getAltersUndEhrenabteilung().getKameraden().get(1));
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
    assertEquals(jugend, wehr.getJugendabteilung().getKameraden().get(0));

    assertEquals(1, wehr.getEinsatzabteilung().getKameraden().size());
    assertEquals(einsatz, wehr.getEinsatzabteilung().getKameraden().get(0));

    assertEquals(1, wehr.getAltersUndEhrenabteilung().getKameraden().size());
    assertEquals(ehren, wehr.getAltersUndEhrenabteilung().getKameraden().get(0));
  }

  @Test
  void testSetKameraden() {
    Wehr wehr = new Wehr();
    Kamerad kamerad1 = Kamerad.builder().vorname("Max").nachname("Mustermann").build();
    Kamerad kamerad2 = Kamerad.builder().vorname("Erika").nachname("Musterfrau").build();

    wehr.getKameraden().addAll(List.of(kamerad1, kamerad2));

    assertEquals(2, wehr.getKameraden().size());
    assertEquals(kamerad1, wehr.getKameraden().get(0));
    assertEquals(kamerad2, wehr.getKameraden().get(1));
  }
}
