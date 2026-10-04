package de.eichstaedt.ortswehrfuehrer.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class KameradTest {

  @Test
  void testNoArgsConstructorAndSetters() {
    Kamerad kamerad = new Kamerad();
    assertNull(kamerad.getId());
    assertNull(kamerad.getVorname());
    assertNull(kamerad.getNachname());
    assertNull(kamerad.getGeburtsdatum());
    assertNull(kamerad.getAdresse());
    assertNull(kamerad.getTelefonnummer());
    assertNull(kamerad.getEmailAdresse());

    LocalDate geburtsdatum = LocalDate.of(1990, 8, 15);
    Adresse adresse = new Adresse("Dorfstraße", "12", "14712", "Göttlin");

    kamerad.setId("custom-id-123");
    kamerad.setVorname("Max");
    kamerad.setNachname("Mustermann");
    kamerad.setGeburtsdatum(geburtsdatum);
    kamerad.setAdresse(adresse);
    kamerad.setTelefonnummer("01738884932");
    kamerad.setEmailAdresse("max.mustermann@example.com");

    assertEquals("custom-id-123", kamerad.getId());
    assertEquals("Max", kamerad.getVorname());
    assertEquals("Mustermann", kamerad.getNachname());
    assertEquals(geburtsdatum, kamerad.getGeburtsdatum());
    assertEquals(adresse, kamerad.getAdresse());
    assertEquals("01738884932", kamerad.getTelefonnummer());
    assertEquals("max.mustermann@example.com", kamerad.getEmailAdresse());
    assertEquals("max.mustermann@example.com", kamerad.getEmail());

    kamerad.setEmail("neue.email@example.com");
    assertEquals("neue.email@example.com", kamerad.getEmailAdresse());
    assertEquals("neue.email@example.com", kamerad.getEmail());
  }

  @Test
  void testBuilderWithVornameAndNachname() {
    Kamerad kamerad = Kamerad.builder()
        .vorname("Max")
        .nachname("Mustermann")
        .build();

    assertNotNull(kamerad.getId());
    assertDoesNotThrow(() -> UUID.fromString(kamerad.getId()));
    assertEquals("Max", kamerad.getVorname());
    assertEquals("Mustermann", kamerad.getNachname());
    assertNull(kamerad.getGeburtsdatum());
    assertNull(kamerad.getAdresse());
    assertNull(kamerad.getTelefonnummer());
    assertNull(kamerad.getEmailAdresse());
  }

  @Test
  void testBuilderWithAllFields() {
    String id = UUID.randomUUID().toString();
    LocalDate geburtsdatum = LocalDate.of(1992, 11, 4);
    Adresse adresse = new Adresse("Lindenweg", "7", "14712", "Göttlin");

    Kamerad kamerad = Kamerad.builder()
        .id(id)
        .vorname("Erika")
        .nachname("Musterfrau")
        .geburtsdatum(geburtsdatum)
        .adresse(adresse)
        .telefonnummer("01738884932")
        .emailAdresse("erika.musterfrau@example.com")
        .build();

    assertEquals(id, kamerad.getId());
    assertEquals("Erika", kamerad.getVorname());
    assertEquals("Musterfrau", kamerad.getNachname());
    assertEquals(geburtsdatum, kamerad.getGeburtsdatum());
    assertEquals(adresse, kamerad.getAdresse());
    assertEquals("01738884932", kamerad.getTelefonnummer());
    assertEquals("erika.musterfrau@example.com", kamerad.getEmailAdresse());
  }

  @Test
  void testBuilderWithGermanMethodAliases() {
    String id = UUID.randomUUID().toString();
    LocalDate geburtsdatum = LocalDate.of(1985, 3, 20);
    Adresse adresse = new Adresse("Hauptstraße", "5", "14712", "Stechow");

    Kamerad kamerad = Kamerad.builder()
        .mitId(id)
        .mitVorname("Max")
        .mitNachname("Mustermann")
        .mitGeburtsdatum(geburtsdatum)
        .mitAdresse(adresse)
        .mitTelefonnummer("01738884932")
        .mitEmailAdresse("max.mustermann@example.com")
        .build();

    assertEquals(id, kamerad.getId());
    assertEquals("Max", kamerad.getVorname());
    assertEquals("Mustermann", kamerad.getNachname());
    assertEquals(geburtsdatum, kamerad.getGeburtsdatum());
    assertEquals(adresse, kamerad.getAdresse());
    assertEquals("01738884932", kamerad.getTelefonnummer());
    assertEquals("max.mustermann@example.com", kamerad.getEmailAdresse());

    Kamerad kamerad2 = Kamerad.builder()
        .email("email1@test.de")
        .mitEmail("email2@test.de")
        .build();
    assertEquals("email2@test.de", kamerad2.getEmailAdresse());
  }

  @Test
  void testBuilderGeneratesRandomIdWhenNotSet() {
    LocalDate geburtsdatum = LocalDate.of(1985, 3, 20);
    Adresse adresse = new Adresse("Hauptstraße", "5", "14712", "Stechow");
    Kamerad kamerad = Kamerad.builder()
        .vorname("Max")
        .nachname("Mustermann")
        .geburtsdatum(geburtsdatum)
        .adresse(adresse)
        .build();

    assertNotNull(kamerad.getId());
    assertDoesNotThrow(() -> UUID.fromString(kamerad.getId()));
    assertEquals("Max", kamerad.getVorname());
    assertEquals("Mustermann", kamerad.getNachname());
    assertEquals(geburtsdatum, kamerad.getGeburtsdatum());
    assertEquals(adresse, kamerad.getAdresse());
  }

  @Test
  void testBerechneAlterMitStichtag() {
    LocalDate geburtsdatum = LocalDate.of(1990, 5, 15);
    Kamerad kamerad = Kamerad.builder()
        .vorname("Max")
        .nachname("Mustermann")
        .geburtsdatum(geburtsdatum)
        .build();

    // Vor dem Geburtstag im selben Jahr
    assertEquals(33, kamerad.berechneAlter(LocalDate.of(2024, 5, 14)));
    // Genau am Geburtstag
    assertEquals(34, kamerad.berechneAlter(LocalDate.of(2024, 5, 15)));
    // Nach dem Geburtstag
    assertEquals(34, kamerad.berechneAlter(LocalDate.of(2024, 5, 16)));
  }

  @Test
  void testBerechneAlterMitAktuellemDatum() {
    LocalDate geburtsdatum = LocalDate.now().minusYears(30).minusDays(10);
    Kamerad kamerad = Kamerad.builder()
        .vorname("Max")
        .nachname("Mustermann")
        .geburtsdatum(geburtsdatum)
        .build();

    assertEquals(30, kamerad.berechneAlter());
  }

  @Test
  void testBerechneAlterWirftExceptionWennGeburtsdatumNull() {
    Kamerad kamerad = Kamerad.builder()
        .vorname("Max")
        .nachname("Mustermann")
        .build();

    assertThrows(IllegalStateException.class, kamerad::berechneAlter);
    assertThrows(IllegalStateException.class, () -> kamerad.berechneAlter(LocalDate.of(2024, 1, 1)));
  }

  @Test
  void testBerechneAlterWirftExceptionWennStichtagNull() {
    Kamerad kamerad = Kamerad.builder()
        .vorname("Max")
        .nachname("Mustermann")
        .geburtsdatum(LocalDate.of(1990, 1, 1))
        .build();

    assertThrows(IllegalArgumentException.class, () -> kamerad.berechneAlter(null));
  }

  @Test
  void testEqualsAndHashCode() {
    Kamerad kamerad1 = Kamerad.builder()
        .id("kamerad-id-1")
        .vorname("Max")
        .nachname("Mustermann")
        .build();

    Kamerad kamerad2 = Kamerad.builder()
        .id("kamerad-id-1")
        .vorname("Maximilian")
        .nachname("Mustermann")
        .build();

    Kamerad kamerad3 = Kamerad.builder()
        .id("kamerad-id-2")
        .vorname("Max")
        .nachname("Mustermann")
        .build();

    assertEquals(kamerad1, kamerad2);
    assertEquals(kamerad1.hashCode(), kamerad2.hashCode());
    assertNotEquals(kamerad1, kamerad3);
    assertNotNull(kamerad1);
    assertFalse(kamerad1.equals(null));
    assertFalse(kamerad1.equals(new Object()));
  }

  @Test
  void testAendernUebernimmtAlleDatenUndBehaeltId() {
    Kamerad kamerad = Kamerad.builder()
        .id("kam-1")
        .vorname("Max")
        .nachname("Mustermann")
        .telefonnummer("0170 1234567")
        .build();
    Adresse neueAdresse = new Adresse("Lindenallee", "3", "14712", "Göttlin");

    Kamerad result = kamerad.aendern("Konrad", "Eichstädt", LocalDate.of(1980, 1, 1), neueAdresse, null,
        "konrad@example.com");

    assertEquals(kamerad, result);
    assertEquals("kam-1", kamerad.getId());
    assertEquals("Konrad", kamerad.getVorname());
    assertEquals("Eichstädt", kamerad.getNachname());
    assertEquals(LocalDate.of(1980, 1, 1), kamerad.getGeburtsdatum());
    assertEquals(neueAdresse, kamerad.getAdresse());
    assertNull(kamerad.getTelefonnummer());
    assertEquals("konrad@example.com", kamerad.getEmailAdresse());
  }

  @Test
  void testAendernOhneNamenWirftException() {
    Kamerad kamerad = Kamerad.builder().vorname("Max").nachname("Mustermann").build();

    assertThrows(IllegalArgumentException.class, () -> kamerad.aendern(null, "Eichstädt", null, null, null, null));
    assertThrows(IllegalArgumentException.class, () -> kamerad.aendern("Konrad", " ", null, null, null, null));
    assertEquals("Max", kamerad.getVorname());
    assertEquals("Mustermann", kamerad.getNachname());
  }
}
